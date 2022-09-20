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
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author kaede
 * @create 2022-09-18
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*@Autowired
    private MyUserDetailService myUserDetailService;*/

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
        //auth.userDetailsService(myUserDetailService);
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
        //设置认证成功时使用自定义的RememberMeServices
        loginFilter.setRememberMeServices(rememberMeServices());

        //认证成功处理
        loginFilter.setAuthenticationSuccessHandler((req,resp,authentication) -> {
            Map<String,Object> result = new HashMap<>();
            result.put("msg", "login success");
            //result.put("state", 200);
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
            //result.put("state", 500);
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
                //开启记住我功能，cookie进行实现
                //1.认证成功后保存记住我cookie到客户端
                //2.只有cookie写入客户端成功，才能实现自动登录功能
                .and().rememberMe()
                //设置自动登录使用的RememberMeServices
                .rememberMeServices(rememberMeServices())
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((req,resp,authentication) -> {
                    Map<String,Object> result = new HashMap<>();
                    result.put("msg", "logout success: " + authentication);
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
        //往当前的过滤器链中添加或替换过滤器
        //http.addFilterAt(); //用指定filter替换过滤器链中的某个filter
        //http.addFilterBefore();   //将指定filter放在过滤器链中某个filter之前
        //http.addFilterAfter();   //将指定filter放在过滤器链中某个filter之后
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new MyPersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(),
            userDetailsService(),new InMemoryTokenRepositoryImpl());
    }

}
