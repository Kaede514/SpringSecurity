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
        //pwd = $2a$10$5QGvOQHqA6umZeLSinJ6reiyczy9d1M8jWgfjjN69oEKSqvnHxRuG
        System.out.println("pwd = " + pwd);
    }

}
