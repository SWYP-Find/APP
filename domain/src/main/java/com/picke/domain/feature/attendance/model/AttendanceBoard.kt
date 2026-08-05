package com.picke.domain.feature.attendance.model

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

/**
 * 이번 주(월~일) 출석 현황 도메인 모델
 *
 * @property weekStartDate 이번 주 시작일(월요일)
 * @property consecutiveDays 현재까지의 연속 출석 일수
 * @property isStreakAchieved 7일 연속 출석 달성 여부
 * @property days 월~일 요일별 출석 상태
 * @property streakRewardPoints 7일 연속 출석 달성 시 지급되는 보너스 포인트
 */
data class WeeklyAttendance(
    val userTag: String,
    val weekStartDate: String,
    val consecutiveDays: Int,
    val isStreakAchieved: Boolean,
    val days: List<WeeklyAttendanceDay>,
    val streakRewardPoints: Int
)

data class WeeklyAttendanceDay(
    val day: String,
    val date: String,
    val status: WeeklyAttendanceDayStatus,
    val points: Int
)

enum class WeeklyAttendanceDayStatus {
    /** 출석 성공 */
    ATTENDED,

    /** 출석 실패 */
    MISSED,

    /** 아직 지나지 않은 날 */
    UPCOMING
}
