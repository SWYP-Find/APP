package com.picke.presentation.ui.my.setting.alarm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer

// AlarmCategoryHeader/AlarmSettingItem과 동일한 padding 값을 그대로 써서
// 로딩이 끝나고 실제 콘텐츠로 바뀔 때 레이아웃이 튀지 않도록 맞춘다.
// 실제 화면과 동일하게 기능(2개)/소셜(3개)/마케팅(1개) 항목 개수를 맞춰서 높이를 재현했다.
@Composable
fun SettingAlarmSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        CategoryHeaderSkeleton()
        repeat(2) { AlarmSettingItemSkeleton() }
        CategoryHeaderSkeleton()
        repeat(3) { AlarmSettingItemSkeleton() }
        CategoryHeaderSkeleton()
        AlarmSettingItemSkeleton()
    }
}

@Composable
private fun CategoryHeaderSkeleton() {
    SkeletonLine(
        width = 90.dp,
        height = 14.dp,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
private fun AlarmSettingItemSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            SkeletonLine(width = 140.dp, height = 16.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 200.dp, height = 12.dp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Spacer(
            modifier = Modifier
                .width(36.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(50))
                .shimmer()
        )
    }
}
