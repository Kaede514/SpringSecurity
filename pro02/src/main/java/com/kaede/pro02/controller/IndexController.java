package com.kaede.pro02.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaede
 * @create 2022-09-17
 */

@RestController
public class IndexController {

    @GetMapping({"/index","/"})
    public String index() {
        return "hello, index";
    }

}
