package com.picke.presentation.ui.scenario.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun OptionConfirmButton(
    text: String,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isEnabled) {
        PickeTheme.colors.buttonPrimaryBackground
    } else {
        PickeTheme.colors.buttonPrimaryBackgroundDisabled
    }
    val textColor = PickeTheme.colors.buttonPrimaryText

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(bgColor)
            .clickable(enabled = isEnabled) { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = PickeTheme.typography.b5Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OptionConfirmButtonPreview() {
    PickeTheme {
        OptionConfirmButton(
            text = "test text",
            isEnabled = true,
            onClick = {}
        )
    }
}