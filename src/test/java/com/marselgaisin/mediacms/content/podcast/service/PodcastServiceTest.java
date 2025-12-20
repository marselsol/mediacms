package com.marselgaisin.mediacms.content.podcast.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marselgaisin.mediacms.common.exception.NotFoundException;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastCreateRequest;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastEpisodeDto;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastResponse;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastUpdateRequest;
import com.marselgaisin.mediacms.content.podcast.mapper.PodcastMapper;
import com.marselgaisin.mediacms.content.podcast.model.Podcast;
import com.marselgaisin.mediacms.content.podcast.model.PodcastEpisode;
import com.marselgaisin.mediacms.content.podcast.repository.PodcastRepository;

@ExtendWith(MockitoExtension.class)
class PodcastServiceTest {

    @Mock
    private PodcastRepository podcastRepository;

    private PodcastService podcastService;

    @BeforeEach
    void setUp() {
        podcastService = new PodcastService(podcastRepository, new PodcastMapper());
    }

    @Test
    void create_savesPodcastWithEpisodes() {
        PodcastCreateRequest request = new PodcastCreateRequest();
        request.setTitle("Title");
        request.setUrl("https://example.com/podcast");
        request.setEpisodes(List.of(episodeDto(1), episodeDto(2)));

        UUID id = UUID.randomUUID();
        when(podcastRepository.saveAndFlush(any(Podcast.class))).thenAnswer(invocation -> {
            Podcast podcast = invocation.getArgument(0);
            podcast.setId(id);
            return podcast;
        });

        PodcastResponse response = podcastService.create(request);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getEpisodes()).hasSize(2);
        verify(podcastRepository).saveAndFlush(any(Podcast.class));
    }

    @Test
    void getById_returnsPodcastResponse() {
        UUID id = UUID.randomUUID();
        Podcast podcast = new Podcast();
        podcast.setId(id);
        podcast.setTitle("Title");
        podcast.setUrl("https://example.com/podcast");
        when(podcastRepository.findById(id)).thenReturn(Optional.of(podcast));

        PodcastResponse response = podcastService.getById(id);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo("Title");
        verify(podcastRepository).findById(id);
    }

    @Test
    void getById_throwsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(podcastRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> podcastService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Podcast not found");
    }

    @Test
    void update_replacesEpisodes() {
        UUID id = UUID.randomUUID();
        Podcast podcast = new Podcast();
        podcast.setId(id);
        podcast.setTitle("Old");
        podcast.setUrl("https://example.com/old");
        PodcastEpisode existing = new PodcastEpisode();
        existing.setEpisodeNumber(1);
        podcast.getEpisodes().add(existing);
        when(podcastRepository.findById(id)).thenReturn(Optional.of(podcast));
        when(podcastRepository.saveAndFlush(any(Podcast.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PodcastUpdateRequest request = new PodcastUpdateRequest();
        request.setTitle("New");
        request.setUrl("https://example.com/new");
        request.setEpisodes(List.of(episodeDto(3)));

        PodcastResponse response = podcastService.update(id, request);

        assertThat(response.getTitle()).isEqualTo("New");
        ArgumentCaptor<Podcast> captor = ArgumentCaptor.forClass(Podcast.class);
        verify(podcastRepository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getEpisodes()).hasSize(1);
        assertThat(captor.getValue().getEpisodes().get(0).getEpisodeNumber()).isEqualTo(3);
    }

    @Test
    void delete_removesPodcast() {
        UUID id = UUID.randomUUID();
        Podcast podcast = new Podcast();
        podcast.setId(id);
        when(podcastRepository.findById(id)).thenReturn(Optional.of(podcast));

        podcastService.delete(id);

        verify(podcastRepository).delete(podcast);
    }

    private PodcastEpisodeDto episodeDto(int number) {
        PodcastEpisodeDto dto = new PodcastEpisodeDto();
        dto.setEpisodeNumber(number);
        dto.setTitle("Episode " + number);
        dto.setUrl("https://example.com/episode-" + number);
        dto.setDurationSeconds(180);
        return dto;
    }
}
