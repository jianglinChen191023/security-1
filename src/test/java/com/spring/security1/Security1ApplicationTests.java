package com.spring.security1;

import com.spring.security1.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class Security1ApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
//        User user = userMapper.getUserByUsername("123");
//        System.out.println(user.getId());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123123");
        System.out.println(encode);
    }              

}
