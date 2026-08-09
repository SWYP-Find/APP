package com.picke.presentation.ui.perspective.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.perspective.model.PerspectiveUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData

@Composable
fun PerspectiveItemCard(
    item: PerspectiveUiModel,
    modifier: Modifier = Modifier,
    status: String? = null,
    isDetail: Boolean = false,
    onMoreClick: () -> Unit = {},
    clickable: Boolean = true,
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val cardBgColor = when (status) {
        "REJECTED" -> Color(0xFFFFF9F9)
        "PENDING" -> PickeTheme.colors.secondary50
        else -> Color.White
    }
    val borderBadgeColor = when (status) {
        "REJECTED" -> Color(0xFFA64D47)
        "PENDING" -> PickeTheme.colors.secondary
        else -> PickeTheme.colors.borderDefault
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = clickable && !isDetail) { onMoreClick() },
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = if (isDetail) null else BorderStroke(width = 1.dp, color = borderBadgeColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfileImage(
                    model = item.profileImageUrl,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                )

                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (item.isMine) "나" else item.nickname,
                        style = PickeTheme.typography.labelMedium,
                        color = PickeTheme.colors.textSecondary
                    )
                    Text(
                        text = item.timeAgo,
                        style = PickeTheme.typography.labelXSmall,
                        color = PickeTheme.colors.outline
                    )
                }

                if (status != "PENDING") {
                    Box {
                        IconButton(
                            onClick = { isMenuExpanded = true },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_more),
                                "더보기",
                                tint = PickeTheme.colors.textMuted
                            )
                        }

                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                            modifier = Modifier
                                .background(PickeTheme.colors.primaryPressed)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            if (item.isMine) {
                                if (status != "REJECTED") {
                                    PerspectiveMenuItem(
                                        iconRes = R.drawable.ic_trash,
                                        text = "삭제"
                                    ) {
                                        isMenuExpanded = false
                                        onDeleteClick()
                                    }
                                }
                                PerspectiveMenuItem(iconRes = R.drawable.ic_edit, text = "수정") {
                                    isMenuExpanded = false
                                    onEditClick(item.content)
                                }
                            } else {
                                PerspectiveMenuItem(iconRes = R.drawable.ic_bell, text = "신고") {
                                    isMenuExpanded = false
                                    onReportClick()
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            if (status == "PENDING" || status == "REJECTED") {
                Surface(
                    color = borderBadgeColor,
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Text(
                        text = if (status == "PENDING") "검수중" else "거절됨",
                        style = PickeTheme.typography.b5Medium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            } else {
                Surface(
                    color = PickeTheme.colors.badgeBackground,
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Text(
                        text = item.optionTitle,
                        style = PickeTheme.typography.b5Medium,
                        color = PickeTheme.colors.badgeText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.content,
                style = PickeTheme.typography.b4Regular,
                color = PickeTheme.colors.neutral600,
                maxLines = if (isDetail) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))
            if (status != "PENDING" && status != "REJECTED") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!isDetail) {
                        Text(
                            text = "더보기",
                            style = PickeTheme.typography.b5Medium,
                            color = PickeTheme.colors.textMuted,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (clickable) onMoreClick()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    if (!isDetail) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onMoreClick()
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .clickable { onMoreClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_message),
                                    contentDescription = "댓글",
                                    modifier = Modifier.size(12.dp),
                                    tint = PickeTheme.colors.textMuted
                                )
                            }
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${item.replyCount}",
                                style = PickeTheme.typography.b5Medium,
                                color = PickeTheme.colors.textMuted
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onLikeClick()
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .clickable { onLikeClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart_plus),
                                contentDescription = "좋아요",
                                modifier = Modifier.size(12.dp),
                                tint = if (item.isLiked) PickeTheme.colors.primary else PickeTheme.colors.textMuted
                            )
                        }
                        Text(
                            text = "${item.likeCount}",
                            style = PickeTheme.typography.b5Medium,
                            color = if (item.isLiked) PickeTheme.colors.primary else PickeTheme.colors.textMuted,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PerspectiveMenuItem(
    iconRes: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = PickeTheme.typography.labelMedium,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PerspectiveItemCardPreview() {
    PickeTheme {
        PerspectiveItemCard(item = DummyData.dummyPerspectives.first())
    }
}