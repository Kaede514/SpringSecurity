package com.kaede.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaede
 * @create 2022-09-20
 */

//@CrossOrigin(origins = {"http://localhost:63342"})    //允许跨域访问
//@CrossOrigin    //默认允许所有域访问
@RestController
public class DemoController {

    @GetMapping("/demo")
    public String demo() {
        return "demo ok";
    }

}
