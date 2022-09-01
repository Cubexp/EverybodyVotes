package com.zj.everybodyvotes.service.impl;

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zj.everybodyvotes.constant.CommonResponseEnum;
import com.zj.everybodyvotes.constant.GiteeConstant;
import com.zj.everybodyvotes.constant.OauthConstant;
import com.zj.everybodyvotes.constant.RoleEnum;
import com.zj.everybodyvotes.dao.ISysUserDao;
import com.zj.everybodyvotes.domain.SysUser;
import com.zj.everybodyvotes.domain.dto.LoginedUserDTO;
import com.zj.everybodyvotes.domain.dto.OauthStateCacheDTO;
import com.zj.everybodyvotes.domain.request.LoginRequest;
import com.zj.everybodyvotes.domain.request.OauthLoginRequest;
import com.zj.everybodyvotes.domain.request.RegisterRequest;
import com.zj.everybodyvotes.domain.response.LoginResponse;
import com.zj.everybodyvotes.domain.response.OauthResponse;
import com.zj.everybodyvotes.exception.OauthException;
import com.zj.everybodyvotes.service.IAliYunShortMessageService;
import com.zj.everybodyvotes.service.IOauthService;
import com.zj.everybodyvotes.service.ISysUserService;
import com.zj.everybodyvotes.utils.DateUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.AuthGiteeScope;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * @author cuberxp
 * @date 2021/5/7 9:17 下午
 */
@Service
@AllArgsConstructor
public class OauthServiceImpl implements IOauthService {

    private final ISysUserDao iSysUserDao;

    private final SymmetricCrypto symmetricCrypto;

    private final IAliYunShortMessageService iAliYunShortMessageService;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ISysUserService iSysUserService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(SysUser::getPhone, loginRequest.getPhone());

        SysUser sysUser = iSysUserDao.selectOne(queryWrapper);

        checkPassword(sysUser.getPassword(), loginRequest.getPassword());

