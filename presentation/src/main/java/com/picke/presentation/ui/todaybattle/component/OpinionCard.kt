package com.picke.presentation.ui.todaybattle.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun OpinionCard(
    name: String,
    opinion: String,
    quote: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) PickeTheme.colors.secondary700 else Color.Transparent
    val bgColor = if (isSelected) PickeTheme.colors.textPrimary else PickeTheme.colors.textPrimary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            style = PickeTheme.typography.labelXSmall,
            color = PickeTheme.colors.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = opinion,
            style = PickeTheme.typography.h3SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "\"$quote\"",
            style = PickeTheme.typography.labelXSmall,
            color = Color.White.copy(0.3f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OpinionCardPreview() {
    PickeTheme {
        OpinionCard(
            name = "test name",
            opinion = "test opinion",
            quote = "test quote",
            isSelected = false,
            onClick = {}
        )
    }
}