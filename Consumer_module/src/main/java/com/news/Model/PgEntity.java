package com.news.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "news_articles")
public class PgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private String link;

    public PgEntity() {
    }

    public PgEntity(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "PgEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
