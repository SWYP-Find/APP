package com.picke.presentation.ui.todaybattle.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun TopIndicatorBar(currentPage: Int, totalPages: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 0 until totalPages) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(if (i == currentPage) Color.White else Color.White.copy(alpha = 0.3f))
                )
            }
        }

        Text(
            text = "${currentPage + 1}/$totalPages",
            style = PickeTheme.typography.labelXSmall,
            color = Color.White.copy(alpha = 0.3f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopIndicatorBarPreview() {
    PickeTheme {
        TopIndicatorBar(
            currentPage = 1,
            totalPages = 3
        )
    }
}