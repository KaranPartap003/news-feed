package com.news;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) {
        SpringApplication.run(Producer.class, args);
    }

    //rate limit = 200/day
    @Scheduled(fixedRate = 432000)  // approx 200 calls per day
    public void fetchFromNewsDataIO() throws JsonProcessingException {
        try {
            producer.sendToRedis();
        } catch (JsonProcessingException e) {
            LOGGER.info(e.toString());
        }
    }

    //rate limit = 100/day
    @Scheduled(fixedRate = 864000) //approx 100 calls per day
    public void fetchGuardian() throws JsonProcessingException {
        try {
            producer.sendToSql();
        } catch (JsonProcessingException e) {
            LOGGER.info(e.toString());
        }
    }
}