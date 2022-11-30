package com.kaede.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().formLogin()
            .and().cors()   //开启放行跨域请求
            //对放行跨域请求进行配置
            .configurationSource(configurationSource())
            .and().csrf().disable();
    }

    @Bean
    CorsConfigurationSource configurationSource() {
       CorsConfiguration corsConfiguration = new CorsConfiguration();
       corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
       corsConfiguration.setAllowedMethods(Arrays.asList("*"));
       corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
       corsConfiguration.setMaxAge(3600L);
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", corsConfiguration);
       return source;
    }

}
