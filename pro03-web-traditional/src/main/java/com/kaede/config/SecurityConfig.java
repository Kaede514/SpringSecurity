package com.kaede.config;

import com.kaede.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author kaede
 * @create 2022-09-18
 *
 * 自定义SpringSecurity相关配置
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers("/login").permitAll()
            .anyRequest().authenticated()
            .and().formLogin()
            .loginPage("/login")   //指定自定义的登录界面
            .loginProcessingUrl("/doLogin")
            .usernameParameter("uname")
            .passwordParameter("pwd")
            .defaultSuccessUrl("/index",true)
            .failureUrl("/login")
            .and().logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .and().csrf().disable();
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }*/

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService());
        auth.userDetailsService(myUserDetailService);
    }

}
