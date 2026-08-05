package com.picke.presentation.ui.component

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
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun SortFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) PickeTheme.colors.primary else PickeTheme.colors.primaryLight
    val contentColor = if (isSelected) PickeTheme.colors.primaryLight else PickeTheme.colors.primary
    val borderColor = PickeTheme.colors.primary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(2.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(2.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = PickeTheme.typography.b4Medium,
            color = contentColor
        )
    }
}