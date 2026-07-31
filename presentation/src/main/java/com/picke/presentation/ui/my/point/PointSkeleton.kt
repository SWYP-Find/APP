package com.picke.presentation.ui.my.point

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.theme.SwypTheme

// PointHistoryItem과 동일한 padding 값을 그대로 써서
// 로딩이 끝나고 실제 콘텐츠로 바뀔 때 레이아웃이 튀지 않도록 맞춘다.
@Composable
fun PointHistorySkeleton(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        items(6) {
            PointHistoryItemSkeleton()
        }
    }
}

@Composable
private fun PointHistoryItemSkeleton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, SwypTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                SkeletonLine(width = 120.dp, height = 16.dp)
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonLine(width = 70.dp, height = 12.dp)
            }
            Column(horizontalAlignment = Alignment.End) {
                SkeletonLine(width = 50.dp, height = 16.dp)
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonLine(width = 40.dp, height = 12.dp)
            }
        }
    }
}
