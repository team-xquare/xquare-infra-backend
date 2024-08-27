package xquare.app.xquareinfra.infrastructure.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
}