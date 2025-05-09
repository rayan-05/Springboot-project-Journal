package com.rayanmitra.journalApp.scheduler;


import com.rayanmitra.journalApp.cache.AppCache;
import com.rayanmitra.journalApp.entity.JournalEntry;
import com.rayanmitra.journalApp.entity.User;
import com.rayanmitra.journalApp.enums.Sentiment;
import com.rayanmitra.journalApp.repository.UserRepository;
import com.rayanmitra.journalApp.repository.UserRepositoryImpl;
import com.rayanmitra.journalApp.service.EmailService;
import com.rayanmitra.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;


//    @Scheduled(cron = "0 0 9 ? * SUN")
    public void fetchUsersAndSendSaMail(){
        List<User> users = userRepository.getUserForSA();
        for(User user : users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentCounts=new HashMap<>();
            for(Sentiment sentiment : sentiments){

                if(sentiment!=null){
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
                }
            }
            Sentiment mostFrequentSentiment=null;
            int maxcount=0;
            for(Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()){
                if(entry.getValue()>maxcount){
                    maxcount=entry.getValue();
                    mostFrequentSentiment=entry.getKey();
                }
            }
            if(mostFrequentSentiment!=null){
                emailService.sendMail(user.getEmail(),"Sentiment for last 7 days",mostFrequentSentiment.toString());
            }


        }


    }

    @Scheduled(cron = "0 0/5 0 ? * *")
    public void clearAppCache() {

        appCache.init();

    }
}
