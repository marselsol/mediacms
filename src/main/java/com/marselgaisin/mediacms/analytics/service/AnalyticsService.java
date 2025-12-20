package com.marselgaisin.mediacms.analytics.service;

import java.time.Duration;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.marselgaisin.mediacms.common.util.TimeBucketUtils;
import com.marselgaisin.mediacms.common.exception.BadRequestException;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private static final Duration TOP_TTL = Duration.ofHours(2);
    private static final Set<String> ALLOWED_TYPES = Set.of("article", "video", "podcast");

    private final StringRedisTemplate redisTemplate;

    public void trackView(String type, String id) {
        String normalizedType = normalizeType(type);

        redisTemplate.opsForValue().increment(buildViewsKey(normalizedType, id));

        String topKey = TimeBucketUtils.topKey(normalizedType);
        redisTemplate.opsForZSet().incrementScore(topKey, id, 1.0);

        Long ttlSeconds = redisTemplate.getExpire(topKey, java.util.concurrent.TimeUnit.SECONDS);
        if (ttlSeconds == null || ttlSeconds < 0) { // -1 = нет TTL, -2 = нет ключа
            redisTemplate.expire(topKey, TOP_TTL);
        }
    }


    public long getViews(String type, String id) {
        String normalizedType = normalizeType(type);
        String viewsKey = buildViewsKey(normalizedType, id);
        String value = redisTemplate.opsForValue().get(viewsKey);
        if (value == null) {
            return 0L;
        }
        return Long.parseLong(value);
    }

    private String buildViewsKey(String type, String id) {
        return "views:" + type + ":" + id;
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
