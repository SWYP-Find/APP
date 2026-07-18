package com.picke.app.ui.perspective

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.picke.app.ui.component.SkeletonLine
import com.picke.app.ui.component.shimmer
import com.picke.app.ui.theme.SwypTheme

// 투표 비율 데이터(voteOptions)가 아직 로드되기 전에 보여주는 상단 영역 자리.
// PerspectiveHeader와 동일한 padding/spacer 값을 써서, 실데이터가 도착해
// 실제 헤더로 바뀔 때 높이가 튀지 않도록 맞춘다. 로딩 전 기본값(50:50 비율 등)을
// 그대로 렌더링하면 실제 비율로 값이 바뀌는 순간 바가 눈에 띄게 움직여 보이므로,
// 데이터가 준비되기 전에는 이 스켈레톤으로 가린다.
@Composable
fun PerspectiveHeaderSkeleton(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        PerspectiveHeaderSideSkeleton()
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            SkeletonLine(width = 96.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .shimmer()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        PerspectiveHeaderSideSkeleton()
    }
}

@Composable
private fun PerspectiveHeaderSideSkeleton() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        Spacer(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmer()
        )
        Spacer(modifier = Modifier.height(2.dp))
        SkeletonLine(width = 40.dp, height = 14.dp)
        SkeletonLine(width = 28.dp, height = 16.dp)
    }
}

// 전체/옵션별 탭바(CustomTabBar) 자리. voteOptions가 비어있는 동안은 실제 탭 개수를
// 알 수 없으므로(전체 1개 vs 전체+옵션 여러개), 특정 탭 구조를 흉내내지 않고
// 중립적인 shimmer 바 하나만 보여준다.
@Composable
fun PerspectiveTabBarSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            SkeletonLine(width = 64.dp, height = 20.dp)
        }
        HorizontalDivider(color = SwypTheme.colors.borderDefault, thickness = 2.dp)
    }
}

// PerspectiveItemCard와 동일한 padding 값을 그대로 써서
// 로딩이 끝나고 실제 콘텐츠로 바뀔 때 레이아웃이 튀지 않도록 맞춘다.
@Composable
fun PerspectiveListSkeleton(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(4) {
            PerspectiveItemCardSkeleton()
        }
    }
}

@Composable
private fun PerspectiveItemCardSkeleton() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(width = 1.dp, color = SwypTheme.colors.borderDefault)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // 1. 프로필 영역
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .shimmer()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SkeletonLine(width = 40.dp, height = 16.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        SkeletonLine(width = 28.dp, height = 16.dp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    SkeletonLine(width = 50.dp, height = 12.dp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. 본문 영역
            SkeletonLine(width = 260.dp, height = 16.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 200.dp, height = 16.dp)

            Spacer(modifier = Modifier.height(12.dp))

            // 3. 하단 영역 (더보기, 댓글 수, 좋아요)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                SkeletonLine(width = 40.dp, height = 14.dp)
                Spacer(modifier = Modifier.weight(1f))
                SkeletonLine(width = 24.dp, height = 14.dp)
                Spacer(modifier = Modifier.width(12.dp))
                SkeletonLine(width = 24.dp, height = 14.dp)
            }
        }
    }
}
