package com.news.Service;

import com.news.Model.Article;
import com.news.Model.UserRequest;
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
    private final HashSet<Article> articlesBuffer = new HashSet<>();
    private final int BATCH_SIZE = 10;


    @KafkaListener(topics = "recommended_articles", groupId = "group1")
    public void addArticles(Article article){
        LOGGER.info(String.format("article received : %s", article.toString()));
        articlesBuffer.add(article);
        if(articlesBuffer.size() >= BATCH_SIZE){
            processBatch(new ArrayList<>(articlesBuffer));
            LOGGER.info("data sent to redis server");
            articlesBuffer.clear();
        }
    }

    public List<Article> getRelevantArticles(UserRequest request){
        if(request.getUserArticles() == null)
            return new ArrayList<>(articlesBuffer);
        List<String> UserArticles = request.getUserArticles();
        HashSet<Document> results = new HashSet<>();
        for(String query : UserArticles){
            results.addAll(
                    //returns a list<Document>
                    vectorStore.doSimilaritySearch(
                            SearchRequest.builder()
                                    .topK(3)
                                    .query(query)
                                    .build()
                    )
            );
        }
        return results.stream()
                .map(this::convertToRedisArticle)
                .collect(Collectors.toList());
    }

    public List<Document> similaritySearch(String request){
        return vectorStore.doSimilaritySearch(
                SearchRequest
                        .builder()
                        .topK(4)
                        .query(request)
                        .build()
        );
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
        String content = article.getTitle();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", article.getTitle());
        metadata.put("description", (article.getDescription() != null)? article.getDescription() : "Description not available");
        metadata.put("link", (article.getLink() != null)? article.getLink() : "link not available");
        return new Document(content, metadata);
    }

    public Article convertToRedisArticle(Document doc){
        Map<String, Object> metadata = doc.getMetadata();
        String link = (String) metadata.get("link");
        String description = (String) metadata.get("description");
        String title = (String) metadata.get("title");
        return new Article(title, description, link);
    }

}
