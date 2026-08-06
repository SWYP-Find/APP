package com.picke.presentation.ui.comment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.comment.model.CommentUiModel
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData

@Composable
fun CommentItemCard(
    item: CommentUiModel,
    modifier: Modifier = Modifier,
    isMainContent: Boolean = false,
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onReportClick: () -> Unit = {}
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
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

            if (!isMainContent) {
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
                            CommentMenuItem(iconRes = R.drawable.ic_trash, text = "삭제") {
                                isMenuExpanded = false
                                onDeleteClick()
                            }
                            CommentMenuItem(iconRes = R.drawable.ic_edit, text = "수정") {
                                isMenuExpanded = false
                                onEditClick(item.content)
                            }
                        } else {
                            CommentMenuItem(iconRes = R.drawable.ic_bell, text = "신고") {
                                isMenuExpanded = false
                                onReportClick()
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = PickeTheme.colors.badgeBackground,
            shape = RoundedCornerShape(2.dp)
        ) {
            Text(
                text = item.stance,
                style = PickeTheme.typography.b5Medium,
                color = PickeTheme.colors.badgeText,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = item.content,
            style = PickeTheme.typography.b4Regular,
            color = PickeTheme.colors.neutral600
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { onLikeClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart_plus),
                        contentDescription = "좋아요",
                        modifier = Modifier.size(16.dp),
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

@Composable
fun CommentMenuItem(
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
        Text(text = text, style = PickeTheme.typography.labelMedium, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun CommentMenuItemPreview() {
    PickeTheme {
        CommentItemCard(item = DummyData.dummyComments.first())
    }
}