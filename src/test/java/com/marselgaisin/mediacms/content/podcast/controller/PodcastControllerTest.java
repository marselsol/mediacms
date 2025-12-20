package com.marselgaisin.mediacms.content.podcast.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.marselgaisin.mediacms.auth.security.JwtService;
import com.marselgaisin.mediacms.config.SecurityConfig;
import com.marselgaisin.mediacms.config.TestSecurityConfig;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastCreateRequest;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastEpisodeDto;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastResponse;
import com.marselgaisin.mediacms.content.podcast.service.PodcastService;

@WebMvcTest(controllers = PodcastController.class)
@Import({SecurityConfig.class, TestSecurityConfig.class})
class PodcastControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtService jwtService;

    @MockBean
    private PodcastService podcastService;

    @Test
    void getById_isPublic() throws Exception {
        UUID id = UUID.randomUUID();
        PodcastResponse response = new PodcastResponse();
        response.setId(id);
        response.setTitle("Title");
        response.setUrl("https://example.com/podcast");
        response.setEpisodes(List.of());
        when(podcastService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/podcasts/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void create_requiresAuth() throws Exception {
        PodcastCreateRequest request = new PodcastCreateRequest();
        request.setTitle("Title");
        request.setUrl("https://example.com/podcast");
        request.setEpisodes(List.of(episodeDto(1)));

        MvcResult result = mockMvc.perform(post("/api/podcasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status == 401 || status == 403).isTrue();
    }

    @Test
    void create_allowsEditorToken() throws Exception {
        PodcastCreateRequest request = new PodcastCreateRequest();
        request.setTitle("Title");
        request.setUrl("https://example.com/podcast");
        request.setEpisodes(List.of(episodeDto(1)));

        PodcastResponse response = new PodcastResponse();
        response.setId(UUID.randomUUID());
        response.setTitle("Title");
        response.setUrl("https://example.com/podcast");
        response.setEpisodes(List.of());
        when(podcastService.create(any(PodcastCreateRequest.class))).thenReturn(response);

        String token = jwtService.generateToken("editor");

        mockMvc.perform(post("/api/podcasts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(podcastService).create(any(PodcastCreateRequest.class));
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
