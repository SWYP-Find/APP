package com.picke.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun BattleOpinionBox(
    opinion: String?,
    name: String?,
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, PickeTheme.colors.borderDisabled, RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surfaceSubtle)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(model = imageUrl, modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.width(4.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = opinion ?: "의견",
                style = PickeTheme.typography.label,
                color = PickeTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = name ?: "이름",
                style = PickeTheme.typography.labelXSmall,
                color = PickeTheme.colors.textMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BattleOpinionBoxPreview() {
    PickeTheme {
        BattleOpinionBox(
            opinion = "test opinion",
            name = "test name",
            imageUrl = ""
        )
    }
}