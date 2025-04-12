package com.rayanmitra.journalApp.repository;

import com.rayanmitra.journalApp.repository.UserRepositoryImpl;
import org.bson.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    private UserRepositoryImpl userRepository; // Spring will inject this bean

    @Test
    public void testSaveNewUser() {
        Assertions.assertNotNull(userRepository);
    }
}
