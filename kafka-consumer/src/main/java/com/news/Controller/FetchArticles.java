package com.news.Controller;

import com.news.Model.Article;
import com.news.Model.RedisRequest;
import com.news.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news-feed")
public class FetchArticles {

    @Autowired
    private RedisService redisService;

    @PostMapping("/fetchArticles")
    public ResponseEntity<List<Article>> fetch(@RequestBody RedisRequest request){
        return ResponseEntity.ok(redisService.getRelevantArticles(request));
    }
}
