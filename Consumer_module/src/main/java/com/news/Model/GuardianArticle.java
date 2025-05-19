package com.news.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuardianArticle {
    private String webTitle;

    private String webUrl;

    public GuardianArticle(String webTitle, String webUrl) {
        this.webTitle = webTitle;
        this.webUrl = webUrl;
    }

    public GuardianArticle() {
    }

    @Override
    public String toString() {
        return "GuardianArticle{" +
                "webTitle='" + webTitle + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
