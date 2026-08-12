package com.picke.presentation.ui.my.user.componenet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun MySkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        ProfileSectionSkeleton()

        Spacer(modifier = Modifier.height(20.dp))
        CreditCardSkeleton()

        Spacer(modifier = Modifier.height(16.dp))
        PhilosopherTypeCardSkeleton()

        Spacer(modifier = Modifier.height(24.dp))
        repeat(3) {
            MyPageMenuItemSkeleton()
        }
    }
}

@Composable
private fun ProfileSectionSkeleton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .shimmer()
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            SkeletonLine(width = 100.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 60.dp, height = 14.dp)
        }
    }
}

@Composable
private fun CreditCardSkeleton() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(2.dp))
            .shimmer()
    )
}

@Composable
private fun PhilosopherTypeCardSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surfaceTertiary)
            .border(1.dp, PickeTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmer()
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            SkeletonLine(width = 60.dp, height = 14.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 100.dp, height = 18.dp)
        }
    }
}

@Composable
private fun MyPageMenuItemSkeleton() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonLine(width = 120.dp, height = 18.dp)
        }
        HorizontalDivider(color = PickeTheme.colors.borderDefault, thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun MySkeletonPreview() {
    PickeTheme { MySkeleton() }
}