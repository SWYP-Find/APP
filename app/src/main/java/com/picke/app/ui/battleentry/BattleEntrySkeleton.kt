package com.picke.app.ui.battleentry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.picke.app.ui.component.shimmer

// 사전투표/관점 중 어디로 이동할지 API 응답을 받기 전까지 보여주는 화면.
// 목적지 화면의 레이아웃을 특정해서 흉내내면(예: VoteSkeleton) 실제로는 다른 화면으로
// 이동했을 때 모양이 안 맞아 레이아웃이 튀어 보이므로, 특정 콘텐츠 모양 없이
// 화면 전체를 shimmer로 덮는다.
@Composable
fun BattleEntrySkeleton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .shimmer()
    )
}
