package com.picke.presentation.ui.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.Beige100
import com.picke.presentation.ui.theme.Gray100
import com.picke.presentation.ui.theme.Gray50
import com.picke.presentation.ui.theme.Gray900
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.White
import com.picke.presentation.ui.theme.tokens.SemanticColorTokens

/**
 * 당일 첫 진입 시 노출되는 출석체크 결과 바텀시트.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceCheckBottomSheet(
    uiState: AttendanceCheckUiState,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .background(Gray100, RoundedCornerShape(2.dp))
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 20.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.title,
                style = PickeTheme.typography.h2SemiBold,
                color = Gray900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = uiState.subtitle,
                style = PickeTheme.typography.b4Regular,
                color = SemanticColorTokens.textMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Beige100, RoundedCornerShape(6.dp))
                    .border(1.dp, Gray50, RoundedCornerShape(6.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AttendanceStreakBadge(
                    streakDays = uiState.streakDays,
                    isStreakAchieved = uiState.isStreakAchieved,
                    earnedPoints = uiState.earnedPoints
                )
                AttendanceWeekRow(days = uiState.days)
            }

            Spacer(modifier = Modifier.height(16.dp))

            AttendanceRewardFooter(
                title = uiState.rewardTitle,
                caption = uiState.rewardCaption
            )
        }
    }
}

private fun weekDayLabels() = listOf("월", "화", "수", "목", "금", "토", "일")

/**
 * 미리보기용 샘플 상태. streakDays는 항상 [currentStreak]로 days와 앞뒤가 맞게 계산한다.
 */
private fun sampleUiState(
    days: List<AttendanceDayUiState>,
    title: String,
    subtitle: String,
    earnedPoints: Int
): AttendanceCheckUiState {
    val streak = days.currentStreak()
    val isAchieved = streak >= 7
    return AttendanceCheckUiState(
        title = title,
        subtitle = subtitle,
        streakDays = streak,
        isStreakAchieved = isAchieved,
        earnedPoints = earnedPoints,
        days = days,
        rewardTitle = if (isAchieved) "다음 보상도 기대해주세요!" else "7일 연속 출석 시 +7P",
        rewardCaption = if (isAchieved) "월요일에 연속 출석체크가 초기화 돼요" else "실패해도 다음 주 월요일에 다시 도전해요"
    )
}

/** 진행 중 + 이번 주 실패 없음 (일요일 칸에 보너스 기대 아이콘 노출) */
private fun sampleSuccessInProgress(): AttendanceCheckUiState {
    val labels = weekDayLabels()
    val days = listOf(
        AttendanceDayUiState(labels[0], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[1], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[2], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[3], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[4], AttendanceDayStatus.EMPTY),
        AttendanceDayUiState(labels[5], AttendanceDayStatus.EMPTY),
        AttendanceDayUiState(labels[6], AttendanceDayStatus.EMPTY_GIFT)
    )
    return sampleUiState(
        days = days,
        title = "오늘의 출석체크 성공",
        subtitle = "픽케에 매일 출석하고 포인트를 모아 보세요",
        earnedPoints = 5
    )
}

/** 7일 연속 출석 달성 (일요일, 보너스 +7P) */
private fun sampleSuccessWeekComplete(): AttendanceCheckUiState {
    val labels = weekDayLabels()
    val days = labels.mapIndexed { index, label ->
        AttendanceDayUiState(label, AttendanceDayStatus.SUCCESS, if (index == 6) 7 else 5)
    }
    return sampleUiState(
        days = days,
        title = "7일 연속 출석 성공!",
        subtitle = "7일 연속 출석하여 +7p를 드려요",
        earnedPoints = 7
    )
}

/** 진행 중 + 이번 주 실패 있음 (일요일 칸은 보너스 기대 불가로 빈 원) */
private fun sampleFailureInProgress(): AttendanceCheckUiState {
    val labels = weekDayLabels()
    val days = listOf(
        AttendanceDayUiState(labels[0], AttendanceDayStatus.FAIL),
        AttendanceDayUiState(labels[1], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[2], AttendanceDayStatus.FAIL),
        AttendanceDayUiState(labels[3], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[4], AttendanceDayStatus.EMPTY),
        AttendanceDayUiState(labels[5], AttendanceDayStatus.EMPTY),
        AttendanceDayUiState(labels[6], AttendanceDayStatus.EMPTY)
    )
    return sampleUiState(
        days = days,
        title = "오늘의 출석체크 성공",
        subtitle = "픽케에 매일 출석하고 포인트를 모아 보세요",
        earnedPoints = 5
    )
}

/** 실패 포함 + 일요일까지 도달 (7일 보너스는 이미 물 건너감, 일요일도 +5P) */
private fun sampleFailureWeekComplete(): AttendanceCheckUiState {
    val labels = weekDayLabels()
    val days = listOf(
        AttendanceDayUiState(labels[0], AttendanceDayStatus.FAIL),
        AttendanceDayUiState(labels[1], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[2], AttendanceDayStatus.FAIL),
        AttendanceDayUiState(labels[3], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[4], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[5], AttendanceDayStatus.SUCCESS, 5),
        AttendanceDayUiState(labels[6], AttendanceDayStatus.SUCCESS, 5)
    )
    return sampleUiState(
        days = days,
        title = "오늘의 출석체크 성공",
        subtitle = "픽케에 매일 출석하고 포인트를 모아 보세요",
        earnedPoints = 5
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "출석체크 - 진행 중 (성공)")
@Composable
private fun AttendanceCheckBottomSheetSuccessInProgressPreview() {
    PickeTheme {
        AttendanceCheckBottomSheet(uiState = sampleSuccessInProgress(), onDismiss = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "출석체크 - 7일 연속 달성")
@Composable
private fun AttendanceCheckBottomSheetSuccessWeekCompletePreview() {
    PickeTheme {
        AttendanceCheckBottomSheet(uiState = sampleSuccessWeekComplete(), onDismiss = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "출석체크 - 진행 중 (실패 포함)")
@Composable
private fun AttendanceCheckBottomSheetFailureInProgressPreview() {
    PickeTheme {
        AttendanceCheckBottomSheet(uiState = sampleFailureInProgress(), onDismiss = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "출석체크 - 실패 포함 + 일주일 끝")
@Composable
private fun AttendanceCheckBottomSheetFailureWeekCompletePreview() {
    PickeTheme {
        AttendanceCheckBottomSheet(uiState = sampleFailureWeekComplete(), onDismiss = {})
    }
}
