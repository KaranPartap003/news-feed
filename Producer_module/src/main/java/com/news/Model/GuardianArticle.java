package com.news.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuardianArticle {

    @JsonProperty("webTitle")
    private String title;

    @JsonProperty("webUrl")
    private String link;

    public GuardianArticle(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public GuardianArticle() {
    }

    @Override
    public String toString() {
        return "GuardianArticle{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getlink() {
        return link;
    }

    public void setlink(String link) {
        this.link = link;
    }
}
