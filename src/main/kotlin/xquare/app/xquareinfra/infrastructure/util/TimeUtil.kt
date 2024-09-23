package xquare.app.xquareinfra.infrastructure.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

object TimeUtil {
    fun unixNanoToKoreanTime(nanoTime: Long): String {
        val instant = Instant.ofEpochSecond(0, nanoTime)
        val koreanZoneId = ZoneId.of("Asia/Seoul")
        val koreanTime = instant.atZone(koreanZoneId)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        return koreanTime.format(formatter)
    }

    fun unixNanoToMilliseconds(nanoTime: Long): Long {
        return nanoTime / 1_000_000
    }


    fun getTimeRangeInNanosMinutes(minutesAgo: Long): TimePair {
        val now = Instant.now()
        val past = now.minus(minutesAgo, ChronoUnit.MINUTES)

        return TimePair(
            past = past.toEpochMilli() * 1_000_000,
            now = now.toEpochMilli() * 1_000_000
        )
    }

    fun nowInNano(): Long {
        val now = Instant.now()
        return now.toEpochMilli() * 1_000_000
    }

    fun getTimeRangeInNanosSeconds(secondsAgo: Long): TimePair {
        val now = Instant.now()
        val past = now.minus(secondsAgo, ChronoUnit.SECONDS)

        return TimePair(
            past = past.toEpochMilli() * 1_000_000,
            now = now.toEpochMilli() * 1_000_000
        )
    }

    fun unixToKoreanTime(unixTimestamp: Long): String {
        val instant = Instant.ofEpochMilli(unixTimestamp)
        val koreanZoneId = ZoneId.of("Asia/Seoul")
        val koreanTime = instant.atZone(koreanZoneId)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return koreanTime.format(formatter)
    }

    fun calculateDurationMs(startTimeNano: Long, endTimeNano: Long): Long {
        return abs(endTimeNano - startTimeNano) / 1_000_000
    }
}