package com.marselgaisin.mediacms.content.article.mapper;

import org.springframework.stereotype.Component;

import com.marselgaisin.mediacms.content.article.dto.ArticleCreateRequest;
import com.marselgaisin.mediacms.content.article.dto.ArticleResponse;
import com.marselgaisin.mediacms.content.article.dto.ArticleUpdateRequest;
import com.marselgaisin.mediacms.content.article.model.Article;

@Component
public class ArticleMapper {

    public Article toEntity(ArticleCreateRequest request) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setText(request.getText());
        article.setAuthor(request.getAuthor());
        article.setPublishedAt(request.getPublishedAt());
        return article;
    }

    public void updateEntity(Article article, ArticleUpdateRequest request) {
        article.setTitle(request.getTitle());
        article.setText(request.getText());
        article.setAuthor(request.getAuthor());
        if (request.getPublishedAt() != null) {
            article.setPublishedAt(request.getPublishedAt());
        }
    }

    public ArticleResponse toResponse(Article article) {
        ArticleResponse response = new ArticleResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setText(article.getText());
        response.setAuthor(article.getAuthor());
        response.setPublishedAt(article.getPublishedAt());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());
        return response;
    }
}
