package com.kaede.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
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

@Configuration
//指定当前应用为授权服务器
@EnableAuthorizationServer
public class InMemoryAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    //用来配置授权服务器可以为哪些客户端授权   id secret redirectURI 授权模式
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient("myClient")    //客户端
            .secret(passwordEncoder.encode("mySecret"))     //注册客户端密钥
            .redirectUris("https://www.baidu.com")  //重定向URL（客户端）
            //授权服务器支持的模式，此处支持授权码模式，还支持刷新令牌
            .authorizedGrantTypes("authorization_code","refresh_code")
            .scopes("read:user");   //令牌允许获取的资源权限
    }

    //配置授权服务器指定使用哪个UserDetailsService（刷新令牌所需配置）
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.userDetailsService(userDetailsService);
    }

    //授权码模式：
    // 1.请求用户是否授权 /oauth/authorize
    //   完整路径 http://localhost:8080/oauth/authorize?client_id=myClient&
    //           response_type=code&redirect_uri=https://www.baidu.com
    // 2.授权之后根据获取的授权码获取令牌 /oauth/token      id secret redirectURI 授权类型authorization_code
    // 3.支持令牌的刷新                 /oauth/token      id secret 刷新的令牌 授权类型refresh_token

}
