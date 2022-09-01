package com.zj.everybodyvotes.controller;

import cn.hutool.core.lang.Validator;
import com.zj.everybodyvotes.annotaation.Authority;
import com.zj.everybodyvotes.constant.OauthConstant;
import com.zj.everybodyvotes.constant.RoleEnum;
import com.zj.everybodyvotes.domain.request.LoginRequest;
import com.zj.everybodyvotes.domain.request.OauthLoginRequest;
import com.zj.everybodyvotes.domain.request.RegisterRequest;
import com.zj.everybodyvotes.domain.response.BaseResponse;
import com.zj.everybodyvotes.domain.response.CommonsResponse;
import com.zj.everybodyvotes.domain.response.LoginResponse;
import com.zj.everybodyvotes.domain.response.OauthResponse;
import com.zj.everybodyvotes.service.IAliYunShortMessageService;
import com.zj.everybodyvotes.service.IOauthService;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cuberxp
 * @date 2021/5/7 7:44 下午
 */
@RequestMapping("/oauth")
@RestController
@AllArgsConstructor
public class OauthController {

    private final IOauthService iOauthService;

    private final IAliYunShortMessageService iAliYunShortMessageService;

    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/login")
    public CommonsResponse<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        LoginResponse loginResponse = iOauthService.login(loginRequest);

        return new CommonsResponse<>(loginResponse);
    }

    @PutMapping("/logout")
    public BaseResponse logout(HttpServletRequest request) {
        String jwtHeader = request.getHeader(OauthConstant.FRONT_POINT_HEADER_TOKEN);

        long userId = Long.parseLong(this.decodeToken(jwtHeader));

        String redisKey = OauthConstant.LOGINED_USER_REDIS_KEY + userId;

        redisTemplate.delete(redisKey);
        return new BaseResponse();
    }

    @PostMapping("/register")
    public BaseResponse register(@Validated @RequestBody RegisterRequest registerRequest) {
        iOauthService.register(registerRequest);

        return new BaseResponse();
    }

    @PostMapping("/send/short/message")
    public CommonsResponse<String> sendShortMessage(@RequestParam("phone") String phone) throws Exception {
        Validator.validateMobile(phone, "手机号码格式不正确!");
        String codeKey = iAliYunShortMessageService.sendShortMessage(phone);

        return new CommonsResponse<>(codeKey);
    }

    /**
     * 获取第三方登陆，重定向地址
     */
    @RequestMapping("/render/{source}/anon")
    public ModelAndView renderAuth(@PathVariable("source") String source,
                                   @RequestParam("clientUrl") String clientUrl) {
        String authorizeUrl = iOauthService.getAuthorizeUrl(source, clientUrl);
        return new ModelAndView("redirect:" + authorizeUrl);
    }

    /**
     * 第三方登陆回调地址
     */
    @RequestMapping("/callback/{source}/anon")
    public ModelAndView login(@PathVariable("source") String source,
                              AuthCallback callback) {
        OauthResponse oauthResponse = iOauthService.oauthLogin(source, callback);
        return new ModelAndView(oauthResponse.getClientUrl() + "?" + "oauthToken=" + oauthResponse.getOauthToken() + "&source=" + oauthResponse.getSource());
    }

    /**
     * 获取第三方登陆数据
     */
    @RequestMapping("/do_oauth_login/anon")
    public CommonsResponse<LoginResponse> doOauthLogin(@RequestBody OauthLoginRequest request) {
        LoginResponse loginResponse = iOauthService.doOauthLogin(request);
        return new CommonsResponse<>(loginResponse);
    }

    private String decodeToken(String jwtHeader) {
        return Jwts.parser()
                .setSigningKey(OauthConstant.SECURITY_KEY)
                .parseClaimsJws(jwtHeader)
                .getBody()
                .get(OauthConstant.CLAIM_TOKEN)
                .toString()
                ;
    }
}
