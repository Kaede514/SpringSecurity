package com.kaede.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
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
            password("{noop}123").roles("ADMIN").build());
        inMemoryUserDetailsManager.createUser(User.withUsername("lisa").
            password("{noop}123").roles("USER").build());
        return inMemoryUserDetailsManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers("/a").hasRole("ADMIN")  //xx请求访问必须具有指定角色
            .anyRequest().authenticated()
            .and().formLogin()
            .and().exceptionHandling()//异常处理
            //处理认证异常
            .authenticationEntryPoint((req, resp, e) -> {
                //还可以基于不同类型的异常做不同的处理
                if(e instanceof LockedException) {
                    //...
                }
                Map<String,Object> result = new HashMap<>();
                result.put("msg", "尚未认证，请进行认证操作！");
                resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                resp.setContentType("application/json;charset=UTF-8");
                String jsonStr = new ObjectMapper().writeValueAsString(result);
                resp.getWriter().println(jsonStr);
            })
            //处理授权异常
            .accessDeniedHandler((req, resp, e) -> {
                Map<String,Object> result = new HashMap<>();
                result.put("msg", "无权访问!");
                resp.setStatus(HttpStatus.FORBIDDEN.value());
                resp.setContentType("application/json;charset=UTF-8");
                String jsonStr = new ObjectMapper().writeValueAsString(result);
                resp.getWriter().println(jsonStr);
            })
            .and().csrf().disable();
    }

}