        String jwtToken= Jwts.builder()
                .setIssuer("zj")
                .setSubject("EverybodyVotesToken")
                .claim(OauthConstant.CLAIM_TOKEN, sysUser.getId())
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + OauthConstant.EXPIRATION_TIME))
                //签名算法
                .signWith(SignatureAlgorithm.HS512, OauthConstant.SECURITY_KEY)
                .compact();

        setUserCache(sysUser.getId(), sysUser.getRoleName());

        return new LoginResponse()
                .setName(sysUser.getName())
                .setId(sysUser.getId())
                .setToken(jwtToken)
                .setRoleName(sysUser.getRoleName());
    }

    private void setUserCache(Long id, String roleName) {
        String redisKey = OauthConstant.LOGINED_USER_REDIS_KEY + id;

        LoginedUserDTO loginedUserDTO = new LoginedUserDTO()
                .setUserId(id)
                .setRoleName(roleName);
        redisTemplate.opsForValue().set(redisKey, loginedUserDTO, OauthConstant.EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    private void checkPassword(String userDbPassword, String loginPassword) {
        userDbPassword = symmetricCrypto.decryptStr(userDbPassword);

        // 登陆密码错误
        if (!userDbPassword.equals(loginPassword)) {
            throw new OauthException(CommonResponseEnum.PASSWORD_ERROR);
        }
    }

    @Override
    public SysUser getSysUser(Long userId) {
        return iSysUserDao.selectById(userId);
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        iSysUserService.checkPhoneOnly(registerRequest.getPhone());

        iAliYunShortMessageService.checkShortMessage(registerRequest.getCodeKey(), registerRequest.getCode());

        SysUser sysUser = new SysUser()
                .setPhone(registerRequest.getPhone())
                .setName(registerRequest.getPhone() + "")
                .setRoleName(RoleEnum.USER.getRoleName())
                .setPassword(symmetricCrypto.encryptBase64(registerRequest.getPassword()))
                .setAvatar(OauthConstant.DEFAULT_AVATAR)
                .setRegisterTime(DateUtil.localDateTimeToStampTime(LocalDateTime.now()) * 1000)
                .setGender(1);
        iSysUserDao.insert(sysUser);
    }

    @Override
    public String getAuthorizeUrl(String source, String clientUrl) {
        AuthRequest authRequest = getAuthRequest(source);
        String state = AuthStateUtils.createState();
        String thirdUrl = authRequest.authorize(state);

        this.oauthStateCache(source, state, clientUrl);
        return thirdUrl;
    }

    private void oauthStateCache(String source, String state, String clientUrl) {
        OauthStateCacheDTO cacheDTO = new OauthStateCacheDTO()
                .setClientUrl(clientUrl);

        String redisKey = OauthConstant.OAUTH_STATE_KEY + source.toLowerCase() + ":" + state;
        redisTemplate.opsForValue().set(redisKey, cacheDTO);
    }

    private AuthRequest getAuthRequest(String source) {
        if (source.equals(OauthConstant.SOURCE_GITEE)) {
            return new AuthGiteeRequest(AuthConfig.builder()
                    .scopes(Collections.singletonList(AuthGiteeScope.USER_INFO.getScope()))
                    .clientId(GiteeConstant.CLIENT_ID)
                    .clientSecret(GiteeConstant.CLIENT_SECRET)
                    .redirectUri(GiteeConstant.REDIRECT_URI)
                    .build());
        } else {
            throw new OauthException(CommonResponseEnum.OAUTH_NOT_SUPPORT_SOURCE);
        }
    }

    @Override
    public OauthResponse oauthLogin(String source, AuthCallback callback) {
        AuthRequest authRequest = getAuthRequest(source);

        AuthResponse<AuthUser> loginResponse = authRequest.login(callback);

        if (loginResponse.ok()) {
            AuthUser authUser = loginResponse.getData();

            // 保存或者授权登陆数据
            this.modifyByAuthUser(authUser, source, callback.getState());
            OauthStateCacheDTO oauthStateCacheDTO = getOauthStateCache(source, callback.getState());


            return new OauthResponse()
                    .setSource(source)
                    .setClientUrl(oauthStateCacheDTO.getClientUrl())
                    .setOauthToken(callback.getState());
        }

        throw new OauthException(loginResponse.getCode(), loginResponse.getMsg());
    }

    private void modifyByAuthUser(AuthUser authUser, String source, String state) {
        // 保存获得的第三方平台数据，后面通过state 加 source 拿到
        String redisKey = OauthConstant.OAUTH_DATA_REDIS_KEY + state + ":" + source;
        redisTemplate.opsForValue().set(redisKey, authUser);
    }

    private OauthStateCacheDTO getOauthStateCache(String source, String state) {
        String redisKey = OauthConstant.OAUTH_STATE_KEY + source.toLowerCase() + ":" + state;

        return (OauthStateCacheDTO) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public LoginResponse doOauthLogin(OauthLoginRequest request) {
        // 逻辑
        // 1. 己经登陆 更新授权用户绑定的用户信息。
        // 2. 没有登陆- 己绑定 根据绑定的userId查询用户信息，执行登陆
        // 3. 没有登寻-没有绑定 返回错误码，没有账户绑定该第三方平台

        // oauthtoekn 为state, source为第三方平台地址
        String storageOauthDataKye = OauthConstant.OAUTH_DATA_REDIS_KEY + request.getOauthToken() + ":" + request.getSource();
        AuthUser authUser = (AuthUser)redisTemplate.opsForValue().get(storageOauthDataKye);



        // 己经登陆
//        String redisKey = OauthConstant.LOGINED_USER_REDIS_KEY + userId;
        boolean loginedFlag = Optional.ofNullable(redisTemplate.hasKey(null))
                .orElse(false);

        if (loginedFlag) {

        }
        return null;
    }

    @Override
    public SysUser findUser(String source, String uuid) {

        return null;
    }
}
