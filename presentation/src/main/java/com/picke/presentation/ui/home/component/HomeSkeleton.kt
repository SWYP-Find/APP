package com.picke.presentation.ui.home.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer

@Composable
fun HomeSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        EditorPickSkeleton()
        Spacer(modifier = Modifier.height(24.dp))

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

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SkeletonLine(width = 80.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(16.dp))
            repeat(3) {
                BestBattleRowSkeleton()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

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

@Composable
private fun EditorPickSkeleton() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SkeletonLine(width = 70.dp, height = 16.dp)
            SkeletonLine(width = 32.dp, height = 16.dp)
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.8f)
                .shimmer()
        )
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

@Preview(showBackground = true)
@Composable
fun HomeSkeletonPreview() {
    HomeSkeleton()
}