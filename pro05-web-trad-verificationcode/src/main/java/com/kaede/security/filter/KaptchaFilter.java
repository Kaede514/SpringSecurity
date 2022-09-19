package com.kaede.security.filter;

import com.kaede.security.exception.KaptchaNotMatchException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kaede
 * @create 2022-09-19
 *
 * 自定义验证码的filter
 */

public class KaptchaFilter extends UsernamePasswordAuthenticationFilter {

    private static final String FROM_KAPTCHA_KEY = "kaptcha";

    private String kaptchaParameter = FROM_KAPTCHA_KEY;

    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //1.从请求中获取验证码
        String verityCode = request.getParameter(getKaptchaParameter());
        //2.与seesion中的验证码进行比较
        String sessionVerityCode = (String) request.getSession().getAttribute("kaptcha");
        if (!ObjectUtils.isEmpty(verityCode) && !ObjectUtils.isEmpty(sessionVerityCode)
            && verityCode.equalsIgnoreCase(sessionVerityCode)
        ) {
            //返回Authentication说明认证成功
            return super.attemptAuthentication(request, response);
        }
        //抛AuthenticationException异常说明认证失败
        throw new KaptchaNotMatchException("验证码不匹配！");
    }
}
