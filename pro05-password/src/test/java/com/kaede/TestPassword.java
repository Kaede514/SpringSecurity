package com.kaede;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author kaede
 * @create 2022-09-19
 */

public class TestPassword {

    @Test
    public void test() {
        //加密，推荐使用BCrypt方式
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pwd = passwordEncoder.encode("123");
        String pwd1 = passwordEncoder.encode("123");
        System.out.println("pwd = " + pwd);
        System.out.println("pwd1 = " + pwd1);
    }

}
