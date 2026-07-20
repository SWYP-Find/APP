package com.picke.app.ui.attendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.picke.app.R
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Primary50
import com.picke.app.ui.theme.Primary900
import com.picke.app.ui.theme.SwypAppTheme
import com.picke.app.ui.theme.SwypTheme
import com.picke.app.ui.theme.White
import com.picke.app.ui.theme.tokens.BrandColorTokens.primary700
import com.picke.app.ui.theme.tokens.SemanticColorTokens.borderSubtle
import com.picke.app.ui.theme.tokens.SemanticColorTokens.textMuted

/**
 * 바텀시트 하단의 보상 안내 알약 배지 + 캡션 문구
 */
@Composable
fun AttendanceRewardFooter(
    title: String,
    caption: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, Primary50, RoundedCornerShape(6.dp))
                .background(Beige200, RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_point_gift),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = title,
                style = SwypTheme.typography.b3SemiBold,
                color = Primary900
            )
        }

        Text(
            text = caption,
            style = SwypTheme.typography.b5Medium,
            color = textMuted
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AttendanceRewardFooterPreview() {
    SwypAppTheme {
        AttendanceRewardFooter(
            title = "7일 연속 출석 시 +7P",
            caption = "실패해도 다음 주 월요일에 다시 도전해요"
        )
    }
}
