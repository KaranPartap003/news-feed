package com.news.Service;

import com.news.Model.Article;
import com.news.Model.RedisRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RedisService {

    @Autowired
    private RedisVectorStore vectorStore;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisService.class);
    private final List<Article> articlesBuffer = new ArrayList<>();
    private final int BATCH_SIZE = 10;
    private final int RESPONSE_SIZE = 10;

    @KafkaListener(topics = "all", groupId = "group1")
    public void addArticles(Article article){
        LOGGER.info(String.format("article received : %s", article.toString()));
        articlesBuffer.add(article);
        if(articlesBuffer.size() >= BATCH_SIZE){
            processBatch(new ArrayList<>(articlesBuffer));
            LOGGER.info("data sent to redis server");
            articlesBuffer.clear();
        }
    }

    public List<Article> getRelevantArticles(RedisRequest request){
        if(request.getUserArticles() == null)
            return new ArrayList<>(articlesBuffer);
        List<String> UserArticles = request.getUserArticles();
        List<Document> results = new ArrayList<>();
        for(String query : UserArticles){
            results.addAll(
                    //returns a list<Document>
                    vectorStore.doSimilaritySearch(
                            SearchRequest.builder()
                                    .query(query)
                                    .build()
                    )
            );
        }
        List<Article> response = results.stream()
                .map(this::convertToRedisArticle)
                .collect(Collectors.toList());
        int remainingArticles = BATCH_SIZE - results.size();
        if(remainingArticles > 0){
            int articlesToAdd = Math.min(remainingArticles, articlesBuffer.size());
            response.addAll(articlesBuffer.subList(0, articlesToAdd));
        }
        return response;
    }

    //---------------Helper Functions-----------------//
    public void processBatch(List<Article> batch){
        // Convert articles to documents and send to Redis
        List<Document> documents = batch.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());
        vectorStore.doAdd(documents);
    }

    public Document convertToDocument(Article article){
        String content = article.getTitle() + article.getDescription();
        Map<String, Object> metadata = new HashMap<>();
        if(article.getLink() != null)
            metadata.put("link", article.getLink());
        if(article.getDescription() != null)
            metadata.put("description", article.getDescription());
        if(article.getTitle() != null)
            metadata.put("title", article.getTitle());
        return new Document(content, metadata);
    }

    public Article convertToRedisArticle(Document doc){
        String title = doc.getMetadata().getOrDefault("title", "Untitled").toString();
        String link = doc.getMetadata().getOrDefault("link", "No Link Available").toString();
        String description = doc.getMetadata().getOrDefault("description", "No Description Available").toString();

        return new Article(title, link, description);
    }

}
