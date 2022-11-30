package com.kaede.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @author kaede
 * @create 2022-09-20
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //全局地解决跨域问题
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")   //对指定请求进行跨域处理
            .allowedMethods("*")    //方法
            .allowedOrigins("*")    //来源
            .allowedHeaders("*")    //请求头
            .allowCredentials(false)    //是否需要请求凭证
            .exposedHeaders("") //暴露指定响应头字段
            .maxAge(3600);      //域检的有效期（有效期内不必再次发送域检请求，默认是1800s）
    }*/

    //和上面效果一样
    /*@Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        registrationBean.setFilter(new CorsFilter(source));
        registrationBean.setOrder(-1);
        return registrationBean;
    }*/

}
