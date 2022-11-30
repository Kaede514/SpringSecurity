package com.kaede.security.config;

import com.kaede.security.metasource.CustomSecurityMetaSource;
import com.kaede.service.MyUseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author kaede
 * @create 2022-09-21
 */

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUseDetailsService useDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(useDetailsService);
    }

    private final CustomSecurityMetaSource customSecurityMetadataSource;

    @Autowired
    public SecurityConfig(CustomSecurityMetaSource customSecurityMetadataSource) {
        this.customSecurityMetadataSource = customSecurityMetadataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //1.获取工厂对象
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        //2.设置自定义的url的权限处理
        http.apply(new UrlAuthorizationConfigurer<>(applicationContext))
            .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                @Override
                public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                    object.setSecurityMetadataSource(customSecurityMetadataSource);
                    //是否拒绝公共资源（即未设置权限的资源）的访问
                    object.setRejectPublicInvocations(false);
                    return object;
                }
            });
        http.formLogin().and().csrf().disable();
    }

}
