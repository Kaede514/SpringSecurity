package com.kaede.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaede
 * @create 2022-09-18
 */

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test ok";
    }

}
