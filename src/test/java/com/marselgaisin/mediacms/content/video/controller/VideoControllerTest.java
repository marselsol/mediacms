package com.marselgaisin.mediacms.content.video.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.marselgaisin.mediacms.content.video.dto.VideoCreateRequest;
import com.marselgaisin.mediacms.content.video.dto.VideoResponse;
import com.marselgaisin.mediacms.content.video.service.VideoService;

@WebMvcTest(controllers = VideoController.class)
@Import({SecurityConfig.class, TestSecurityConfig.class})
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtService jwtService;

    @MockBean
    private VideoService videoService;

    @Test
    void getById_isPublic() throws Exception {
        UUID id = UUID.randomUUID();
        VideoResponse response = new VideoResponse();
        response.setId(id);
        response.setTitle("Title");
        response.setUrl("https://example.com/video");
        response.setDurationSeconds(120);
        when(videoService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/videos/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void create_requiresAuth() throws Exception {
        VideoCreateRequest request = new VideoCreateRequest();
        request.setTitle("Title");
        request.setUrl("https://example.com/video");
        request.setDurationSeconds(120);

        MvcResult result = mockMvc.perform(post("/api/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status == 401 || status == 403).isTrue();
    }

    @Test
    void create_allowsEditorToken() throws Exception {
        VideoCreateRequest request = new VideoCreateRequest();
        request.setTitle("Title");
        request.setUrl("https://example.com/video");
        request.setDurationSeconds(120);

        VideoResponse response = new VideoResponse();
        response.setId(UUID.randomUUID());
        response.setTitle("Title");
        response.setUrl("https://example.com/video");
        response.setDurationSeconds(120);
        when(videoService.create(any(VideoCreateRequest.class))).thenReturn(response);

        String token = jwtService.generateToken("editor");

        mockMvc.perform(post("/api/videos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(videoService).create(any(VideoCreateRequest.class));
    }
}
