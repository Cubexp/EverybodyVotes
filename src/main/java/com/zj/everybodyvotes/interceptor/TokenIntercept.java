package com.zj.everybodyvotes.interceptor;

import com.zj.everybodyvotes.annotaation.Authority;
import com.zj.everybodyvotes.constant.CommonResponseEnum;
import com.zj.everybodyvotes.constant.OauthConstant;
import com.zj.everybodyvotes.constant.RoleEnum;
import com.zj.everybodyvotes.domain.dto.LoginedUserDTO;
import com.zj.everybodyvotes.exception.OauthException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author cuberxp
 * @date 2021/5/8 1:16 下午
 */
public class TokenIntercept implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof  HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method method = handlerMethod.getMethod();

            //该方法上是否有权限注解
            if(method.isAnnotationPresent(Authority.class)){
                Authority authorityAnnotation = method.getAnnotation(Authority.class);

                RoleEnum[] roles = authorityAnnotation.role();

                // 游客直接放进
                if (Arrays.asList(roles).contains(RoleEnum.TOURIST)) {
                    return true;
                }

                String jwtHeader = request.getHeader(OauthConstant.FRONT_POINT_HEADER_TOKEN);

                // 没有携带token
                if (jwtHeader == null) {
                    throw new OauthException(CommonResponseEnum.OAUTH_ERROR);
                }

                long userId = Long.parseLong(this.decodeToken(jwtHeader));

                // 获得缓存中的用户id与角色
                LoginedUserDTO userCache = getUserCache(userId);

                boolean isRoleFlag = Arrays.stream(roles).anyMatch(roleEnum -> roleEnum.getRoleName().equals(userCache.getRoleName()));

                // 无权限访问该接口
                if (!isRoleFlag) {
                    throw new OauthException(CommonResponseEnum.OAUTH_ERROR);
                }
            }
        }

        return true;
    }

    private LoginedUserDTO getUserCache(long userId) {
        String redisKey = OauthConstant.LOGINED_USER_REDIS_KEY + userId;

        if (redisTemplate.hasKey(redisKey)) {
            return (LoginedUserDTO) redisTemplate.opsForValue().get(redisKey);
        } else {
            // token己过期
            throw new OauthException(CommonResponseEnum.TOKEN_TIMEOUT);
        }
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
