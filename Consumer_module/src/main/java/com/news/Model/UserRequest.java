package com.news.Model;


import java.util.List;

public class UserRequest {
    private List<String> userArticles;

    public UserRequest(List<String> userArticles) { this.userArticles = userArticles;}

    public UserRequest() {}

    public List<String> getUserArticles() { return userArticles; }

    public void setUserArticles(List<String> userArticles) { this.userArticles = userArticles; }
}
