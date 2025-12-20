package com.marselgaisin.mediacms.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCountResponse {

    private String type;
    private String id;
    private long views;
}
