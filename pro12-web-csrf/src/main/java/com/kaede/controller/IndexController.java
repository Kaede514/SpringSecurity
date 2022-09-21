package com.kaede.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaede
 * @create 2022-09-20
 */

@RestController
public class IndexController {

    @PostMapping("/")
    public String index() {
        return "index ok";
    }

    @GetMapping("/i")
    public String index2() {
        return "index2 ok";
    }

}
