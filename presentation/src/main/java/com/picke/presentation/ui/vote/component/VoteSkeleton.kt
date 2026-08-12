package com.picke.presentation.ui.vote.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.vote.model.VoteType

@Composable
fun VoteSkeleton(
    voteType: VoteType,
    modifier: Modifier = Modifier
) {
    val isPreVote = voteType == VoteType.PRE
    val bgColor = if (isPreVote) PickeTheme.colors.surface else Color.Black
    val shimmerBase = if (isPreVote) null else PickeTheme.colors.neutral600
    val shimmerHighlight = if (isPreVote) null else PickeTheme.colors.neutral400

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer(
                        baseColor = shimmerBase,
                        highlightColor = shimmerHighlight
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SkeletonLine(
                        width = 48.dp,
                        height = 20.dp,
                        baseColor = shimmerBase,
                        highlightColor = shimmerHighlight
                    )
                    SkeletonLine(
                        width = 48.dp,
                        height = 20.dp,
                        baseColor = shimmerBase,
                        highlightColor = shimmerHighlight
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                SkeletonLine(
                    width = 220.dp,
                    height = 28.dp,
                    baseColor = shimmerBase,
                    highlightColor = shimmerHighlight
                )

                Spacer(modifier = Modifier.height(8.dp))
                SkeletonLine(
                    width = 160.dp,
                    height = 28.dp,
                    baseColor = shimmerBase,
                    highlightColor = shimmerHighlight
                )

                Spacer(modifier = Modifier.height(12.dp))
                SkeletonLine(
                    width = 240.dp,
                    height = 16.dp,
                    baseColor = shimmerBase,
                    highlightColor = shimmerHighlight
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VoteOptionCardSkeleton(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                )
                VoteOptionCardSkeleton(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                )
            }

            Spacer(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .shimmer(shimmerBase, shimmerHighlight)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(20.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(2.dp))
                .shimmer(shimmerBase, shimmerHighlight)
        )
    }
}

@Composable
private fun VoteOptionCardSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, PickeTheme.colors.borderDisabled, RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surfaceSubtle)
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmer()
        )
        Spacer(modifier = Modifier.height(16.dp))
        SkeletonLine(width = 60.dp, height = 20.dp)
        Spacer(modifier = Modifier.height(4.dp))
        SkeletonLine(width = 40.dp, height = 14.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun VoteSkeletonPreview() {
    PickeTheme {
        VoteSkeleton(voteType = VoteType.PRE)
    }
}