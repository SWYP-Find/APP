package com.picke.presentation.ui.perspective.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme

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
        HorizontalDivider(color = PickeTheme.colors.borderDefault, thickness = 2.dp)
    }
}

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
        border = BorderStroke(width = 1.dp, color = PickeTheme.colors.borderDefault)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .shimmer()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    SkeletonLine(width = 40.dp, height = 16.dp)
                    Spacer(modifier = Modifier.height(4.dp))
                    SkeletonLine(width = 50.dp, height = 12.dp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            SkeletonLine(width = 70.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(12.dp))
            SkeletonLine(width = 260.dp, height = 16.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 200.dp, height = 16.dp)

            Spacer(modifier = Modifier.height(12.dp))
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

@Preview(showBackground = true)
@Composable
fun PerspectiveHeaderSkeletonPreview() {
    PickeTheme { PerspectiveHeaderSkeleton() }
}

@Preview(showBackground = true)
@Composable
fun PerspectiveTabBarSkeletonPreview() {
    PickeTheme { PerspectiveTabBarSkeleton() }
}

@Preview(showBackground = true)
@Composable
fun PerspectiveListSkeletonPreview() {
    PickeTheme { PerspectiveListSkeleton() }
}