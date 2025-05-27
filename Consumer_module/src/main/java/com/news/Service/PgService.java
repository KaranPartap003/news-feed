package com.news.Service;

import com.news.Model.GuardianArticle;
import com.news.Model.PgEntity;
import com.news.Repository.PgRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PgService {

    @Autowired
    private PgRepository repository;


    private static final Logger LOGGER = LoggerFactory.getLogger(PgService.class);
    private final List<PgEntity> buffer = new ArrayList<>();


    @KafkaListener(topics = "general_articles", groupId = "group1")
    public void addArticles(GuardianArticle article){
        PgEntity entity = new PgEntity(article.getWebTitle(), article.getWebUrl());
        buffer.add(entity);
        LOGGER.info(entity.toString());
        if(buffer.size() >= 10) {
            repository.saveAll(buffer);//bulk insert
            buffer.clear();
            LOGGER.info("data sent to postgres");
        }
    }

    public List<GuardianArticle> getPaginatedArticles(int pageNo, int pageSize){
        Pageable p = PageRequest.of(pageNo, pageSize);
        Page<PgEntity> page = repository.findAll(p);
        List<PgEntity> content= page.getContent();
        return content.stream()
                .map(entity -> {
                    return new GuardianArticle(entity.getTitle(), entity.getLink());
                })
                .toList();
    }

}
