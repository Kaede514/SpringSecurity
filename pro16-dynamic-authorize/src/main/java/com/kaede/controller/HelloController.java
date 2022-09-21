package com.kaede.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaede
 * @create 2022-09-21
 */

@RestController
public class HelloController {

    /**
     * /admin/**    ROLE_ADMIN
     * /user/**     ROLE_USER
     * /guest/**    ROLE_GUEST  ROLE_USER
     *
     * admin    ADMIN   USER
     * user     USER
     * blr      GUEST
     */

    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user";
    }

    @GetMapping("/guest/hello")
    public String guest() {
        return "hello guest";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}