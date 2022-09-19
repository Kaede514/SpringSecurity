package com.kaede.pro02.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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
 * 自定义注销成功后的处理
 */

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Map<String,Object> result = new HashMap<>();
        result.put("msg", "logout success: " + authentication);
        result.put("state", 200);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String jsonStr = new ObjectMapper().writeValueAsString(result);
        httpServletResponse.getWriter().println(jsonStr);
    }

}
