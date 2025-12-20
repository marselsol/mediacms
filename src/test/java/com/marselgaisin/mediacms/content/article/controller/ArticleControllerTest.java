package com.marselgaisin.mediacms.content.article.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
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
import com.marselgaisin.mediacms.content.article.dto.ArticleCreateRequest;
import com.marselgaisin.mediacms.content.article.dto.ArticleResponse;
import com.marselgaisin.mediacms.content.article.service.ArticleService;

@WebMvcTest(controllers = ArticleController.class)
@Import({SecurityConfig.class, TestSecurityConfig.class})
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtService jwtService;

    @MockBean
    private ArticleService articleService;

    @Test
    void getById_isPublic() throws Exception {
        UUID id = UUID.randomUUID();
        ArticleResponse response = new ArticleResponse();
        response.setId(id);
        response.setTitle("Title");
        response.setText("Text");
        response.setAuthor("Author");
        response.setPublishedAt(Instant.parse("2024-01-01T00:00:00Z"));
        when(articleService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/articles/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void create_requiresAuth() throws Exception {
        ArticleCreateRequest request = new ArticleCreateRequest();
        request.setTitle("Title");
        request.setText("Text");
        request.setAuthor("Author");

        MvcResult result = mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status == 401 || status == 403).isTrue();
    }

    @Test
    void create_allowsEditorToken() throws Exception {
        ArticleCreateRequest request = new ArticleCreateRequest();
        request.setTitle("Title");
        request.setText("Text");
        request.setAuthor("Author");

        ArticleResponse response = new ArticleResponse();
        response.setId(UUID.randomUUID());
        response.setTitle("Title");
        response.setText("Text");
        response.setAuthor("Author");
        when(articleService.create(any(ArticleCreateRequest.class))).thenReturn(response);

        String token = jwtService.generateToken("editor");

        mockMvc.perform(post("/api/articles")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(articleService).create(any(ArticleCreateRequest.class));
    }
}
