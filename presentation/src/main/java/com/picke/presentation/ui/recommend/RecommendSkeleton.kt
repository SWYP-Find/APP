package com.picke.presentation.ui.recommend

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.SwypTheme

// RecommendItemCard와 동일한 padding 값을 그대로 써서
// 로딩이 끝나고 실제 콘텐츠로 바뀔 때 레이아웃이 튀지 않도록 맞춘다.
@Composable
fun RecommendListSkeleton(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(4) {
            RecommendItemCardSkeleton()
        }
    }
}

@Composable
private fun RecommendItemCardSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, SwypTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .padding(12.dp)
    ) {
        // 1. 태그 및 시간/조회수 자리
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonLine(width = 50.dp, height = 18.dp)
            SkeletonLine(width = 70.dp, height = 14.dp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. 제목/요약 자리
        SkeletonLine(width = 200.dp, height = 18.dp)
        Spacer(modifier = Modifier.height(6.dp))
        SkeletonLine(width = 240.dp, height = 14.dp)

        Spacer(modifier = Modifier.height(8.dp))

        // 3. VS 영역 자리
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            OpinionBoxSkeleton(modifier = Modifier.weight(1f))
            Spacer(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmer()
            )
            OpinionBoxSkeleton(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun OpinionBoxSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, SwypTheme.colors.borderDisabled, RoundedCornerShape(2.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmer()
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.weight(1f)) {
            SkeletonLine(width = 50.dp, height = 14.dp)
            Spacer(modifier = Modifier.height(2.dp))
            SkeletonLine(width = 36.dp, height = 12.dp)
        }
    }
}
