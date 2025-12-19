package com.marselgaisin.mediacms.content.article.service;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marselgaisin.mediacms.common.exception.NotFoundException;
import com.marselgaisin.mediacms.content.article.dto.ArticleCreateRequest;
import com.marselgaisin.mediacms.content.article.dto.ArticleResponse;
import com.marselgaisin.mediacms.content.article.dto.ArticleUpdateRequest;
import com.marselgaisin.mediacms.content.article.mapper.ArticleMapper;
import com.marselgaisin.mediacms.content.article.model.Article;
import com.marselgaisin.mediacms.content.article.repository.ArticleRepository;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleMapper.toEntity(request);
        Article saved = articleRepository.saveAndFlush(article);
        return articleMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "articles", key = "#id")
    public ArticleResponse getById(UUID id) {
        Article article = getEntity(id);
        return articleMapper.toResponse(article);
    }

    @Transactional
    @CacheEvict(cacheNames = "articles", key = "#id")
    public ArticleResponse update(UUID id, ArticleUpdateRequest request) {
        Article article = getEntity(id);
        articleMapper.updateEntity(article, request);
        Article saved = articleRepository.saveAndFlush(article);
        return articleMapper.toResponse(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "articles", key = "#id")
    public void delete(UUID id) {
        Article article = getEntity(id);
        articleRepository.delete(article);
    }

    private Article getEntity(UUID id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article not found"));
    }
}
