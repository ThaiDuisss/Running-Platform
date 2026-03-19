package com.running_platform.repository;

import com.running_platform.entity.Post.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Articles, Long> {
}
