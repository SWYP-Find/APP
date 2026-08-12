package com.picke.presentation.ui.my.philosopher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme

// PhilosopherHeaderSection/TraitAnalysisSection/TasteReportSection/ChemistrySection과
// 동일한 padding/spacer 값을 그대로 써서 로딩이 끝나고 실제 콘텐츠로 바뀔 때 레이아웃이 튀지 않도록 맞춘다.
@Composable
fun PhilosopherTypeSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        HeaderCardSkeleton()
        TraitAnalysisCardSkeleton()
        TasteReportCardSkeleton()
        ChemistrySectionSkeleton()
    }
}

private val cardShape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 4.dp, bottomEnd = 4.dp)

@Composable
private fun HeaderCardSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.surface, cardShape)
            .border(1.dp, PickeTheme.colors.surfaceTertiary, cardShape)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(PickeTheme.colors.primary))

        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SkeletonLine(width = 100.dp, height = 14.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 120.dp, height = 28.dp)
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.size(68.dp).clip(CircleShape).shimmer())
            Spacer(modifier = Modifier.height(24.dp))
            SkeletonLine(width = 260.dp, height = 16.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonLine(width = 200.dp, height = 16.dp)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { SkeletonLine(width = 50.dp, height = 20.dp) }
            }
        }
    }
}

@Composable
private fun TraitAnalysisCardSkeleton() {
    Column {
        SkeletonLine(width = 80.dp, height = 20.dp)
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PickeTheme.colors.surface, RoundedCornerShape(4.dp))
                .border(1.dp, PickeTheme.colors.surfaceTertiary, RoundedCornerShape(4.dp))
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(220.dp).clip(CircleShape).shimmer())
            Spacer(modifier = Modifier.height(32.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(3) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        ScoreBarSkeleton(modifier = Modifier.weight(1f))
                        ScoreBarSkeleton(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBarSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(PickeTheme.colors.beige100, RoundedCornerShape(4.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SkeletonLine(width = 20.dp, height = 12.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .clip(CircleShape)
                .shimmer()
        )
        Spacer(modifier = Modifier.width(8.dp))
        SkeletonLine(width = 20.dp, height = 12.dp)
    }
}

@Composable
private fun TasteReportCardSkeleton() {
    Column {
        SkeletonLine(width = 100.dp, height = 20.dp)
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PickeTheme.colors.surface, RoundedCornerShape(4.dp))
                .border(1.dp, PickeTheme.colors.surfaceTertiary, RoundedCornerShape(4.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ReportStatItemSkeleton()
                Box(modifier = Modifier.height(40.dp).width(1.dp).background(PickeTheme.colors.surfaceTertiary))
                ReportStatItemSkeleton()
                Box(modifier = Modifier.height(40.dp).width(1.dp).background(PickeTheme.colors.surfaceTertiary))
                ReportStatItemSkeleton()
            }

            HorizontalDivider(color = PickeTheme.colors.surfaceTertiary)

            repeat(3) { index ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SkeletonLine(width = 80.dp, height = 16.dp)
                    SkeletonLine(width = 40.dp, height = 14.dp)
                }
                if (index < 2) HorizontalDivider(color = PickeTheme.colors.beige100)
            }
        }
    }
}

@Composable
private fun ReportStatItemSkeleton() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SkeletonLine(width = 30.dp, height = 28.dp)
        Spacer(modifier = Modifier.height(4.dp))
        SkeletonLine(width = 40.dp, height = 12.dp)
    }
}

@Composable
private fun ChemistrySectionSkeleton() {
    Column {
        SkeletonLine(width = 80.dp, height = 20.dp)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ChemistryCardSkeleton(modifier = Modifier.weight(1f).fillMaxHeight())
            ChemistryCardSkeleton(modifier = Modifier.weight(1f).fillMaxHeight())
        }

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(2.dp))
                .shimmer()
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ChemistryCardSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(PickeTheme.colors.surface, RoundedCornerShape(4.dp))
            .border(1.dp, PickeTheme.colors.surfaceTertiary, RoundedCornerShape(4.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SkeletonLine(width = 40.dp, height = 16.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.size(56.dp).clip(CircleShape).shimmer())
        Spacer(modifier = Modifier.height(12.dp))
        SkeletonLine(width = 60.dp, height = 18.dp)
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLine(width = 80.dp, height = 12.dp)
    }
}
