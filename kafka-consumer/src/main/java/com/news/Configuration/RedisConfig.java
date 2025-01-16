package com.news.Configuration;


import com.news.Model.RedisArticle;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import redis.clients.jedis.JedisPooled;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.user}")
    private String username;

    @Value("${redis.password}")
    private String password;

    @Value("${spring.kafka.consumer.bootstrap-server}")
    private String server;

    @Bean
    public JedisPooled jedisPooled(){
        return new JedisPooled(host, port, username, password);
    }

    @Bean
    public EmbeddingModel embeddingModel(){
        return OllamaEmbeddingModel
                .builder()
                .defaultOptions(
                        OllamaOptions.builder()
                                .model(OllamaModel.MXBAI_EMBED_LARGE)
                                .build()
                )
                .ollamaApi(new OllamaApi())
                .build();
    }

    @Bean
    public RedisVectorStore vectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel){
        return RedisVectorStore
                .builder(jedisPooled, embeddingModel)
                .initializeSchema(true)
                .vectorAlgorithm(RedisVectorStore.Algorithm.FLAT)
                .build();
    }

    @Bean
    public ConsumerFactory<String, RedisArticle> consumerFactory(){
        Map<String, Object> prop = new HashMap<>();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(prop);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RedisArticle> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RedisArticle> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true); // Enable batch listening
        return factory;
    }
}
