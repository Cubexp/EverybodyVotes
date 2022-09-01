package com.zj.everybodyvotes.service;

import com.zj.everybodyvotes.domain.SysUser;
import com.zj.everybodyvotes.domain.request.LoginRequest;
import com.zj.everybodyvotes.domain.request.OauthLoginRequest;
import com.zj.everybodyvotes.domain.request.RegisterRequest;
import com.zj.everybodyvotes.domain.response.LoginResponse;
import com.zj.everybodyvotes.domain.response.OauthResponse;
import me.zhyd.oauth.model.AuthCallback;

/**
 * @author cuberxp
 * @date 2021/5/7 9:16 下午
 */
public interface IOauthService {

    /**
     * 登陆
     * @param loginRequest {@link LoginRequest}
     * @return 访问的token
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 根据用户id查询用户数据
     * @param userId 用户
     * @return {@link SysUser}
     */
    SysUser getSysUser(Long userId);

    /**
     * 注册
     * @param registerRequest {@link RegisterRequest}
     */
    void register(RegisterRequest registerRequest);

    /**
     * 获得重定向地址
     * @param source
     * @param clientUrl 前端当前页面的url
     * @return
     */
    String getAuthorizeUrl(String source, String clientUrl);

    /**
     *
     * @param source 授权登录的source字符串
     * @param callback 第三方授权回调的参数
     * @return
     */
    OauthResponse oauthLogin(String source, AuthCallback callback);

    /**
     * 登到oauth授权登陆后的token
     * @param request
     * @return
     */
    LoginResponse doOauthLogin(OauthLoginRequest request);

    /**
     * 根据uuid与source获取用户数据
     * @param source
     * @param uuid
     * @return
     */
    SysUser findUser(String source, String uuid);
}
