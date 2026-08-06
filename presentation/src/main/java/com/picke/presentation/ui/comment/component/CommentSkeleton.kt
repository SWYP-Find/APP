package com.picke.presentation.ui.comment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
fun CommentSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        CommentItemCardSkeleton(showContentTwoLines = true)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F8F6))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            SkeletonLine(width = 70.dp, height = 16.dp)
        }

        repeat(4) {
            CommentItemCardSkeleton()
            HorizontalDivider(thickness = 1.dp, color = PickeTheme.colors.borderDefault)
        }
    }
}

@Composable
private fun CommentItemCardSkeleton(showContentTwoLines: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .shimmer()
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                SkeletonLine(width = 48.dp, height = 16.dp)
                Spacer(modifier = Modifier.height(4.dp))
                SkeletonLine(width = 40.dp, height = 12.dp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        SkeletonLine(width = 70.dp, height = 20.dp)

        Spacer(modifier = Modifier.height(12.dp))

        SkeletonLine(width = 260.dp, height = 16.dp)
        if (showContentTwoLines) {
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 180.dp, height = 16.dp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            SkeletonLine(width = 36.dp, height = 16.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentSkeletonPreview() {
    CommentSkeleton()
}