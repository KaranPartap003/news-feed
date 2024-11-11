package com.news;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class Producer{

    @Autowired
    private NewsProducer producer;
    public static void main(String[] args) {
        SpringApplication.run(Producer.class, args);
    }

    //rate limit = 200/day
    @Scheduled(fixedRate = 2000)
    public void fetchFromNewsDataIO() throws JsonProcessingException {
        try {
            producer.sendMessage();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}