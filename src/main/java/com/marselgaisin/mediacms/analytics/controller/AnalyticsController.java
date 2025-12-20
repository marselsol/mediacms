package com.marselgaisin.mediacms.analytics.controller;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marselgaisin.mediacms.analytics.dto.TopItemResponse;
import com.marselgaisin.mediacms.analytics.dto.ViewCountResponse;
import com.marselgaisin.mediacms.analytics.service.AnalyticsService;
import com.marselgaisin.mediacms.recommendation.service.RecommendationService;

@RestController
@RequestMapping("/api/analytics")
@Validated
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final RecommendationService recommendationService;

    @PostMapping("/view/{type}/{id}")
    public ResponseEntity<Void> trackView(@PathVariable String type, @PathVariable String id) {
        analyticsService.trackView(type, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/views/{type}/{id}")
    public ResponseEntity<ViewCountResponse> getViews(@PathVariable String type, @PathVariable String id) {
        long views = analyticsService.getViews(type, id);
        ViewCountResponse response = new ViewCountResponse(type.toLowerCase(), id, views);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top/{type}")
    public ResponseEntity<List<TopItemResponse>> top(@PathVariable String type,
                                                     @RequestParam(defaultValue = "10")
                                                     @Min(1) @Max(100) int limit) {
        List<TopItemResponse> items = recommendationService.top(type, limit);
        return ResponseEntity.ok(items);
    }
}
