package com.marselgaisin.mediacms.content.video.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marselgaisin.mediacms.common.exception.NotFoundException;
import com.marselgaisin.mediacms.content.video.dto.VideoCreateRequest;
import com.marselgaisin.mediacms.content.video.dto.VideoResponse;
import com.marselgaisin.mediacms.content.video.dto.VideoUpdateRequest;
import com.marselgaisin.mediacms.content.video.mapper.VideoMapper;
import com.marselgaisin.mediacms.content.video.model.Video;
import com.marselgaisin.mediacms.content.video.repository.VideoRepository;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    private VideoService videoService;

    @BeforeEach
    void setUp() {
        videoService = new VideoService(videoRepository, new VideoMapper());
    }

    @Test
    void create_savesVideoAndReturnsResponse() {
        VideoCreateRequest request = new VideoCreateRequest();
        request.setTitle("Title");
        request.setUrl("https://example.com/video");
        request.setDurationSeconds(120);

        UUID id = UUID.randomUUID();
        when(videoRepository.saveAndFlush(any(Video.class))).thenAnswer(invocation -> {
            Video video = invocation.getArgument(0);
            video.setId(id);
            return video;
        });

        VideoResponse response = videoService.create(request);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo("Title");
        verify(videoRepository).saveAndFlush(any(Video.class));
    }

    @Test
    void getById_returnsVideoResponse() {
        UUID id = UUID.randomUUID();
        Video video = new Video();
        video.setId(id);
        video.setTitle("Title");
        video.setUrl("https://example.com/video");
        video.setDurationSeconds(120);
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));

        VideoResponse response = videoService.getById(id);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo("Title");
        verify(videoRepository).findById(id);
    }

    @Test
    void getById_throwsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(videoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> videoService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Video not found");
    }

    @Test
    void update_updatesVideoAndReturnsResponse() {
        UUID id = UUID.randomUUID();
        Video video = new Video();
        video.setId(id);
        video.setTitle("Old");
        video.setUrl("https://example.com/old");
        video.setDurationSeconds(60);
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        when(videoRepository.saveAndFlush(any(Video.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VideoUpdateRequest request = new VideoUpdateRequest();
        request.setTitle("New");
        request.setUrl("https://example.com/new");
        request.setDurationSeconds(180);

        VideoResponse response = videoService.update(id, request);

        assertThat(response.getTitle()).isEqualTo("New");
        ArgumentCaptor<Video> captor = ArgumentCaptor.forClass(Video.class);
        verify(videoRepository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getUrl()).isEqualTo("https://example.com/new");
    }

    @Test
    void delete_removesVideo() {
        UUID id = UUID.randomUUID();
        Video video = new Video();
        video.setId(id);
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));

        videoService.delete(id);

        verify(videoRepository).delete(video);
    }
}
