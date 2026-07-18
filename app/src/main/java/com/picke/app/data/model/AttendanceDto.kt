package com.picke.app.data.model

import com.google.gson.annotations.SerializedName
import com.picke.app.domain.model.AttendanceBoard

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
