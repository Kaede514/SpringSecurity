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

    //内存数据源
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").
            password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }

    //指定全局AuthenticationManager使用内存数据源
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }


    //用来将自定义的AuthenticationManager在工厂中进行暴露，之后可以在任何位置注入
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //自定义的filter交给Spring管理（放入IOC容器中）
    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        //指定接收json用户名和密码的key
        loginFilter.setUsernameParameter("uname");
        loginFilter.setPasswordParameter("pwd");
        loginFilter.setFilterProcessesUrl("/login");  //指定处理登录请求的url
        loginFilter.setAuthenticationManager(authenticationManagerBean());

        //认证成功处理
        loginFilter.setAuthenticationSuccessHandler((req,resp,authentication) -> {
            Map<String,Object> result = new HashMap<>();
            result.put("msg", "login success");
            resp.setStatus(HttpStatus.OK.value());
            result.put("用户信息", authentication.getPrincipal());
            resp.setContentType("application/json;charset=UTF-8");
            String jsonStr = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(jsonStr);
        });

        //认证失败处理
        loginFilter.setAuthenticationFailureHandler((req,resp,ex) -> {
            Map<String,Object> result = new HashMap<>();
            result.put("msg", "login failure: " + ex.getMessage());
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
            .anyRequest().authenticated()
            .and().formLogin()
            .and().csrf()
            //令牌存储机制，将令牌保存到cookie中，允许前端获取cookie（之后在前端将令牌组装到header中）
            //ps：GET请求不需要，针对POST请求
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
