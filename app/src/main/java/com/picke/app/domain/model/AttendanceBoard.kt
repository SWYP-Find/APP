package com.picke.app.domain.model

/**
 * 출석 체크 결과 도메인 모델
 *
 * @property pointsEarned 이번 출석으로 지급된 기본 포인트
 * @property streakBonusEarned 연속 출석 보너스 지급 여부
 * @property streakBonusPoints 연속 출석 보너스로 지급된 포인트
 * @property consecutiveDays 현재까지의 연속 출석 일수
 * @property totalPoints 출석 체크 반영 후 사용자의 총 보유 포인트
 */
data class AttendanceBoard(
    val pointsEarned: Int,
    val streakBonusEarned: Boolean,
    val streakBonusPoints: Int,
    val consecutiveDays: Int,
    val totalPoints: Int
)
