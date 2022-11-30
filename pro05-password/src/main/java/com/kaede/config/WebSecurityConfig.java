package com.kaede.config;

import com.kaede.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author kaede
 * @create 2022-09-18
 */

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //使用PasswordEncoder第二种方式（固定整个系统的加密方式为某种加密）
    //使用之后，无需再添加且不能添加{bcrypt}前缀，直接写密码即可
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    //自定义内存的数据源
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").
            //noop表示明文（即不加密），推荐使用bcrypt加密方式，使用PasswordEncoder第一种方式（该方式比较灵活，推荐使用）
            password("{bcrypt}$2a$10$BLy4re4bHR3yAz6PrTJlCe.61wYqNfqi6AVfPhnKkbN7cp8htzoDC").roles("admin").build());
            //password("$2a$10$5QGvOQHqA6umZeLSinJ6reiyczy9d1M8jWgfjjN69oEKSqvnHxRuG").roles("admin").build());
        return inMemoryUserDetailsManager;
    }

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //builder.userDetailsService(userDetailsService());
        builder.userDetailsService(myUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()   //所有请求必须认证
                .and().formLogin()
                .and().logout()
                .logoutUrl("/logout")
                .and().csrf().disable();
    }

}
