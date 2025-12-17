package com.marselgaisin.mediacms.content.article.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marselgaisin.mediacms.content.article.model.Article;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
}
