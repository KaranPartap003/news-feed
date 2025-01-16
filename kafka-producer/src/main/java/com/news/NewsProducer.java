package com.news;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.news.Model.Article;
import com.news.Model.ResponseClasses.NewsdataIOResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class NewsProducer {

    @Value("${newsdata.api.key}")
    private String apiKey;

    @Autowired
    KafkaTemplate<String, Article> kafkaTemplate;
    Set<String> processedArticles = new HashSet<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsProducer.class);
    RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String nextPage = null;

    public void sendMessage() throws JsonProcessingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://newsdata.io/api/1/latest?&language=en")
                                    .queryParam("apikey", apiKey);
        if(nextPage != null)
            builder.queryParam("page", nextPage);
        String URL = builder.toUriString();
        try{
            //fetch Json as a string
            String jsonResponse = restTemplate.getForObject(URL, String.class);
            //deserialize json string into NewsIOdata
            NewsdataIOResponse response = objectMapper.readValue(jsonResponse, NewsdataIOResponse.class);
            if(response != null && response.getResults() != null){
                List<Article> results = response.getResults();
                for(Article article: results){
                    String articleTitle = article.getTitle().substring(0,10);
                    if (!processedArticles.contains(articleTitle)) {
                        kafkaTemplate.send("all", article);
                        LOGGER.info(article.toString());
                        // Add article to the set to avoid duplicates
                        processedArticles.add(articleTitle.substring(0,10));
                    }
                }
            }
            processedArticles.clear();
            if (response.getNextPage() != null)
                this.nextPage = response.getNextPage();
            else
                LOGGER.info("next Page not found");
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }
}
