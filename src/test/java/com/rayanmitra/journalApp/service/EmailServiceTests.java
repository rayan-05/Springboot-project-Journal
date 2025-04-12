package com.rayanmitra.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendMail() {
        emailService.sendMail("ayishikdhar@gmail.com","stoic quotes by the godfather-springboot testing","dating a fat woman in gym is like investing in bitcoin in 2010");

    }

}
