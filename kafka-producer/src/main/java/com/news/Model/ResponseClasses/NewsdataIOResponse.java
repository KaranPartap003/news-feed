package com.news.Model.ResponseClasses;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.news.Model.Article;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsdataIOResponse {
    private int totalResults;
    private List<Article> results;
    private String nextPage;

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getResults() {
        return results;
    }

    public void setResults(List<Article> results) {
        this.results = results;
    }
}
