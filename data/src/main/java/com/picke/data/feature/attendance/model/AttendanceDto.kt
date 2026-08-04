package com.picke.data.feature.attendance.model

import com.google.gson.annotations.SerializedName
import com.picke.domain.model.AttendanceBoard
import com.picke.domain.model.WeeklyAttendance
import com.picke.domain.model.WeeklyAttendanceDay
import com.picke.domain.model.WeeklyAttendanceDayStatus

/**
 * 출석 체크 API Response
 */
data class AttendanceCheckResponseDto(
    @SerializedName("user_tag")
    val userTag: String,
    @SerializedName("attended_at")
    val attendedAt: String,
    @SerializedName("points_earned")
    val pointsEarned: Int,
    @SerializedName("streak_bonus_earned")
    val streakBonusEarned: Boolean,
    @SerializedName("streak_bonus_points")
    val streakBonusPoints: Int,
    @SerializedName("consecutive_days")
    val consecutiveDays: Int,
    @SerializedName("total_points")
    val totalPoints: Int
)

fun AttendanceCheckResponseDto.toDomain(): AttendanceBoard {
    return AttendanceBoard(
        pointsEarned = this.pointsEarned,
        streakBonusEarned = this.streakBonusEarned,
        streakBonusPoints = this.streakBonusPoints,
        consecutiveDays = this.consecutiveDays,
        totalPoints = this.totalPoints
    )
}

/**
 * 이번 주 출석 현황 조회 API Response
 */
data class WeeklyAttendanceResponseDto(
    @SerializedName("user_tag")
    val userTag: String,
    @SerializedName("week_start_date")
    val weekStartDate: String,
    @SerializedName("consecutive_days")
    val consecutiveDays: Int,
    @SerializedName("is_streak_achieved")
    val isStreakAchieved: Boolean,
    @SerializedName("weekly_attendance")
    val weeklyAttendance: List<WeeklyAttendanceDayDto>,
    @SerializedName("streak_reward_points")
    val streakRewardPoints: Int
)

data class WeeklyAttendanceDayDto(
    @SerializedName("day")
    val day: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("points")
    val points: Int
)

fun WeeklyAttendanceResponseDto.toDomain(): WeeklyAttendance {
    return WeeklyAttendance(
        userTag = this.userTag,
        weekStartDate = this.weekStartDate,
        consecutiveDays = this.consecutiveDays,
        isStreakAchieved = this.isStreakAchieved,
        days = this.weeklyAttendance.map { it.toDomain() },
        streakRewardPoints = this.streakRewardPoints
    )
}

fun WeeklyAttendanceDayDto.toDomain(): WeeklyAttendanceDay {
    return WeeklyAttendanceDay(
        day = this.day,
        date = this.date,
        status = when (this.status) {
            "ATTENDED" -> WeeklyAttendanceDayStatus.ATTENDED
            "MISSED" -> WeeklyAttendanceDayStatus.MISSED
            else -> WeeklyAttendanceDayStatus.UPCOMING
        },
        points = this.points
    )
}
