package com.kaede;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Base64Utils;

@SpringBootTest
class Pro08RememberMeApplicationTests {

    @Test
    void contextLoads() {
        byte[] arr = Base64Utils.decodeFromString("cm9vdDoxNjY0NzkwMzA3MzU1OjdlZWRiOTI3NWZmY2ZkM2U3");
        //root:1664790307355:7eedb9275ffcfd3e7
        System.out.println(new String(arr));
    }

}
