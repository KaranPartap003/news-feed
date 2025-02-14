package com.news.Configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-server}")
    private String server;

    @Bean
    public NewTopic redisTopic(){
        return TopicBuilder.name("redis-data").build();
    }

    @Bean
    public NewTopic sqlTopic(){
        return TopicBuilder.name("sql-data").build();
    }

}
