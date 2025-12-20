package com.marselgaisin.mediacms.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopItemResponse {

    private String id;
    private double score;
}
