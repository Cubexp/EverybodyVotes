package com.zj.everybodyvotes.controller;

import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.aliyuncs.exceptions.ClientException;
import com.zj.everybodyvotes.annotaation.Authority;
import com.zj.everybodyvotes.constant.CommonResponseEnum;
import com.zj.everybodyvotes.constant.ImgTypeConstant;
import com.zj.everybodyvotes.constant.RoleEnum;
import com.zj.everybodyvotes.domain.request.UpUserPhoneRequest;
import com.zj.everybodyvotes.domain.request.ForgivePasswordRequest;
import com.zj.everybodyvotes.domain.request.UpUserEmailRequest;
import com.zj.everybodyvotes.domain.request.UpUserInfoRequest;
import com.zj.everybodyvotes.domain.request.UserUpdatePasswordRequest;
import com.zj.everybodyvotes.domain.response.BaseResponse;
import com.zj.everybodyvotes.domain.response.CommonsResponse;
import com.zj.everybodyvotes.domain.response.UserInfoResponse;
import com.zj.everybodyvotes.exception.UserException;
import com.zj.everybodyvotes.service.ISysUserService;
import com.zj.everybodyvotes.utils.FileTypeUtil;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Objects;

/**
 * @author cuberxp
 * @date 2021/5/9 11:41 下午
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final ISysUserService iSysUserService;

    /**
     * 忘记密码
     */
    @PutMapping("/forgive/password")
    public BaseResponse forgetPassword(@Validated @RequestBody ForgivePasswordRequest forgivePasswordRequest) {
        iSysUserService.forgivePassword(forgivePasswordRequest);

        return new BaseResponse();
    }

    /**
     * 用户修改自己的密码
     */
    @Authority(role = {RoleEnum.USER, RoleEnum.ADMIN})
    @PutMapping("/password")
    public BaseResponse updatePassword(@Validated @RequestBody UserUpdatePasswordRequest userUpdatePassword) {
        iSysUserService.selectUserInfo(userUpdatePassword.getUserId());
        iSysUserService.updateUserPassword(userUpdatePassword);

        return new BaseResponse();
    }

    /**
     * 用户修改自己的头像
     */
    @Authority(role = {RoleEnum.USER, RoleEnum.ADMIN})
    @PostMapping("/avatar")
    public CommonsResponse<String> updateAvatar(@RequestPart("file") MultipartFile mulFile) throws IOException, UfileServerException, UfileClientException {
        ImgTypeConstant type = FileTypeUtil.getType(mulFile);

        if (type == null || ImgTypeConstant.XLS == type || ImgTypeConstant.XLSX == type) {
            throw new UserException(CommonResponseEnum.FILE_ERROR);
        }
        String url = iSysUserService.uploadCloudFile(mulFile.getInputStream(), mulFile.getContentType(), Objects.requireNonNull(type).getSuffix());

        return new CommonsResponse<>(url);
    }

    /**
     * 用户修改自己的个人信息
     */
    @Authority(role = {RoleEnum.USER, RoleEnum.ADMIN})
    @PutMapping("/{id}/info")
    public BaseResponse updateUserInfo(@PathVariable("id") Long userId, @Validated @RequestBody UpUserInfoRequest upUserInfoRequest) {
        iSysUserService.updateUserInfo(userId, upUserInfoRequest);

        return new BaseResponse();
    }

    /**
     * 用户修改邮箱时，邮箱验证码
     */
    @Authority(role = { RoleEnum.USER, RoleEnum.ADMIN })
    @PostMapping("/seder/email/verify")
    public CommonsResponse<String> sendEmailVerifyCode(@RequestParam("email") String oldEmail) throws MessagingException, ClientException {
        String emailVerifyCode = iSysUserService.sendEmailVerifyCode(oldEmail);

        return new CommonsResponse<>(emailVerifyCode);
    }

    /**
     * 用户修改邮箱
     */
    @Authority(role = {RoleEnum.USER, RoleEnum.ADMIN})
    @PutMapping("/{id}/email")
    public BaseResponse updateUserEmail(@PathVariable("id") Long id, @Validated @RequestBody UpUserEmailRequest upUserEmailRequest) {
        iSysUserService.checkEmailOnly(upUserEmailRequest.getNewEmail());

        iSysUserService.updateUserEmail(id, upUserEmailRequest);
        return new BaseResponse();
    }

    /**
     * 用户修改手机号码
     */
    @Authority(role = {RoleEnum.USER, RoleEnum.ADMIN})
    @PutMapping("/{id}/phone")
    public BaseResponse updateUserPhone(@PathVariable("id") Long id, @Validated @RequestBody UpUserPhoneRequest upUserPhoneRequest) {
        iSysUserService.checkPhoneOnly(upUserPhoneRequest.getNewPhone());

        iSysUserService.updateUserPhone(id, upUserPhoneRequest);
        return new BaseResponse();
    }

    @Authority(role = {RoleEnum.USER, RoleEnum.ADMIN})
    @GetMapping("/{id}")
    public CommonsResponse<UserInfoResponse> getUserInfo(@PathVariable("id") Long id) {
        UserInfoResponse userInfoResponse = iSysUserService.selectUserInfo(id);

        return new CommonsResponse<>(userInfoResponse);
    }
}
