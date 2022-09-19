package com.kaede.config;

import com.kaede.security.filter.KaptchaFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author kaede
 * @create 2022-09-18
 *
 * 自定义SpringSecurity相关配置
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //用来将自定义的AuthenticationManager在工厂中进行暴露，之后可以在任何位置注入
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public KaptchaFilter kaptchaFilter() throws Exception {
        KaptchaFilter kaptchaFilter = new KaptchaFilter();
        kaptchaFilter.setFilterProcessesUrl("/doLogin");
        kaptchaFilter.setUsernameParameter("uname");
        kaptchaFilter.setPasswordParameter("pwd");
        kaptchaFilter.setKaptchaParameter("kaptcha");
        //指定认证管理器
        kaptchaFilter.setAuthenticationManager(authenticationManagerBean());
        //指定认证成功的处理
        kaptchaFilter.setAuthenticationSuccessHandler((req,resp,authentication) -> {
            resp.sendRedirect("/index");
        });
        //指定认证失败的处理
        kaptchaFilter.setAuthenticationFailureHandler((req,resp,ex) -> {
            resp.sendRedirect("/login");
        });
        return kaptchaFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers("/login","/vc.jpg").permitAll()
            .anyRequest().authenticated()
            .and().formLogin()
            .loginPage("/login")   //指定自定义的登录界面
            //因为自定义了Filter替换UsernamePasswordAuthenticationFilter，故Filter中的配置会覆盖如下
            //UsernamePasswordAuthenticationFilter中的配置
            /*.loginProcessingUrl("/doLogin")
            .usernameParameter("uname")
            .passwordParameter("pwd")
            .defaultSuccessUrl("/index",true)
            .failureUrl("/login")*/
            .and().logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .and().csrf().disable();
        http.addFilterAt(kaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }

    /*@Autowired
    private MyUserDetailService myUserDetailService;*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
        //auth.userDetailsService(myUserDetailService);
    }

}
