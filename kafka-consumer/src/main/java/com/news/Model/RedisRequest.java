package com.news.Model;


import java.util.List;

public class RedisRequest {
    private List<String> userArticles;

    public RedisRequest(List<String> userArticles) { this.userArticles = userArticles;}

    public RedisRequest() {}

    public List<String> getUserArticles() { return userArticles; }

    public void setUserArticles(List<String> userArticles) { this.userArticles = userArticles; }
}
