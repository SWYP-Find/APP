package com.picke.app.ui.vote

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
import androidx.compose.ui.unit.dp
import com.picke.app.ui.component.SkeletonLine
import com.picke.app.ui.component.shimmer
import com.picke.app.ui.theme.SwypTheme

// VoteScreen의 실제 weight/padding/spacer 값을 그대로 써서
// 로딩이 끝나고 실제 콘텐츠로 바뀔 때 레이아웃이 튀지 않도록 맞춘다.
// 사전투표(밝은 배경)는 기본 shimmer 톤을, 사후투표(검은 배경)는 어두운 톤을 쓴다.
@Composable
fun VoteSkeleton(voteType: VoteType, modifier: Modifier = Modifier) {
    val isPreVote = voteType == VoteType.PRE
    val bgColor = if (isPreVote) SwypTheme.colors.surface else Color.Black
    val shimmerBase = if (isPreVote) null else SwypTheme.colors.neutral600
    val shimmerHighlight = if (isPreVote) null else SwypTheme.colors.neutral400

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        // 1. 상단 이미지 + 태그/제목/설명 자리
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer(shimmerBase, shimmerHighlight)
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
                    SkeletonLine(width = 48.dp, height = 20.dp, baseColor = shimmerBase, highlightColor = shimmerHighlight)
                    SkeletonLine(width = 48.dp, height = 20.dp, baseColor = shimmerBase, highlightColor = shimmerHighlight)
                }
                Spacer(modifier = Modifier.height(16.dp))
                SkeletonLine(width = 220.dp, height = 28.dp, baseColor = shimmerBase, highlightColor = shimmerHighlight)
                Spacer(modifier = Modifier.height(8.dp))
                SkeletonLine(width = 160.dp, height = 28.dp, baseColor = shimmerBase, highlightColor = shimmerHighlight)
                Spacer(modifier = Modifier.height(12.dp))
                SkeletonLine(width = 240.dp, height = 16.dp, baseColor = shimmerBase, highlightColor = shimmerHighlight)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. 옵션 카드 자리
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
                VoteOptionCardSkeleton(modifier = Modifier.weight(0.5f).fillMaxHeight())
                VoteOptionCardSkeleton(modifier = Modifier.weight(0.5f).fillMaxHeight())
            }

            // VS 뱃지도 실제 이미지가 그대로 노출되지 않도록 shimmer 처리한다.
            Spacer(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .shimmer(shimmerBase, shimmerHighlight)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        // 3. 하단 "투표하기" 버튼 자리 (실제 bottomBar와 동일한 여백)
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

// VoteOptionCard는 테마와 무관하게 항상 밝은 surfaceSubtle 카드라, shimmer도 기본(밝은) 톤을 그대로 쓴다.
@Composable
private fun VoteOptionCardSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, SwypTheme.colors.borderDisabled, RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surfaceSubtle)
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
