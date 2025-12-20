package com.marselgaisin.mediacms.analytics.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;
    @Mock
    private ZSetOperations<String, String> zSetOperations;

    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        analyticsService = new AnalyticsService(redisTemplate);
    }

    @Test
    void trackView_incrementsCountersAndSetsTtl() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(redisTemplate.getExpire(anyString(), eq(TimeUnit.SECONDS))).thenReturn(null);

        analyticsService.trackView("article", "123");

        verify(valueOperations).increment("views:article:123");

        ArgumentCaptor<String> topKeyCaptor = ArgumentCaptor.forClass(String.class);
        verify(zSetOperations).incrementScore(topKeyCaptor.capture(), eq("123"), eq(1.0));

        String topKey = topKeyCaptor.getValue();
        assertThat(topKey).startsWith("top:article:");
        verify(redisTemplate).getExpire(eq(topKey), eq(TimeUnit.SECONDS));
        verify(redisTemplate).expire(eq(topKey), eq(Duration.ofHours(2)));
    }

    @Test
    void trackView_doesNotResetTtlWhenPresent() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(redisTemplate.getExpire(anyString(), eq(TimeUnit.SECONDS))).thenReturn(300L);

        analyticsService.trackView("article", "123");

        ArgumentCaptor<String> topKeyCaptor = ArgumentCaptor.forClass(String.class);
        verify(zSetOperations).incrementScore(topKeyCaptor.capture(), eq("123"), eq(1.0));
        verify(redisTemplate, never()).expire(eq(topKeyCaptor.getValue()), eq(Duration.ofHours(2)));
    }

    @Test
    void getViews_returnsZeroWhenMissing() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("views:video:abc")).thenReturn(null);

        long views = analyticsService.getViews("video", "abc");

        assertThat(views).isZero();
        verify(valueOperations).get("views:video:abc");
    }
}
