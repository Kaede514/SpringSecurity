package com.kaede.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * @author kaede
 * @create 2022-09-21
 *
 * 自定义授权服务器的配置，基于内存的实现
 */

//@Configuration
//指定当前应用为授权服务器
//@EnableAuthorizationServer
public class InMemoryAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    //用来配置授权服务器可以为哪些客户端授权，需要id、secret、redirectURI、授权模式
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            //客户端
            .withClient("myClient")
            //注册客户端密钥（必须指定加密方式）
            .secret("{noop}mySecret")
            //重定向URL（客户端）
            .redirectUris("http://www.baidu.com")
            //授权服务器支持的模式，此处支持授权码模式，还支持刷新令牌
            .authorizedGrantTypes("authorization_code","refresh_token")
            //令牌允许获取的资源权限
            .scopes("read:user");
    }

    //配置授权服务器指定使用哪个UserDetailsService（刷新令牌所需配置）
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.userDetailsService(userDetailsService);
    }

}
