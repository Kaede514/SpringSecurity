package com.kaede.customizer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kaede
 * @create 2022-09-18
 *
 * 自定义认证失败时的处理
 */

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Map<String,Object> result = new HashMap<>();
        result.put("msg", "login failure: " + e.getMessage());
        result.put("status", 500);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String jsonStr = new ObjectMapper().writeValueAsString(result);
        httpServletResponse.getWriter().println(jsonStr);
    }

}
