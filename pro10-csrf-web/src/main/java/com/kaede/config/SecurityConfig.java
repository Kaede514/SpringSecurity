package com.kaede.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kaede
 * @create 2022-09-20
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").
            password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        //自定义接收json用户名和密码的key
        loginFilter.setUsernameParameter("uname");
        loginFilter.setPasswordParameter("pwd");
        //指定认证的url
        loginFilter.setFilterProcessesUrl("/login");
        //在LoginFilter中调用了getAuthenticationManager()，所以需要注入AuthenticationManager
        loginFilter.setAuthenticationManager(authenticationManagerBean());

        //认证成功处理
        loginFilter.setAuthenticationSuccessHandler((req, resp, authentication) -> {
            Map<String,Object> result = new HashMap<>();
            result.put("msg", "login success");
            //result.put("status", 200);
            resp.setStatus(HttpStatus.OK.value());
            result.put("用户信息", authentication.getPrincipal());
            resp.setContentType("application/json;charset=UTF-8");
            String jsonStr = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(jsonStr);
        });

        //认证失败处理
        loginFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
            Map<String,Object> result = new HashMap<>();
            result.put("msg", "login failure: " + ex.getMessage());
            //result.put("status", 500);
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setContentType("application/json;charset=UTF-8");
            String jsonStr = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(jsonStr);
        });

        return loginFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated()   //所有请求必须认证
            .and().formLogin()
            .and().logout()
            //配置异常处理
            .and().exceptionHandling()
            //认证异常处理
            .authenticationEntryPoint((req, resp, ex) -> {
                Map<String,Object> result = new HashMap<>();
                result.put("msg", "必须认证之后才能访问");
                resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                resp.setContentType("application/json;charset=UTF-8");
                String jsonStr = new ObjectMapper().writeValueAsString(result);
                resp.getWriter().println(jsonStr);
            })
            .and().csrf()
            //令牌存储机制，将令牌保存到cookie中，允许前端获取cookie（之后在前端将令牌组装到header中）
            //ps：GET请求不需要，针对POST请求
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    //自定义AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
