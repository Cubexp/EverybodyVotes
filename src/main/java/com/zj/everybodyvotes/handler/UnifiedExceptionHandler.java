package com.zj.everybodyvotes.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.zj.everybodyvotes.domain.response.BaseResponse;
import com.zj.everybodyvotes.exception.OauthException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局统一异常处理类
 * @author cuberxp
 * @date 2021/5/7 9:51 下午
 */
@Component
@RestControllerAdvice
public class UnifiedExceptionHandler {

    /**
     * 表单数据绑定异当常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidException(MethodArgumentNotValidException e, HttpServletResponse response) throws IOException {
        System.out.println("MethodArgumentNotValidException");
        e.printStackTrace();
        String errorMessage = wrapperBindingResult(e.getBindingResult());

        BaseResponse baseResponse = new BaseResponse(403, errorMessage);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(JSONUtil.toJsonStr(baseResponse));
    }

    /**
     * json数据绑定检校异常
     */
    @ExceptionHandler(value = BindException.class)
    public void handleBindException(BindException e, HttpServletResponse response) throws IOException {
        System.out.println("BindException");
        e.printStackTrace();
        String errorMessage = wrapperBindingResult(e.getBindingResult());
        BaseResponse baseResponse = new BaseResponse(403, errorMessage);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(JSONUtil.toJsonStr(baseResponse));
    }

    /**
     * 我无语了ssm真的难用。。。
     * 下面的代码在spring boot 无需这样写
     */
    @ExceptionHandler(OauthException.class)
    public void handleLoginException(OauthException exception, HttpServletResponse response) throws IOException {
        System.out.println("OauthException");
        exception.printStackTrace();
        BaseResponse baseResponse = new BaseResponse(exception.getCode(), exception.getMessage());

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(JSONUtil.toJsonStr(baseResponse));
    }

    /**
     * 未定义的异常
     * 如数据库异常
     * 一般是500
     * @param e 出现的异常
     */
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletResponse response) throws IOException {
        System.out.println("未定义的异常");
        e.printStackTrace();

        BaseResponse baseResponse = new BaseResponse(403, e.getLocalizedMessage());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(JSONUtil.toJsonStr(baseResponse));
    }

    /**
     * 对<code>MethodArgumentNotValidException e</code>中出现的异常信息，进行包装
     * @param bindingResult 绑定结果
     * @return 错误信息
     */
    private String wrapperBindingResult(BindingResult bindingResult){
        return bindingResult.getAllErrors().stream()
                .filter(objectError -> objectError instanceof FieldError)
                .map(objectError -> ((FieldError) objectError).getField() + ":" + objectError.getDefaultMessage())
                .collect(StringBuilder::new,
                        (stringBuilder, s) -> stringBuilder.append(",").append(s),
                        StringBuilder::append)
                .substring(1);
    }
}
