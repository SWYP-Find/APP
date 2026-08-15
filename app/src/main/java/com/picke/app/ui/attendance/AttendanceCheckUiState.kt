package com.picke.app.ui.attendance

import com.picke.app.domain.model.WeeklyAttendance
import com.picke.app.domain.model.WeeklyAttendanceDay
import java.time.LocalDate
import java.time.ZoneId

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

private val WEEKDAY_LABELS = listOf("월", "화", "수", "목", "금", "토", "일")

/**
 * [WeeklyAttendance] 조회 응답을 출석체크 바텀시트 UI 상태로 변환한다.
 *
 * 요일 칸의 성공/실패 여부는 서버가 내려주는 status 문자열이 아니라 날짜/포인트로 직접 판단한다.
 * (예: 화요일에 5P가 찍혀 있는데 월요일이 0P라면, status 값이 무엇이든 월요일은 출석하지 않은 것이므로
 * 실패(X)로 표시해야 한다 — status 값이 예상과 다르게 내려와도 안전하게 동작하도록 하기 위함)
 * 연속 출석 일수/달성 여부는 서버가 내려주는 값을 그대로 신뢰한다.
 */
fun WeeklyAttendance.toAttendanceCheckUiState(
    today: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
): AttendanceCheckUiState {
    fun WeeklyAttendanceDay.parsedDate(): LocalDate? = runCatching { LocalDate.parse(date) }.getOrNull()

    // 오늘(포함) 이전인데 포인트를 못 받은 날이 하나라도 있으면, 이번 주 7일 보너스는 이미 물 건너간 것이다.
    val hasFailureSoFar = days.any { day ->
        val date = day.parsedDate()
        date != null && !date.isAfter(today) && day.points <= 0
    }

    val uiDays = days.mapIndexed { index, day ->
        val date = day.parsedDate()
        val status = when {
            date == null -> AttendanceDayStatus.EMPTY
            date.isAfter(today) -> {
                val isLastDayOfWeek = index == days.lastIndex
                if (isLastDayOfWeek && !hasFailureSoFar) AttendanceDayStatus.EMPTY_GIFT else AttendanceDayStatus.EMPTY
            }
            day.points > 0 -> AttendanceDayStatus.SUCCESS
            else -> AttendanceDayStatus.FAIL
        }
        AttendanceDayUiState(
            label = WEEKDAY_LABELS.getOrElse(index) { day.day },
            status = status,
            points = day.points.takeIf { status == AttendanceDayStatus.SUCCESS }
        )
    }

    val todayEarnedPoints = days.find { it.date == today.toString() }?.points ?: 0

    return AttendanceCheckUiState(
        title = if (isStreakAchieved) "7일 연속 출석 성공!" else "오늘의 출석체크 성공",
        subtitle = if (isStreakAchieved) {
            "7일 연속 출석하여 +${streakRewardPoints}p를 드려요"
        } else {
            "픽케에 매일 출석하고 포인트를 모아 보세요"
        },
        streakDays = consecutiveDays,
        isStreakAchieved = isStreakAchieved,
        earnedPoints = todayEarnedPoints,
        days = uiDays,
        rewardTitle = if (isStreakAchieved) "다음 보상도 기대해주세요!" else "7일 연속 출석 시 +${streakRewardPoints}P",
        rewardCaption = if (isStreakAchieved) {
            "월요일에 연속 출석체크가 초기화 돼요"
        } else {
            "실패해도 다음 주 월요일에 다시 도전해요"
        }
    )
}
