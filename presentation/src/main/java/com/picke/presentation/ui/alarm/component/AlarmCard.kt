package com.picke.presentation.ui.alarm.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.domain.feature.alarm.model.AlarmItemBoard
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.toRelativeTimeText

@Composable
fun AlarmCard(
    item: AlarmItemBoard,
    onClick: () -> Unit
) {
    val iconRes = when (item.category) {
        "CONTENT" -> {
            when (item.detailCode) {
                "NEW_BATTLE" -> R.drawable.ic_alarm_battle
                "COMMENT_LIKE" -> R.drawable.ic_alarm_like
                "NEW_COMMENT" -> R.drawable.ic_alarm_comment
                "CREDIT_EARNED" -> R.drawable.ic_alarm_point
                "VOTE_RESULT" -> R.drawable.ic_alarm_vote
                else -> R.drawable.ic_alarm_vote
            }
        }

        "NOTICE" -> R.drawable.ic_alarm_notice
        "EVENT" -> R.drawable.ic_alarm_calendar
        else -> R.drawable.ic_alarm_point
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
            .border(1.dp, PickeTheme.colors.borderDefault, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    style = PickeTheme.typography.caption2Medium,
                    color = PickeTheme.colors.textMuted,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.alignByBaseline()
                ) {
                    Text(
                        text = item.createdAt.toRelativeTimeText(),
                        style = PickeTheme.typography.caption2Medium,
                        color = PickeTheme.colors.neutral200
                    )

                    if (!item.isRead) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(PickeTheme.colors.primary, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.body,
                style = PickeTheme.typography.b3SemiBold,
                color = PickeTheme.colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmCardPreview() {
    AlarmCard(
        item = DummyData.dummyAlarmList.first(),
        onClick = {}
    )
}