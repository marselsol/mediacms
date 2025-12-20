package com.marselgaisin.mediacms.content.article.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marselgaisin.mediacms.common.exception.NotFoundException;
import com.marselgaisin.mediacms.content.article.dto.ArticleCreateRequest;
import com.marselgaisin.mediacms.content.article.dto.ArticleResponse;
import com.marselgaisin.mediacms.content.article.dto.ArticleUpdateRequest;
import com.marselgaisin.mediacms.content.article.mapper.ArticleMapper;
import com.marselgaisin.mediacms.content.article.model.Article;
import com.marselgaisin.mediacms.content.article.repository.ArticleRepository;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleService = new ArticleService(articleRepository, new ArticleMapper());
    }

    @Test
    void create_savesArticleAndReturnsResponse() {
        ArticleCreateRequest request = new ArticleCreateRequest();
        request.setTitle("Title");
        request.setText("Text");
        request.setAuthor("Author");
        request.setPublishedAt(Instant.parse("2024-01-01T00:00:00Z"));

        UUID id = UUID.randomUUID();
        when(articleRepository.saveAndFlush(any(Article.class))).thenAnswer(invocation -> {
            Article article = invocation.getArgument(0);
            article.setId(id);
            return article;
        });

        ArticleResponse response = articleService.create(request);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getAuthor()).isEqualTo("Author");
        verify(articleRepository).saveAndFlush(any(Article.class));
    }

    @Test
    void getById_returnsArticleResponse() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.setId(id);
        article.setTitle("Title");
        article.setText("Text");
        article.setAuthor("Author");
        article.setPublishedAt(Instant.parse("2024-01-01T00:00:00Z"));
        when(articleRepository.findById(id)).thenReturn(Optional.of(article));

        ArticleResponse response = articleService.getById(id);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo("Title");
        verify(articleRepository).findById(id);
    }

    @Test
    void getById_throwsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(articleRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articleService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Article not found");
    }

    @Test
    void update_updatesArticleAndReturnsResponse() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.setId(id);
        article.setTitle("Old");
        article.setText("Old text");
        article.setAuthor("Old author");
        when(articleRepository.findById(id)).thenReturn(Optional.of(article));
        when(articleRepository.saveAndFlush(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArticleUpdateRequest request = new ArticleUpdateRequest();
        request.setTitle("New");
        request.setText("New text");
        request.setAuthor("New author");

        ArticleResponse response = articleService.update(id, request);

        assertThat(response.getTitle()).isEqualTo("New");
        ArgumentCaptor<Article> captor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("New");
    }

    @Test
    void delete_removesArticle() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.setId(id);
        when(articleRepository.findById(id)).thenReturn(Optional.of(article));

        articleService.delete(id);

        verify(articleRepository).delete(article);
    }
}
