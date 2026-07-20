package com.picke.app.ui.attendance

/**
 * 출석체크 바텀시트의 요일 한 칸 상태
 */
enum class AttendanceDayStatus {
    /** 출석 성공 (포인트 지급됨) */
    SUCCESS,

    /** 출석 실패 (미출석) */
    FAIL,

    /** 아직 지나지 않은 날 */
    EMPTY,

    /** 아직 지나지 않은 일요일이면서, 이번 주 실패 없이 7일 보너스를 노려볼 수 있는 상태 */
    EMPTY_GIFT
}

data class AttendanceDayUiState(
    val label: String,
    val status: AttendanceDayStatus,
    val points: Int? = null
)

/**
 * 오늘의 출석체크 바텀시트 UI 상태
 *
 * @property streakDays 이번 주 연속 출석 일수 (배지에 노출)
 * @property isStreakAchieved true면 "N일 연속 출석 달성", false면 "N일 연속 출석 중"
 * @property earnedPoints 오늘 출석으로 획득한 포인트
 * @property days 월~일 7칸의 출석 상태
 */
data class AttendanceCheckUiState(
    val title: String,
    val subtitle: String,
    val streakDays: Int,
    val isStreakAchieved: Boolean,
    val earnedPoints: Int,
    val days: List<AttendanceDayUiState>,
    val rewardTitle: String,
    val rewardCaption: String
)

/**
 * 월요일부터 순서대로 훑으며 실패(FAIL)를 만나면 끊기는 "현재 연속 출석 일수"를 계산한다.
 * 아직 지나지 않은 날(EMPTY/EMPTY_GIFT)은 카운트에 영향을 주지 않는다.
 */
fun List<AttendanceDayUiState>.currentStreak(): Int {
    var streak = 0
    for (day in this) {
        when (day.status) {
            AttendanceDayStatus.SUCCESS -> streak++
            AttendanceDayStatus.FAIL -> streak = 0
            AttendanceDayStatus.EMPTY, AttendanceDayStatus.EMPTY_GIFT -> Unit
        }
    }
    return streak
}
