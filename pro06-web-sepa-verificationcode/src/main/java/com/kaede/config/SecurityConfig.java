package com.kaede.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaede.security.filter.LoginKaptchaFilter;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author kaede
 * @create 2022-09-18
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //自定义内存的数据源
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

    //用来将自定义的AuthenticationManager在工厂中进行暴露
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //配置自定义filter
    @Bean
    public LoginKaptchaFilter loginKaptchaFilter() throws Exception {
        LoginKaptchaFilter loginKaptchaFilter = new LoginKaptchaFilter();
        //1.指定处理登录请求的url
        loginKaptchaFilter.setFilterProcessesUrl("/login");
        //2.指定接收认证参数
        loginKaptchaFilter.setUsernameParameter("uname");
        loginKaptchaFilter.setPasswordParameter("pwd");
        loginKaptchaFilter.setKaptchaParameter("kaptcha");
        //3.指定认证管理器
        loginKaptchaFilter.setAuthenticationManager(authenticationManagerBean());
        //4.指定认证成功时的处理
        loginKaptchaFilter.setAuthenticationSuccessHandler((req,resp,authentication) -> {
            Map<String,Object> result = new HashMap<>();
            result.put("msg", "登录成功");
            //result.put("state", 200);
            resp.setStatus(HttpStatus.OK.value());
            result.put("用户信息", authentication.getPrincipal());
            resp.setContentType("application/json;charset=UTF-8");
            String jsonStr = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(jsonStr);
        });

        //5.指定认证失败时的处理
        loginKaptchaFilter.setAuthenticationFailureHandler((req,resp,ex) -> {
            Map<String,Object> result = new HashMap<>();
            result.put("msg", "登录失败 " + ex.getMessage());
            //result.put("state", 500);
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setContentType("application/json;charset=UTF-8");
            String jsonStr = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(jsonStr);
        });

        return loginKaptchaFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/vc.jpg").permitAll()
                .anyRequest().authenticated()   //所有请求必须认证
                .and().formLogin()
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((req,resp,authentication) -> {
                    Map<String,Object> result = new HashMap<>();
                    result.put("msg", "退出登录成功 " + authentication);
                    //result.put("state", 200);
                    resp.setStatus(HttpStatus.OK.value());
                    resp.setContentType("application/json;charset=UTF-8");
                    String jsonStr = new ObjectMapper().writeValueAsString(result);
                    resp.getWriter().println(jsonStr);
                })
                .and().exceptionHandling()
                .authenticationEntryPoint((req, resp, ex) -> {
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.getWriter().println("必须认证之后才能访问!");
                })
                .and().csrf().disable();
        http.addFilterAt(loginKaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
