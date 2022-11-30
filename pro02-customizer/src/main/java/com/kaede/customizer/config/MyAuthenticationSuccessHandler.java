package com.kaede.customizer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
 * 自定义成功认证后的处理
 */

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Map<String,Object> result = new HashMap<>();
        result.put("msg", "login success");
        result.put("status", 200);
        result.put("authentication",authentication);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String jsonStr = new ObjectMapper().writeValueAsString(result);
        httpServletResponse.getWriter().println(jsonStr);
    }

}
