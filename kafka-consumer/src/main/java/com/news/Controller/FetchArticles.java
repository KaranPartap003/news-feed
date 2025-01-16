package com.news.Controller;

import com.news.ConsumeArticles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news-feed")
public class FetchArticles {

    @Autowired
    private ConsumeArticles consumeArticles;
    @GetMapping("/fetchArticles")
    public String sendArticles(){
        return consumeArticles.sendArticles().toString();
    }
}
