package com.news;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ConsumeArticles {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumeArticles.class);
    private static final int CACHE_SIZE = 50;
    private final ConcurrentLinkedQueue<String> cache = new ConcurrentLinkedQueue<>();

    @KafkaListener(topics = "all", groupId = "group1")
    public void receiveArticles(String message){
        LOGGER.info(message);
        if(cache.size() > CACHE_SIZE)
            cache.poll();
        cache.offer(message);
    }

    public ConcurrentLinkedQueue<String> sendArticles() {
        return new ConcurrentLinkedQueue<>(cache);
    }
}
