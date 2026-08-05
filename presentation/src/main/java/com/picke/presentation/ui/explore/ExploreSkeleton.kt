package com.picke.presentation.ui.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme

// ExploreCard와 동일한 padding/size 값을 그대로 써서
// 실제 카드와 스켈레톤 카드의 높이가 어긋나지 않도록 맞춘다.
@Composable
fun ExploreSkeleton(modifier: Modifier = Modifier, itemCount: Int = 6) {
    Column(modifier = modifier.fillMaxWidth()) {
        repeat(itemCount) {
            HorizontalDivider(thickness = 1.dp, color = PickeTheme.colors.borderDefault)
            ExploreCardSkeleton()
        }
        HorizontalDivider(thickness = 1.dp, color = PickeTheme.colors.borderDefault)
    }
}

@Composable
private fun ExploreCardSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 썸네일 자리
        Spacer(
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(2.dp))
                .shimmer()
        )

        // 텍스트 정보 자리
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // 1. 카테고리 뱃지 & 제목
            Row {
                SkeletonLine(width = 36.dp, height = 18.dp)
                Spacer(modifier = Modifier.width(6.dp))
                SkeletonLine(width = 160.dp, height = 18.dp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            // 2. 설명 2줄
            SkeletonLine(width = 220.dp, height = 14.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 180.dp, height = 14.dp)

            Spacer(modifier = Modifier.weight(1f))

            // 3. 오디오 시간/조회수
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                SkeletonLine(width = 90.dp, height = 14.dp)
            }
        }
    }
}
