package com.news.Repository;

import com.news.Model.PgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PgRepository extends JpaRepository<PgEntity, Long> {

}
