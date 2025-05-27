package com.news.Configuration;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

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

    @Value("${redis.index_name}")
    private String index_name;

    @Bean
    public JedisPooled jedisPooled(){
        return new JedisPooled(host, port, username, password);
    }

    @Bean
    public EmbeddingModel embedModel(){
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
    public RedisVectorStore vectorStore(JedisPooled jedisPooled, EmbeddingModel embedModel){
        return RedisVectorStore
                .builder(jedisPooled, embedModel)
                .indexName(index_name)
                .initializeSchema(true)
                .metadataFields(
                        RedisVectorStore.MetadataField.text("title"),
                        RedisVectorStore.MetadataField.text("link"),
                        RedisVectorStore.MetadataField.text("description")
                )
                .vectorAlgorithm(RedisVectorStore.Algorithm.FLAT)
                .build();
    }
}
