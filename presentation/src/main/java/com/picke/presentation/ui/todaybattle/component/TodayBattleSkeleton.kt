package com.picke.presentation.ui.todaybattle.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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

@Composable
fun TodayBattleSkeleton(modifier: Modifier = Modifier) {
    val darkShimmerBase = PickeTheme.colors.neutral600
    val darkShimmerHighlight = PickeTheme.colors.neutral400

    Column(modifier = modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shimmer(darkShimmerBase, darkShimmerHighlight)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OpinionCardSkeleton(
                    darkShimmerBase = darkShimmerBase,
                    darkShimmerHighlight = darkShimmerHighlight
                )
                OpinionCardSkeleton(
                    darkShimmerBase = darkShimmerBase,
                    darkShimmerHighlight = darkShimmerHighlight
                )
            }

            Spacer(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmer(darkShimmerBase, darkShimmerHighlight)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(20.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(2.dp))
                .shimmer(darkShimmerBase, darkShimmerHighlight)
        )
    }
}

@Composable
private fun OpinionCardSkeleton(
    darkShimmerBase: Color,
    darkShimmerHighlight: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
            .background(PickeTheme.colors.neutral600)
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SkeletonLine(
            width = 40.dp,
            height = 12.dp,
            baseColor = darkShimmerBase,
            highlightColor = darkShimmerHighlight
        )

        Spacer(modifier = Modifier.height(4.dp))
        SkeletonLine(
            width = 120.dp,
            height = 22.dp,
            baseColor = darkShimmerBase,
            highlightColor = darkShimmerHighlight
        )

        Spacer(modifier = Modifier.height(6.dp))
        SkeletonLine(
            width = 160.dp,
            height = 12.dp,
            baseColor = darkShimmerBase,
            highlightColor = darkShimmerHighlight
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TodayBattleSkeletonPreview() {
    PickeTheme { TodayBattleSkeleton() }
}