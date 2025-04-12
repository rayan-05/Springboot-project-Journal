package com.rayanmitra.journalApp.scheduler;


import com.rayanmitra.journalApp.cache.AppCache;
import com.rayanmitra.journalApp.entity.JournalEntry;
import com.rayanmitra.journalApp.entity.User;
import com.rayanmitra.journalApp.repository.UserRepository;
import com.rayanmitra.journalApp.repository.UserRepositoryImpl;
import com.rayanmitra.journalApp.service.EmailService;
import com.rayanmitra.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 ? * SUN")
    public void fetchUsersAndSendSaMail(){
        List<User> users = userRepository.getUserForSA();
        for(User user : users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<String> filteredEntries = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x->x.getContent()).collect(Collectors.toList());
            String entry=String.join(" ", filteredEntries);
            String sentiment= sentimentAnalysisService.getSentiment(entry);
            emailService.sendMail(user.getEmail(), "Sentiment for last 7 days", sentiment);
        }


    }

    @Scheduled(cron = "0 0/5 0 ? * *")
    public void clearAppCache() {

        appCache.init();

    }
}
