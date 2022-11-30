package com.kaede.customizer.config;

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
 * @create 2022-11-21
 */

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers("/","/index","/toLogin").permitAll()
            .anyRequest().authenticated()
            .and().formLogin()
            .loginPage("/toLogin")
            .loginProcessingUrl("/doLogin")
            .usernameParameter("uname")
            .passwordParameter("pwd")
            .successHandler(new MyAuthenticationSuccessHandler())
            .failureHandler(new MyAuthenticationFailureHandler())
            .and().logout()/*.logoutUrl("/logout")*/
            .logoutRequestMatcher(new OrRequestMatcher(
                new AntPathRequestMatcher("/aa","GET"),
                new AntPathRequestMatcher("/bb","POST")
            ))
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutSuccessHandler(new MyLogoutSuccessHandler())
            .and().csrf().disable();
    }

    /*@Autowired  //注入AuthenticationManagerBuilder
    public void initialize(AuthenticationManagerBuilder builder) throws Exception {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        //默认情况下用配置文件中的配置，这里对全局进行再次覆盖，用这里指定的信息
        userDetailsService.createUser(User.withUsername("kaede")
            .password("{noop}123").roles("admin").build());
        builder.userDetailsService(userDetailsService);
    }*/

    /*@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        userDetailsService.createUser(User.withUsername("kaede")
            .password("{noop}123").roles("admin").build());
        return userDetailsService;
    }*/

    @Autowired
    private MyUserDetailService myUserDetailService;

    //自定义AuthenticationManager（会覆盖默认的）（推荐使用）
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //更改的是DaoAuthenticationProvider中的UserDetailService
        //builder.userDetailsService(userDetailsService());
        builder.userDetailsService(myUserDetailService);
    }

    //用来将自定义的AuthenticationManager在工厂中进行暴露，之后可以在任何位置注入
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
