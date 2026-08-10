package com.picke.presentation.ui.scenario.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.scenario.model.ScenarioOptionUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData

@Composable
fun InteractiveOptionsUI(
    options: List<ScenarioOptionUiModel>,
    selectedNodeId: String?,
    onOptionClick: (String) -> Unit
) {
    var pendingSelectedId by remember(options) { mutableStateOf<String?>(null) }
    val selectGuideText = if (selectedNodeId == null) {
        "이제 당신의 입장을 선택해주세요"
    } else {
        "아래가 당신의 선택입니다."
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = PickeTheme.colors.neutral200
            )
            Text(
                text = selectGuideText,
                style = PickeTheme.typography.labelMedium.copy(fontStyle = FontStyle.Italic),
                color = PickeTheme.colors.textTertiary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = PickeTheme.colors.neutral200
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        options.forEach { option ->
            val isCommitted = selectedNodeId == option.nextNodeId
            val isPending = pendingSelectedId == option.nextNodeId
            val isSelected = isCommitted || isPending

            OptionSelectionCard(
                text = option.label,
                isSelected = isSelected,
                isEnabled = selectedNodeId == null,
                onClick = {
                    if (selectedNodeId == null) {
                        pendingSelectedId = option.nextNodeId
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (selectedNodeId == null) {
            OptionConfirmButton(
                text = "입장 선택하기",
                isEnabled = pendingSelectedId != null,
                onClick = {
                    pendingSelectedId?.let { onOptionClick(it) }
                }
            )
        }
    }
}

@Composable
fun OptionSelectionCard(
    text: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val borderColor =
        if (isSelected) PickeTheme.colors.secondary else PickeTheme.colors.borderSubtle
    val bgColor =
        if (isSelected) PickeTheme.colors.surfaceTertiary else PickeTheme.colors.surfaceTertiary
    val textColor =
        if (isSelected) PickeTheme.colors.textPrimary else PickeTheme.colors.textTertiary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(bgColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(2.dp)
            )
            .clickable(enabled = isEnabled) { onClick() }
            .padding(vertical = 20.dp, horizontal = 16.dp),
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
fun InteractiveOptionsUIPreview() {
    PickeTheme {
        InteractiveOptionsUI(
            options = DummyData.dummyPastChoices.first().options,
            selectedNodeId = "",
            onOptionClick = {}
        )
    }
}