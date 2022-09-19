package com.kaede.pro02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author kaede
 * @create 2022-09-18
 */

@Controller
public class LogoutController {

    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }

}
