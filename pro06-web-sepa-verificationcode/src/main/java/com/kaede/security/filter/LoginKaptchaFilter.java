package com.kaede.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaede.security.exception.KaptchaNotMatchException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author kaede
 * @create 2022-09-19
 *
 * 自定义验证码的filter
 */

public class LoginKaptchaFilter extends UsernamePasswordAuthenticationFilter {

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
        //1.判断是否是POST方式请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //2.判断是否是json格式的请求类型
        if(!request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            throw new AuthenticationServiceException("Authentication data not supported: " + request.getMethod());
        }
        //3.从json数据中获取用户输入的用户名、密码和验证码进行认证
        try {
            //3.1.获取请求数据
            Map<String,String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            //3.2.获取数据中的验证码
            String verityCode = userInfo.get(getKaptchaParameter());
            String username = userInfo.get(getUsernameParameter());
            String password = userInfo.get(getPasswordParameter());
            //3.3.获取session中的验证码
            String sessionVerityCode = (String) request.getSession().getAttribute("kaptcha");
            if (!ObjectUtils.isEmpty(verityCode) && !ObjectUtils.isEmpty(sessionVerityCode)
                && verityCode.equalsIgnoreCase(sessionVerityCode)
            ) {
                //3.4.封装认证
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, password);
                setDetails(request, authRequest);
                //返回Authentication说明认证成功
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //抛AuthenticationException异常说明认证失败
        throw new KaptchaNotMatchException("验证码不匹配！");
    }
}
