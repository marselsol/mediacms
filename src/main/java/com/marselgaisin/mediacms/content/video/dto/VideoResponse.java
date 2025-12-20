package com.marselgaisin.mediacms.content.video.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Data
public class VideoResponse {

    private UUID id;
    private String title;
    private String url;
    private Integer durationSeconds;
    private Instant createdAt;
    private Instant updatedAt;
}
