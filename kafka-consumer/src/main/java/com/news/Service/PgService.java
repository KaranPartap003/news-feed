package com.news.Service;

import com.news.Model.GuardianArticle;
import com.news.Model.PgEntity;
import com.news.Repository.PgRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PgService {

    @Autowired
    private PgRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PgService.class);

    @KafkaListener(topics = "sql-data", groupId = "group1")
    public void addArticles(GuardianArticle article){
        PgEntity entity = new PgEntity();
        LOGGER.info(STR."article received : \{article.toString()}");
        entity.setTitle(article.getWebTitle());
        entity.setLink(article.getWebUrl());
        LOGGER.info(entity.toString());
        repository.save(entity);
    }

    public List<GuardianArticle> getPaginatedArticles(int pageNo, int pageSize){
        Pageable p = PageRequest.of(pageNo, pageSize);
        Page<PgEntity> page = repository.findAll(p);
        List<PgEntity> content= page.getContent();
        return content.stream()
                .map(entity -> {return new GuardianArticle(entity.getTitle(), entity.getLink());})
                .toList();
    }

}
