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
        String secret = passwordEncoder.encode("mySecret");
        //pwd =
        System.out.println("secret = " + secret);
    }

}
