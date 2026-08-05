package com.picke.presentation.ui.my.notice

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.theme.PickeTheme

// NoticeEventCard와 동일한 padding 값을 그대로 써서
// 로딩이 끝나고 실제 콘텐츠로 바뀔 때 레이아웃이 튀지 않도록 맞춘다.
@Composable
fun NoticeEventSkeleton(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(4) {
            NoticeEventCardSkeleton()
        }
    }
}

@Composable
private fun NoticeEventCardSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, PickeTheme.colors.surfaceTertiary, RoundedCornerShape(2.dp))
            .padding(16.dp)
    ) {
        SkeletonLine(width = 50.dp, height = 18.dp)
        Spacer(modifier = Modifier.height(12.dp))
        SkeletonLine(width = 200.dp, height = 16.dp)
        Spacer(modifier = Modifier.height(4.dp))
        SkeletonLine(width = 240.dp, height = 14.dp)
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLine(width = 70.dp, height = 12.dp)
    }
}
