package com.swyp4.team2.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun CustomTopAppBar(
    title: String? = null,
    showLogo: Boolean = false,
    showBackButton: Boolean = false,
    backgroundColor: Color,
    onBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    val startPadding = if (showBackButton) 4.dp else 20.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(
                start = startPadding,
                end = 16.dp,
                top = 16.dp,
                bottom = 4.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "뒤로가기",
                        tint = Gray900,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            if (showLogo) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Picke 로고",
                    tint = Color.Unspecified,
                )
            }

            if (title != null) {
                Text(
                    text = title,
                    style = SwypTheme.typography.h4SemiBold,
                    color = Gray900
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            actions()
        }
    }
}
