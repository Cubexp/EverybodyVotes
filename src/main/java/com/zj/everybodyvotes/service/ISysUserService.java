package com.zj.everybodyvotes.service;

import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.everybodyvotes.domain.SysUser;
import com.zj.everybodyvotes.domain.request.*;
import com.zj.everybodyvotes.domain.response.UserInfoResponse;

import javax.mail.MessagingException;
import java.io.InputStream;

/**
 * @author cuberxp
 * @date 2021/5/9 11:48 下午
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 忘记密码
     * @param forgivePasswordRequest
     */
    void forgivePassword(ForgivePasswordRequest forgivePasswordRequest);

    void updateUserPassword(UserUpdatePasswordRequest userUpdatePasswordRequest);

    String uploadCloudFile(InputStream inputStream, String contentType, String suffix) throws UfileServerException, UfileClientException;

    void updateUserInfo(Long userId, UpUserInfoRequest upUserInfoRequest);

    String sendEmailVerifyCode(String email) throws MessagingException, ClientException;

    /**
     * 检查email是否己经存在了
     * @param newEmail
     */
    void checkEmailOnly(String newEmail);

    /**
     * 修改邮箱
     * @param upUserEmailRequest
     */
    void updateUserEmail(Long userId, UpUserEmailRequest upUserEmailRequest);

    /**
     * 检查phone是否己经存在了
     */
    void checkPhoneOnly(String phone);

    void updateUserPhone(Long id, UpUserPhoneRequest upUserEmailRequest);

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    UserInfoResponse selectUserInfo(Long userId);

    void selectUserOnly(Long userId);
}
