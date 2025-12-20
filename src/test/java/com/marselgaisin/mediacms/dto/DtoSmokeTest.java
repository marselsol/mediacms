package com.marselgaisin.mediacms.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.marselgaisin.mediacms.auth.dto.AuthResponse;
import com.marselgaisin.mediacms.auth.dto.LoginRequest;
import com.marselgaisin.mediacms.auth.dto.RegisterRequest;
import com.marselgaisin.mediacms.content.article.dto.ArticleCreateRequest;
import com.marselgaisin.mediacms.content.article.dto.ArticleResponse;
import com.marselgaisin.mediacms.content.video.dto.VideoCreateRequest;
import com.marselgaisin.mediacms.content.video.dto.VideoResponse;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastCreateRequest;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastEpisodeDto;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastResponse;

class DtoSmokeTest {

    @Test
    void authDtos_storeValues() {
        RegisterRequest register = new RegisterRequest();
        register.setUsername("user");
        register.setPassword("pass");

        LoginRequest login = new LoginRequest();
        login.setUsername("user");
        login.setPassword("pass");

        AuthResponse response = new AuthResponse();
        response.setToken("Bearer token");

        assertThat(register.getUsername()).isEqualTo("user");
        assertThat(register.getPassword()).isEqualTo("pass");
        assertThat(login.getUsername()).isEqualTo("user");
        assertThat(login.getPassword()).isEqualTo("pass");
        assertThat(response.getToken()).isEqualTo("Bearer token");
    }

    @Test
    void contentDtos_storeValues() {
        ArticleCreateRequest articleCreate = new ArticleCreateRequest();
        articleCreate.setTitle("Title");
        articleCreate.setText("Text");
        articleCreate.setAuthor("Author");
        articleCreate.setPublishedAt(Instant.parse("2024-01-01T00:00:00Z"));

        ArticleResponse articleResponse = new ArticleResponse();
        UUID articleId = UUID.randomUUID();
        articleResponse.setId(articleId);
        articleResponse.setTitle("Title");
        articleResponse.setText("Text");
        articleResponse.setAuthor("Author");

        VideoCreateRequest videoCreate = new VideoCreateRequest();
        videoCreate.setTitle("Video");
        videoCreate.setUrl("https://example.com/video");
        videoCreate.setDurationSeconds(120);

        VideoResponse videoResponse = new VideoResponse();
        UUID videoId = UUID.randomUUID();
        videoResponse.setId(videoId);
        videoResponse.setTitle("Video");
        videoResponse.setUrl("https://example.com/video");
        videoResponse.setDurationSeconds(120);

        PodcastEpisodeDto episodeDto = new PodcastEpisodeDto();
        episodeDto.setEpisodeNumber(1);
        episodeDto.setTitle("Episode 1");
        episodeDto.setUrl("https://example.com/episode-1");
        episodeDto.setDurationSeconds(180);

        PodcastCreateRequest podcastCreate = new PodcastCreateRequest();
        podcastCreate.setTitle("Podcast");
        podcastCreate.setUrl("https://example.com/podcast");
        podcastCreate.setEpisodes(List.of(episodeDto));

        PodcastResponse podcastResponse = new PodcastResponse();
        UUID podcastId = UUID.randomUUID();
        podcastResponse.setId(podcastId);
        podcastResponse.setTitle("Podcast");
        podcastResponse.setUrl("https://example.com/podcast");
        podcastResponse.setEpisodes(List.of(episodeDto));

        assertThat(articleCreate.getTitle()).isEqualTo("Title");
        assertThat(articleCreate.getPublishedAt()).isEqualTo(Instant.parse("2024-01-01T00:00:00Z"));
        assertThat(articleResponse.getId()).isEqualTo(articleId);
        assertThat(videoCreate.getUrl()).isEqualTo("https://example.com/video");
        assertThat(videoResponse.getId()).isEqualTo(videoId);
        assertThat(podcastCreate.getEpisodes()).hasSize(1);
        assertThat(podcastResponse.getId()).isEqualTo(podcastId);
    }
}
