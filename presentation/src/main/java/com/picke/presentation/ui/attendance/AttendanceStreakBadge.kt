package com.picke.presentation.ui.attendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.Gray500
import com.picke.presentation.ui.theme.SwypAppTheme
import com.picke.presentation.ui.theme.SwypTheme
import com.picke.presentation.ui.theme.tokens.BrandColorTokens

/**
 * "🔥 N일 연속 출석 중/달성" 배지 + 오늘 획득 포인트를 보여주는 상단 정보 로우
 */
@Composable
fun AttendanceStreakBadge(
    streakDays: Int,
    isStreakAchieved: Boolean,
    earnedPoints: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .background(BrandColorTokens.secondary100, RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_point_fire),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            val stateLabel = if (isStreakAchieved) "달성" else "중"
            Text(
                text = "${streakDays}일 연속 출석 $stateLabel",
                style = SwypTheme.typography.b4Medium,
                color = BrandColorTokens.primary700
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "+${earnedPoints}P 획득",
                style = SwypTheme.typography.b4Medium,
                color = Gray500
            )
            Image(
                painter = painterResource(R.drawable.ic_point_coin),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AttendanceStreakBadgePreview() {
    SwypAppTheme {
        AttendanceStreakBadge(
            streakDays = 4,
            isStreakAchieved = false,
            earnedPoints = 5
        )
    }
}
