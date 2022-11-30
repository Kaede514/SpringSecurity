package com.kaede.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaede
 * @create 2022-11-30
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello!";
    }

}
