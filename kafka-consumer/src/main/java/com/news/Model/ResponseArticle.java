package com.news.Model;

public class ResponseArticle extends Article{
    private boolean isRecommended;

    public ResponseArticle(String title, String description, String link, boolean isRecommended){
        super(title, description, link);
        this.isRecommended = isRecommended;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    @Override
    public String toString() {
        return "ResponseArticle{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", link='" + getLink() + '\'' +
                ", isRecommended=" + isRecommended +
                '}';
    }
}
