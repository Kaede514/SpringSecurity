package com.kaede.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaede.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kaede
 * @create 2022-09-18
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //自定义的filter交给Spring管理（放入IOC容器中）
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

        //configure中设置的successHandler()和failureHandler()是设置UsernamePasswordAuthenticationFilter中的handler
        //现在改为了LoginFilter，所以在此处设置successHandler()和failureHandler()
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
            .logoutUrl("/logout")
            .logoutSuccessHandler((req, resp, authentication) -> {
                Map<String,Object> result = new HashMap<>();
                result.put("msg", "logout success");
                //result.put("status", 200);
                resp.setStatus(HttpStatus.OK.value());
                result.put("authentication",authentication);
                resp.setContentType("application/json;charset=UTF-8");
                String jsonStr = new ObjectMapper().writeValueAsString(result);
                resp.getWriter().println(jsonStr);
            })
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
            .and().csrf().disable();
        //往当前的过滤器链中添加或替换过滤器
        //http.addFilterAt();       //用指定filter替换过滤器链中的某个filter
        //http.addFilterBefore();   //将指定filter放在过滤器链中某个filter之前
         //http.addFilterAfter();    //将指定filter放在过滤器链中某个filter之后
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    private MyUserDetailService myUserDetailService;

    //自定义AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(myUserDetailService);
    }

    //用来将自定义的AuthenticationManager在工厂中进行暴露，之后可以在任何位置注入
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
