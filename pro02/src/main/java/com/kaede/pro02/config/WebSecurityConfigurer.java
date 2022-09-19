package com.kaede.pro02.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

/**
 * @author kaede
 * @create 2022-09-17
 */

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //放行/和/index，放行的资源要写在拦截的前面
        http.authorizeRequests()
                .mvcMatchers("/","/index","/toLogin").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()  //form表单认证
                .loginPage("/toLogin")   //自定义默认    登录界面，注意：一旦自定义登录页面后必须指定登录的url
                .loginProcessingUrl("/doLogin") //指定处理登录请求的url
                .usernameParameter("uname") //自定义请求参数
                .passwordParameter("pwd")   //自定义请求参数
                //认证成功后跳转的路径（未设置时是重定向到请求路径），始终在认证成功后跳转到指定的请求
                //.successForwardUrl("/index")
                //认证成功后重定向的路径（和上面的跳转最多只能用一个），和上面的区别是这个会优先重定向到
                //上一次被拦截的请求，可以设置第二个参数为true，此时就会始终在认证成功后重定向到指定的请求
                //.defaultSuccessUrl("/index")
                .successHandler(new MyAuthenticationSuccessHandler())  //认证成功时的处理（适用于前后端分离）
                //.failureForwardUrl("/doLogin")    //认证失败之后forward跳转
                //.failureUrl("/doLogin")   //认证失败之后的redirect跳转（默认）
                .failureHandler(new MyAuthenticationFailureHandler())   //用来自定义认证失败后的处理（适用于前后端分离）
                .and().logout() //取得注销登录的配置对象，注销登录默认开启
                //.logoutUrl("/logout")    //指定注销登录的URL（必须以get方式请求）
                .logoutRequestMatcher(new OrRequestMatcher( //指定多个注销登录的URL以请求方式
                    new AntPathRequestMatcher("/aa","GET"),
                    new AntPathRequestMatcher("/bb","POST")
                ))
                .invalidateHttpSession(true)    //是否让当前session失效（默认会使失效）
                .clearAuthentication(true)  //是否清除当前认证信息（默认会清除）
                //.logoutSuccessUrl("/doLogin") //指定注销登录成功后重定向到的界面
                .logoutSuccessHandler(new MyLogoutSuccessHandler()) //自定义注销登录成功后的处理（适用于前后端分离）
                .and().csrf().disable();  //禁用csrf，跨站请求保护
        //自定义时，表单的提交方式必须为POST，对应字段默认为username和password
        //流程梳理，浏览器请求被拦截路径，然后服务器拦截后重定向到自定义的登录界面，提交表单并通过
        //验证后放行到一开始请求的路径
    }

    //SpringBoot对SpringSecurity的默认配置中，在工厂中默认创建AuthenticationManager
    /*@Autowired  //注入AuthenticationManagerBuilder
    public void initialize(AuthenticationManagerBuilder builder) throws Exception {
        //org.springframework.security.config.annotation.authentication.configuration
        System.out.println("springboot中的默认配置：" + builder);
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        //默认情况下用配置文件中的配置，这里对全局进行再次覆盖，用这里指定的信息
        userDetailsService.createUser(User.withUsername("kaede").password("{noop}123").roles("admin").build());
        builder.userDetailsService(userDetailsService);
    }*/

    /*@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        //{noop}代表明文
        userDetailsService.createUser(User.withUsername("kaede").password("{noop}123").roles("admin").build());
        return userDetailsService;
    }*/

    //自定义AuthenticationManager（会覆盖默认的）（推荐使用）
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter$2@66e889df
        System.out.println("自定义的AuthenticationManager：" + builder);
        //builder.userDetailsService(userDetailsService());//更改的是DaoAuthenticationProvider中的UserDetailService
        builder.userDetailsService(myUserDetailService);
    }

    //用来将自定义的AuthenticationManager在工厂中进行暴露，之后可以在任何位置注入
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
