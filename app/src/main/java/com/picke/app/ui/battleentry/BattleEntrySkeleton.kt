package com.picke.app.ui.battleentry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picke.app.ui.component.SkeletonLine
import com.picke.app.ui.component.shimmer
import com.picke.app.ui.theme.SwypTheme

// 사전투표/관점 중 어디로 이동할지 API 응답을 받기 전까지 보여주는 화면.
// 목적지 화면의 레이아웃을 특정해서 흉내내면(예: VoteSkeleton) 실제로는 다른 화면으로
// 이동했을 때 모양이 안 맞아 레이아웃이 튀어 보이므로, 어느 쪽 구조도 암시하지 않는
// 중립적인 shimmer만 보여준다.
@Composable
fun BattleEntrySkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SwypTheme.colors.surface)
            .padding(20.dp)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmer()
        )
        Spacer(modifier = Modifier.height(24.dp))
        SkeletonLine(width = 200.dp, height = 24.dp)
        Spacer(modifier = Modifier.height(12.dp))
        SkeletonLine(width = 140.dp, height = 18.dp)
    }
}
