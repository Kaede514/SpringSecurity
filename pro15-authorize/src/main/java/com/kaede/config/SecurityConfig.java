package com.kaede.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author kaede
 * @create 2022-09-20
 */

//开启全局的方法注解的支持（一般开启prePostEnabled就足够了）
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //内存数据源
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").
            password("{noop}123").roles("ADMIN","USER").build());
        inMemoryUserDetailsManager.createUser(User.withUsername("user").
            password("{noop}123").roles("USER").build());
        inMemoryUserDetailsManager.createUser(User.withUsername("win7").
            password("{noop}123").authorities("READ_INFO").build());
        return inMemoryUserDetailsManager;
    }

    //指定全局AuthenticationManager使用内存数据源
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            //数据库中查出来的角色会自动加上ROLE_前缀，角色底层也会自动加ROLE_前缀
            .mvcMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN") //具有ADMIN角色
            .mvcMatchers("/user").hasRole("USER")   //具有USER角色
            .mvcMatchers("/getInfo").hasAuthority("READ_INFO") //具有READ_INFO权限
            .mvcMatchers("/").hasAnyRole("ADMIN", "USER")
            //.regexMatchers().hasRole()    //支持正则表达式
            .anyRequest().authenticated()
            .and().formLogin()
            .and().csrf().disable();
    }

}
