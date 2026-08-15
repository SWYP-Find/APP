package com.picke.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picke.app.ui.component.SkeletonLine
import com.picke.app.ui.component.shimmer

// 홈 화면 최초 로딩 시, 실제 컨텐츠와 비슷한 형태의 뼈대에 반짝임 효과를 준 스켈레톤.
// 스피너 대신 이걸 보여줘서 "화면이 멈췄다"는 느낌 대신 "채워지고 있다"는 느낌을 준다.
@Composable
fun HomeSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 1. 에디터 픽 배너 자리
        EditorPickSkeleton()
        Spacer(modifier = Modifier.height(24.dp))

        // 2. 지금 뜨는 배틀 자리
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SkeletonLine(width = 120.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(2) {
                    TrendingCardSkeleton()
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        // 3. Best 배틀 자리
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SkeletonLine(width = 80.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(16.dp))
            repeat(3) {
                BestBattleRowSkeleton()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // 4. 새로운 배틀 자리
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SkeletonLine(width = 100.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(12.dp))
            repeat(2) {
                NewBattleCardSkeleton()
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// EditorPickSection과 동일한 padding/spacer 값을 그대로 써서
// 실제 UI와 스켈레톤의 전체 높이가 어긋나지 않도록 맞춘다.
@Composable
private fun EditorPickSkeleton() {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 상단 라벨 & 페이지네이션 자리
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SkeletonLine(width = 70.dp, height = 16.dp)
            SkeletonLine(width = 32.dp, height = 16.dp)
        }
        // 이미지 자리 (실제 이미지와 동일한 aspectRatio)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.8f)
                .shimmer()
        )
        // 하단 제목/요약/태그 자리
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SkeletonLine(width = 160.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 240.dp, height = 16.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 200.dp, height = 16.dp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SkeletonLine(width = 100.dp, height = 14.dp)
                SkeletonLine(width = 40.dp, height = 14.dp)
            }
        }
    }
}

@Composable
private fun TrendingCardSkeleton() {
    Column(modifier = Modifier.width(220.dp)) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
        )
        Spacer(modifier = Modifier.height(12.dp))
        SkeletonLine(width = 160.dp, height = 16.dp)
    }
}

@Composable
private fun BestBattleRowSkeleton() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            SkeletonLine(width = 200.dp, height = 18.dp)
            Spacer(modifier = Modifier.height(8.dp))
            SkeletonLine(width = 100.dp, height = 14.dp)
        }
    }
}

@Composable
private fun NewBattleCardSkeleton() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(2.dp))
            .shimmer()
    )
}
