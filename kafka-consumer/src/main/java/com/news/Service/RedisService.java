package com.news.Service;

import com.news.Model.RedisArticle;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RedisService {

    @Autowired
    private RedisVectorStore vectorStore;

    private final List<RedisArticle> articlesBuffer = new ArrayList<>();
    private final int BATCH_SIZE = 10;

    @KafkaListener(topics = "all", groupId = "group1")
    public void addArticles(RedisArticle article){
        articlesBuffer.add(article);
        if(articlesBuffer.size() >= BATCH_SIZE){
            processBatch(new ArrayList<>(articlesBuffer));
            articlesBuffer.clear();
        }
    }

    public void processBatch(List<RedisArticle> batch){
        // Convert articles to documents and send to Redis
        List<Document> documents = batch.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());
        vectorStore.add(documents);
    }

    public Document convertToDocument(RedisArticle article){
        String content = article.getTitle() + article.getDescription();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("link", article.getLink());
        metadata.put("description", article.getDescription());
        metadata.put("title", article.getTitle());
        return new Document(content, metadata);
    }

}
