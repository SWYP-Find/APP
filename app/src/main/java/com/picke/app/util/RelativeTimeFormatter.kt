package com.picke.app.util

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

private val RELATIVE_TIME_ZONE = ZoneId.of("Asia/Seoul")

/**
 * 서버가 내려주는 createdAt 문자열(ISO-8601 계열)을
 * "8분 전" / "1시간 전" / "3일 전" / "2주 전" / "한달 전" 같은 상대 시간 문구로 변환한다.
 * 알림 목록, 관점/댓글 화면, 내 콘텐츠 활동 카드가 모두 이 함수 하나를 거쳐 시간을 표시한다.
 */
fun String.toRelativeTimeText(): String {
    val createdAt = parseServerDateTime(this) ?: return this
    val now = LocalDateTime.now(RELATIVE_TIME_ZONE)
    if (createdAt.isAfter(now)) return "방금 전"

    val minutes = ChronoUnit.MINUTES.between(createdAt, now)
    val hours = ChronoUnit.HOURS.between(createdAt, now)
    val days = ChronoUnit.DAYS.between(createdAt, now)

    return when {
        minutes < 1 -> "방금 전"
        minutes < 60 -> "${minutes}분 전"
        hours < 24 -> "${hours}시간 전"
        days < 7 -> "${days}일 전"
        days < 35 -> "${days / 7}주 전"
        else -> {
            val months = ChronoUnit.MONTHS.between(createdAt.toLocalDate(), now.toLocalDate())
                .coerceAtLeast(1)
            if (months < 12) {
                if (months == 1L) "한달 전" else "${months}개월 전"
            } else {
                "${months / 12}년 전"
            }
        }
    }
}

private fun parseServerDateTime(raw: String): LocalDateTime? {
    if (raw.isBlank()) return null
    return try {
        LocalDateTime.parse(raw)
    } catch (e: Exception) {
        try {
            OffsetDateTime.parse(raw).atZoneSameInstant(RELATIVE_TIME_ZONE).toLocalDateTime()
        } catch (e2: Exception) {
            try {
                LocalDateTime.parse("${raw.take(10)}T00:00:00")
            } catch (e3: Exception) {
                null
            }
        }
    }
}
