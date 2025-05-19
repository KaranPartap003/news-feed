package com.news.Model.ResponseClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.news.Model.GuardianArticle;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuardianResponseData {

    @JsonProperty("results")
    private List<GuardianArticle> results;
    public List<GuardianArticle> getArticles() {
        return results;
    }

    public GuardianResponseData() {
    }

    @Override
    public String toString() {
        return "GuardianResponseData{" +
                "results=" + results +
                '}';
    }

    public GuardianResponseData(List<GuardianArticle> results) {
        this.results = results;
    }

    public void setArticles(List<GuardianArticle> articles) {
        this.results = articles;
    }
}
