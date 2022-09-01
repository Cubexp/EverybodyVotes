package com.zj.everybodyvotes.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zj.everybodyvotes.constant.AliYunShortMessageConstant;
import com.zj.everybodyvotes.constant.CommonResponseEnum;
import com.zj.everybodyvotes.constant.EmailConstant;
import com.zj.everybodyvotes.dao.ISysUserDao;
import com.zj.everybodyvotes.domain.SysUser;
import com.zj.everybodyvotes.domain.request.*;
import com.zj.everybodyvotes.domain.response.UserInfoResponse;
import com.zj.everybodyvotes.exception.OauthException;
import com.zj.everybodyvotes.exception.UserException;
import com.zj.everybodyvotes.service.IAliYunShortMessageService;
import com.zj.everybodyvotes.service.IEmailService;
import com.zj.everybodyvotes.service.ISysUserService;
import com.zj.everybodyvotes.service.IUCloudService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author cuberxp
 * @date 2021/5/9 11:48 下午
 */
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<ISysUserDao, SysUser> implements ISysUserService {

    private final ISysUserDao iSysUserDao;

    private final IAliYunShortMessageService iAliYunShortMessageService;

    private final SymmetricCrypto symmetricCrypto;

    private final IUCloudService iuCloudService;

    private final IEmailService iEmailService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void forgivePassword(ForgivePasswordRequest forgivePasswordRequest) {
        iAliYunShortMessageService.checkShortMessage(forgivePasswordRequest.getCodeKey(), forgivePasswordRequest.getCode());

        LambdaQueryWrapper<SysUser> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        objectLambdaQueryWrapper.eq(SysUser::getPhone, forgivePasswordRequest.getPhone());

        Integer integer = iSysUserDao.selectCount(objectLambdaQueryWrapper);

        if (integer <= 0) {
            throw new OauthException(403, "无账户绑定该手机号码");
        }

        iSysUserDao.updateUserPhone(forgivePasswordRequest.getPhone(), symmetricCrypto.encryptBase64(forgivePasswordRequest.getPassword()));
    }

    @Override
    public void updateUserPassword(UserUpdatePasswordRequest userUpdatePasswordRequest) {
        checkPassword(userUpdatePasswordRequest.getNewPassword(), userUpdatePasswordRequest.getOldPassword(), userUpdatePasswordRequest.getUserId());

        SysUser sysUser = new SysUser()
                .setId(userUpdatePasswordRequest.getUserId())
                .setPassword(symmetricCrypto.encryptBase64(userUpdatePasswordRequest.getNewPassword()));

        iSysUserDao.update(sysUser, Wrappers.emptyWrapper());
    }

    private void checkPassword(String newPassword, String oldPassword, Long userId) {
        if (newPassword.equals(oldPassword)) {
            throw new UserException(CommonResponseEnum.OLD_NEW_PASSWORD_EQUAL);
        }
        String datasourceOldPassword = iSysUserDao.selectById(userId).getPassword();
        datasourceOldPassword = symmetricCrypto.decryptStr(datasourceOldPassword);

        if (!datasourceOldPassword.equals(oldPassword)) {
            throw new UserException(CommonResponseEnum.OLD_PASSWORD_ERROR);
        }
    }

    @Override
    public String uploadCloudFile(InputStream inputStream, String contentType, String suffix) throws UfileServerException, UfileClientException {
        String generateFileName = UUID.randomUUID()+ "." + suffix;

        iuCloudService.uploadEnclosure(inputStream,contentType, generateFileName);

        //单位秒 40年
        return iuCloudService.getEnclosure(generateFileName, 24 * 60 * 60 * 365 * 40);
    }

    @Override
    public void updateUserInfo(Long userId, UpUserInfoRequest upUserInfoRequest) {
        SysUser sysUser = new SysUser()
                .setId(userId)
                .setAvatar(upUserInfoRequest.getAvatar())
                .setGender(upUserInfoRequest.getGender())
                .setName( upUserInfoRequest.getName());

        iSysUserDao.updateById(sysUser);
    }

    @Override
    public String sendEmailVerifyCode(String email) throws MessagingException, ClientException {
        String verifyCode = randomVerifyString();
        String key = IdUtil.fastSimpleUUID();
        String emailVerifyCodeKey = EmailConstant.SHORT_MESSAGE_REDIS_KEY + key;

        redisTemplate.opsForValue().set(emailVerifyCodeKey, verifyCode, EmailConstant.SHORT_MESSAGE_KEY_EXPIRATION, TimeUnit.MINUTES);

        iEmailService.sendEmail(email, "人人投票", "你进行了修改邮箱操作，你的验证码为: " + verifyCode + " 验证有效期为5分钟！");
        return emailVerifyCodeKey;
    }

    private String randomVerifyString() {
        StringBuilder verifyString = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < AliYunShortMessageConstant.CODE_LEN; i++) {
            verifyString.append(secureRandom.nextInt(10));
        }

        return verifyString.toString();
    }

    @Override
    public void checkEmailOnly(String newEmail) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(SysUser::getEmail, newEmail);

        boolean flag = iSysUserDao.selectCount(queryWrapper) >= 1;

        if (flag) {
            throw new UserException(CommonResponseEnum.EMAIL_ONLY_ERROR);
        }
    }

    @Override
    public void updateUserEmail(Long userId, UpUserEmailRequest upUserEmailRequest) {
        String emailVerifyCodeKey = upUserEmailRequest.getVerifyCodeKey();

        String verifyCode = (String)Optional.ofNullable(redisTemplate.opsForValue().get(emailVerifyCodeKey))
                .orElseThrow(() -> new UserException(CommonResponseEnum.SHORT_MESSAGE_VERIFY_CODE_EXPIRATION));

        if (!verifyCode.equals(upUserEmailRequest.getVerifyCode())) {
            throw new UserException(CommonResponseEnum.SHORT_MESSAGE_VERIFY_CODE_NOT_EQUAL);
        }

        SysUser sysUser = new SysUser()
                .setId(userId)
                .setEmail(upUserEmailRequest.getNewEmail());

        iSysUserDao.updateById(sysUser);
    }

    @Override
    public void checkPhoneOnly(String newPhone) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(SysUser::getPhone, newPhone);

        boolean flag = iSysUserDao.selectCount(queryWrapper) >= 1;

        if (flag) {
            throw new UserException(CommonResponseEnum.PHONE_ONLY_ERROR);
        }
    }

    @Override
    public void updateUserPhone(Long id, UpUserPhoneRequest upUserPhoneRequest) {
        String emailVerifyCodeKey = upUserPhoneRequest.getVerifyCodeKey();

        String verifyCode = (String)Optional.ofNullable(redisTemplate.opsForValue().get(emailVerifyCodeKey))
                .orElseThrow(() -> new UserException(CommonResponseEnum.SHORT_MESSAGE_VERIFY_CODE_EXPIRATION));

        if (!verifyCode.equals(upUserPhoneRequest.getVerifyCode())) {
            throw new UserException(CommonResponseEnum.SHORT_MESSAGE_VERIFY_CODE_NOT_EQUAL);
        }

        SysUser sysUser = new SysUser()
                .setId(id)
                .setPhone(upUserPhoneRequest.getNewPhone());

        iSysUserDao.updateById(sysUser);
    }

    @Override
    public UserInfoResponse selectUserInfo(Long userId) {
        SysUser sysUser = iSysUserDao.selectById(userId);

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        BeanUtils.copyProperties(sysUser, userInfoResponse);
        userInfoResponse.setRegisterTime(sysUser.getRegisterTime());
        return userInfoResponse;
    }

    @Override
    public void selectUserOnly(Long userId) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getId, userId);

        boolean flag = iSysUserDao.selectCount(queryWrapper) <= 0;

        if (flag) {
            throw new OauthException(CommonResponseEnum.USER_NOT_EXIST_ERROR);
        }
    }
}
