package com.kaede.pro01.contoller;

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
        return "hello, spring security";
    }

}
