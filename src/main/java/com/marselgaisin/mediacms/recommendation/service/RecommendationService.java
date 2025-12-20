package com.marselgaisin.mediacms.recommendation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import com.marselgaisin.mediacms.analytics.dto.TopItemResponse;
import com.marselgaisin.mediacms.common.util.TimeBucketUtils;
import com.marselgaisin.mediacms.common.exception.BadRequestException;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private static final Set<String> ALLOWED_TYPES = Set.of("article", "video", "podcast");

    private final StringRedisTemplate redisTemplate;

    public List<TopItemResponse> top(String type, int limit) {
        String normalizedType = normalizeType(type);
        String topKey = TimeBucketUtils.topKey(normalizedType);
        if (limit <= 0) {
            return Collections.emptyList();
        }

        var tuples = redisTemplate.opsForZSet()
                .reverseRangeWithScores(topKey, 0, limit - 1);

        if (tuples == null || tuples.isEmpty()) {
            return Collections.emptyList();
        }

        List<TopItemResponse> result = new ArrayList<>(tuples.size());
        for (TypedTuple<String> tuple : tuples) {
            if (tuple.getValue() == null || tuple.getScore() == null) {
                continue;
            }
            result.add(new TopItemResponse(tuple.getValue(), tuple.getScore()));
        }
        return result;
    }

    private String normalizeType(String type) {
        if (type == null) {
            throw new BadRequestException("Content type is required");
        }
        String normalized = type.toLowerCase();
        if (!ALLOWED_TYPES.contains(normalized)) {
            throw new BadRequestException("Unsupported content type: " + type);
        }
        return normalized;
    }
}
