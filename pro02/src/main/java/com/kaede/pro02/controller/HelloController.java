package com.kaede.pro02.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaede
 * @create 2022-09-17
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        //1.获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println("身份信息：" + user);
        System.out.println("权限信息：" + authentication.getAuthorities());
        //默认的实现不允许子线程获取
        //配置VMOptions，-Dspring.security.strategy=MODE_INHERITABLETHREADLOCAL，启动该策略，此时允许子线程获取
        new Thread(() -> {
            Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("子线程获取：" + authentication1);
        }).start();
        return "hello, spring security";
    }

}
