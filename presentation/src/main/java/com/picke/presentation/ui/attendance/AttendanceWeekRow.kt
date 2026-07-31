package com.picke.presentation.ui.attendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.Beige900
import com.picke.presentation.ui.theme.Gray600
import com.picke.presentation.ui.theme.Primary500
import com.picke.presentation.ui.theme.SwypAppTheme
import com.picke.presentation.ui.theme.SwypTheme
import com.picke.presentation.ui.theme.White
import com.picke.presentation.ui.theme.tokens.BrandColorTokens
import com.picke.presentation.ui.theme.tokens.SemanticColorTokens

private val DayCellSize = 36.dp

/**
 * 월~일 7칸의 요일 라벨 + 출석 상태 원을 한 줄로 보여준다.
 */
@Composable
fun AttendanceWeekRow(
    days: List<AttendanceDayUiState>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = day.label,
                    style = SwypTheme.typography.b5Medium,
                    color = Gray600
                )
                AttendanceDayCell(status = day.status, points = day.points)
            }
        }
    }
}

/**
 * 요일 한 칸의 출석 상태 원. 성공/실패/미도래(빈 원)/미도래(보너스 원) 4가지 상태를 표현한다.
 */
@Composable
fun AttendanceDayCell(
    status: AttendanceDayStatus,
    points: Int?,
    modifier: Modifier = Modifier
) {
    when (status) {
        AttendanceDayStatus.SUCCESS -> Box(
            modifier = modifier
                .size(DayCellSize)
                .clip(CircleShape)
                .background(Primary500),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+${points}P",
                style = SwypTheme.typography.caption2SemiBold,
                color = White
            )
        }

        AttendanceDayStatus.FAIL -> Box(
            modifier = modifier
                .size(DayCellSize)
                .clip(CircleShape)
                .background(BrandColorTokens.beige600),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_x),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Beige900)
            )
        }

        AttendanceDayStatus.EMPTY -> Box(
            modifier = modifier
                .size(DayCellSize)
                .dashedCircleBorder(SemanticColorTokens.borderSubtle)
        )

        AttendanceDayStatus.EMPTY_GIFT -> Box(
            modifier = modifier
                .size(DayCellSize)
                .dashedCircleBorder(SemanticColorTokens.borderSubtle),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_point_gift),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun Modifier.dashedCircleBorder(
    color: Color,
    strokeWidth: Dp = 1.dp
) = this.drawBehind {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 3.dp.toPx()), 0f)
    )
    drawCircle(color = color, style = stroke, radius = (size.minDimension - stroke.width) / 2)
}

@Preview(showBackground = true)
@Composable
private fun AttendanceWeekRowPreview() {
    SwypAppTheme {
        AttendanceWeekRow(
            days = listOf(
                AttendanceDayUiState("월", AttendanceDayStatus.FAIL),
                AttendanceDayUiState("화", AttendanceDayStatus.SUCCESS, 5),
                AttendanceDayUiState("수", AttendanceDayStatus.FAIL),
                AttendanceDayUiState("목", AttendanceDayStatus.SUCCESS, 5),
                AttendanceDayUiState("금", AttendanceDayStatus.EMPTY),
                AttendanceDayUiState("토", AttendanceDayStatus.EMPTY),
                AttendanceDayUiState("일", AttendanceDayStatus.EMPTY_GIFT)
            )
        )
    }
}
