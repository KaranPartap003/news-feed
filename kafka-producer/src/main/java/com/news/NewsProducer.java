package com.news;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.news.Model.Article;
import com.news.Model.GuardianArticle;
import com.news.Model.ResponseClasses.GuardianResponse;
import com.news.Model.ResponseClasses.GuardianResponseData;
import com.news.Model.ResponseClasses.NewsdataIOResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.List;


@Service
public class NewsProducer {

    @Value("${newsdata.api-key}")
    private String newsdataApiKey;

    @Value("${guardian.api-key}")
    private String guardianApiKey;

    @Autowired
    KafkaTemplate<String, Article> redisArticlekafkaTemplate;

    @Autowired
    KafkaTemplate<String, GuardianArticle> guardianArticleKafkaTemplate;

    HashSet<String> processedArticles = new HashSet<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsProducer.class);
    RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String nextPage = null;

    private int currGuardianPage = 1;

    //convert to webclient
    public void sendToRedis() throws JsonProcessingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://newsdata.io/api/1/latest?&language=en")
                                    .queryParam("apikey", newsdataApiKey);
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
                    String articleTitle = article.getTitle().substring(0,Math.min(20, article.getTitle().length()));
                    if (!processedArticles.contains(articleTitle)) {
                        if(article.getDescription() != null) {
                            article.setDescription(article
                                    .getDescription()
                                    .substring(0, Math.min(120, article.getDescription().length()))
                                    + "...");
                        }
                        redisArticlekafkaTemplate.send("redis-data", article);
                        LOGGER.info(String.format("Data sent: %s", article.toString()));
                        // Add article to the set to avoid duplicates
                        processedArticles.add(articleTitle.substring(0,Math.min(20, article.getTitle().length())));
                    }
                }
            }
            processedArticles.clear();
            if (response != null && response.getNextPage() != null)
                this.nextPage = response.getNextPage();
            else
                LOGGER.info("next Page not found");
        }catch (RestClientException | JsonProcessingException e){
            LOGGER.info(e.toString());
        }
    }

    //convert to webclient
    public void sendToSql() throws JsonProcessingException{
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://content.guardianapis.com/search")
                .queryParam("api-key", guardianApiKey)
                .queryParam("page", currGuardianPage++);
        String url = builder.toUriString();
        LOGGER.info(url);
        try{
            String response = restTemplate.getForObject(url, String.class);
            GuardianResponse guardianResponse = objectMapper.readValue(response, GuardianResponse.class);
            GuardianResponseData guardianResponseData = guardianResponse.getResponseData();
            if(guardianResponseData != null && guardianResponseData.getArticles() != null){
                List<GuardianArticle> articles = guardianResponseData.getArticles();
                for(GuardianArticle article : articles){
                    guardianArticleKafkaTemplate.send("sql-data", article);
                    LOGGER.info(article.toString());
                }
            }
        }catch (JsonProcessingException | RestClientException e){
            LOGGER.info(e.toString());
        }
    }

}
