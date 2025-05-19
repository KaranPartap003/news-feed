package com.news.Controller;

import com.news.Model.Article;
import com.news.Model.GuardianArticle;
import com.news.Model.ResponseArticle;
import com.news.Model.UserRequest;
import com.news.Service.PgService;
import com.news.Service.RedisService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/news-feed")
public class FetchArticles {

    @Autowired
    private RedisService redisService;

    @Autowired
    private PgService pgService;

    //testing getRelevantArticles
    @PostMapping("/fetchArticles")
    public ResponseEntity<List<Article>> fetch(@RequestBody UserRequest request){
        return ResponseEntity.ok(redisService.getRelevantArticles(request));
    }

    //testing similaritySearch function
    @PostMapping("/fetch")
    public ResponseEntity<List<String>> fetch(@RequestBody String request){
        List<Document> temp = redisService.similaritySearch(request);
        List<String> res = new ArrayList<>();
        for (Document doc : temp)
            res.add(doc.getFormattedContent());
        return ResponseEntity.ok(res);
    }

    //testing pagination from postgres
    @GetMapping("/fetchFromSql")
    public ResponseEntity<List<GuardianArticle>> fetch(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize){
        List<GuardianArticle> articles = pgService.getPaginatedArticles(pageNo, pageSize);
        return ResponseEntity.ok(articles);
    }

    //returns combined results of both, paginated articles and similar articles
    @PostMapping("/getUserArticles")
    public List<ResponseArticle> fetch(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                               @RequestBody(required = false) UserRequest request){
        List<GuardianArticle> guardianArticles = pgService.getPaginatedArticles(pageNo, pageSize);
        List<ResponseArticle> responseArticles = guardianArticles
                .stream()
                .map(article -> {
                    return new ResponseArticle(article.getWebTitle()
                            , "description not available"
                            , article.getWebUrl()
                            , false);
                })
                .collect(Collectors.toList());

        if(request != null) {
            List<Article> redisArticles = redisService.getRelevantArticles(request);
            responseArticles.addAll(redisArticles
                    .stream()
                    .map(article -> {
                        return new ResponseArticle(article.getTitle()
                                , article.getDescription()
                                , article.getLink(), true);}
                    ).toList());
        }

        Collections.shuffle(responseArticles);

        return responseArticles;
    }
}
