package com.kaede.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kaede
 * @create 2022-09-18
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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().logout().logoutUrl("/logout")
                .and().csrf().disable()
                .sessionManagement()    //开启会话管理
                .maximumSessions(1)     //允许同一个用户的会话最大的并发只能是一个客户端
                //会话被挤下线后的处理
                .expiredSessionStrategy(event -> {
                    HttpServletResponse resp = event.getResponse();
                    Map<String, Object> result = new HashMap<>();
                    result.put("msg", "当前会话已失效，请重新登录!");
                    resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    String s = new ObjectMapper().writeValueAsString(result);
                    resp.setContentType("application/json;charset=UTF-8");
                    String jsonStr = new ObjectMapper().writeValueAsString(result);
                    resp.getWriter().println(jsonStr);
                })
                .sessionRegistry(sessionRegistry())  //将session交给xxx管理
                .maxSessionsPreventsLogin(true);    //登录达到最大并发之后禁止再次登录
    }

    //对session做监听（不加也可以，但建议加）
    /*@Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }*/

    //注入操作session的对象
    @Autowired
    private FindByIndexNameSessionRepository sessionRepository;

    //创建session同步到redis中的方案
    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }

}
