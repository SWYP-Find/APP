package com.picke.presentation.ui.my.content

import androidx.compose.foundation.border
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.SwypTheme

// ContentActivityCard와 동일한 padding 값을 그대로 써서
// 로딩이 끝나고 실제 콘텐츠로 바뀔 때 레이아웃이 튀지 않도록 맞춘다.
@Composable
fun ContentActivitySkeleton(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(4) {
            ContentActivityCardSkeleton()
        }
    }
}

@Composable
private fun ContentActivityCardSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, SwypTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .shimmer()
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SkeletonLine(width = 60.dp, height = 16.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    SkeletonLine(width = 40.dp, height = 16.dp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                SkeletonLine(width = 80.dp, height = 14.dp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SkeletonLine(width = 250.dp, height = 16.dp)
        Spacer(modifier = Modifier.height(4.dp))
        SkeletonLine(width = 180.dp, height = 16.dp)

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            SkeletonLine(width = 30.dp, height = 16.dp)
        }
    }
}
