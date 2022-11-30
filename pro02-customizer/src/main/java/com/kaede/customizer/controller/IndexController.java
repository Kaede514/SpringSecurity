package com.kaede.customizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaede
 * @create 2022-09-17
 */

@RestController
public class IndexController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping({"/index","/"})
    public String index() {
        return "hello, index";
    }

}
