package com.kaede.customizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author kaede
 * @create 2022-09-17
 */

@Controller
public class LoginController {

    @GetMapping("/toLogin")
    public String login() {
        return "login";
    }

}
