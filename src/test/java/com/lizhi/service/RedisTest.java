package com.lizhi.service;

import com.lizhi.model.domain.User;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 增
        valueOperations.set("lizhiString", "dog");
        valueOperations.set("lizhiInt", 1);
        valueOperations.set("lizhiDouble", 2.0);
        User user = new User();
        user.setId(1L);
        user.setUsername("lizhi");
        valueOperations.set("lizhiUser", user);
        // 查
        Object lizhi = valueOperations.get("lizhiString");
        Assertions.assertTrue("dog".equals((String) lizhi));
        lizhi = valueOperations.get("lizhiInt");
        Assertions.assertTrue(1 == (Integer) lizhi);
        lizhi = valueOperations.get("lizhiDouble");
        Assertions.assertTrue(2.0 == (Double) lizhi);
        System.out.println(valueOperations.get("lizhiUser"));
        valueOperations.set("lizhiString", "dog");
        redisTemplate.delete("lizhiString");
    }
}
