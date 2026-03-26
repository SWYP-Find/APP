package com.swyp4.team2.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.swyp4.team2.ui.theme.Primary50
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun SortFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // 선택 여부에 따라 색상 반전 처리
    val backgroundColor = if (isSelected) SwypTheme.colors.primary else Primary50
    val contentColor = if (isSelected) Primary50 else SwypTheme.colors.primary
    val borderColor = SwypTheme.colors.primary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SwypTheme.typography.b4Medium, // 폰트 사이즈가 작아 보여서 b4Medium으로 설정했습니다. 필요시 조정하세요!
            color = contentColor
        )
    }
}