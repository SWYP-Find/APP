package com.picke.presentation.ui.todaybattle

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
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme

// BattleContent와 동일한 weight/padding/spacer 값을 그대로 써서
// 로딩이 끝나고 실제 콘텐츠로 바뀔 때 레이아웃이 튀지 않도록 맞춘다.
// 화면 배경이 검은색이라 기본 밝은 shimmer 색 대신 어두운 톤(neutral600/neutral400)을 쓴다.
private val darkShimmerBase: Color
    @Composable get() = PickeTheme.colors.neutral600
private val darkShimmerHighlight: Color
    @Composable get() = PickeTheme.colors.neutral400

@Composable
fun TodayBattleSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        // 1. 상단 이미지 자리
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shimmer(darkShimmerBase, darkShimmerHighlight)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // 2. VS 카드 자리
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OpinionCardSkeleton()
                OpinionCardSkeleton()
            }

            // 정중앙 VS 뱃지도 실제 이미지가 그대로 노출되지 않도록 shimmer 처리한다.
            Spacer(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmer(darkShimmerBase, darkShimmerHighlight)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // 3. 하단 "배틀 시작하기" 버튼 자리 (실제 bottomBar와 동일한 여백)
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
private fun OpinionCardSkeleton() {
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
