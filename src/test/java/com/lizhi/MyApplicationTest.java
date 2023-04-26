package com.lizhi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class MyApplicationTest {

    @Test
    void testDigest() throws NoSuchAlgorithmException {
        String newPassword = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        System.out.println(newPassword);
    }

    @Test
    void contextLoads() {

    }


    @Test
    void test() throws IOException, InterruptedException {

    }
}
