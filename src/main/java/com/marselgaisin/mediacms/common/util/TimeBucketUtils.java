package com.marselgaisin.mediacms.common.util;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeBucketUtils {

    private TimeBucketUtils() {
    }

    private static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");

    public static String currentHourBucket() {
        return currentHourBucket(Clock.systemDefaultZone());
    }

    public static String currentHourBucket(Clock clock) {
        ZonedDateTime now = ZonedDateTime.now(clock).withMinute(0).withSecond(0).withNano(0);
        return now.format(HOUR_FORMATTER);
    }

    public static String topKey(String type) {
        return "top:" + type + ":" + currentHourBucket();
    }
}
