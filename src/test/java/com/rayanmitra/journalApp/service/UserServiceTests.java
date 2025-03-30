package com.rayanmitra.journalApp.service;


import com.rayanmitra.journalApp.entity.User;
import com.rayanmitra.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @CsvSource({
            "ram",
            "vipul",
            "shyam",

    })
    public void testFindByUserName(String name){

        User user = userRepository.findByUserName(name);
        assertNotNull(user, "failed for " + name);

    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "10,1,11",
            "3,3,9"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a+b);
    }

}
