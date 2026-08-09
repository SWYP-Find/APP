package com.picke.presentation.ui.perspective.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun PerspectiveEmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo_picke),
            contentDescription = "빈 화면 로고",
            modifier = Modifier.size(width = 160.dp, height = 120.dp),
            tint = PickeTheme.colors.borderDefault
        )
        Text(
            text = message,
            style = PickeTheme.typography.b3Regular,
            color = PickeTheme.colors.beige800
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PerspectiveEmptyStatePreview() {
    PickeTheme {
        PerspectiveEmptyState(message = "test message")
    }
}