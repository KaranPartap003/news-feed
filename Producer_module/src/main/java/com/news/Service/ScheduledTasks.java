package com.news.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.news.NewsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasks {

    @Autowired
    private NewsProducer producer;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    //rate limit = 200/day
    @Scheduled(fixedRate = 2000)  // (43200)approx 200 calls per day
    public void fetchFromNewsDataIO() throws JsonProcessingException {
        try {
            producer.sendToRedis();
        } catch (JsonProcessingException e) {
            LOGGER.info(e.toString());
        }
    }

    //rate limit = 100/day
    @Scheduled(fixedRate = 2000) // (864000)approx 100 calls per day
    public void fetchGuardian() throws JsonProcessingException {
        try {
            producer.sendToSql();
        } catch (JsonProcessingException e) {
            LOGGER.info(e.toString());
        }
    }
}
