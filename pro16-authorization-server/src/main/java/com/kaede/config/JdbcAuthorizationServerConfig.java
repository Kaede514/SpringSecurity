package com.kaede.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * @author kaede
 * @create 2022-09-21
 *
 * 自定义授权服务器的配置，基于JDBC的实现
 */

@Configuration
@EnableAuthorizationServer
public class JdbcAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final DataSource dataSource;

    @Autowired
    public JdbcAuthorizationServerConfig(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, DataSource dataSource) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    //声明ClientDetailsService实现
    @Bean
    public ClientDetailsService clientDetails() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //使用jdbc存储
        clients.withClientDetails(clientDetails());
    }

    //声明TokenStore实现
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    //配置使用数据库实现
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //指定认证管理器
        endpoints.authenticationManager(authenticationManager);
        //配置令牌存储为数据库存储
        endpoints.tokenStore(tokenStore());

        //配置TokenServices参数
        //修改默认令牌生成服务
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //基于数据库令牌生成
        tokenServices.setTokenStore(endpoints.getTokenStore());
        //是否支持刷新令牌
        tokenServices.setSupportRefreshToken(true);
        //是否允许重复刷新令牌（直到过期，若为false，则令牌过期之前始终为同一个令牌）
        tokenServices.setReuseRefreshToken(true);

        //设置客户端信息
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        //用来控制令牌存储增强策略
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        //访问令牌的默认有效期（默认以秒为单位），过期的令牌为零或负数
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); //30天
        //刷新令牌的有效期（默认以秒为单位），如果小于或等于零，则令牌将不会过期
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(3)); //3天
        //使用配置令牌服务
        endpoints.tokenServices(tokenServices);
    }

}
