package com.picke.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun CustomTabBar(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedTab: String,
    isScrollable: Boolean = false,
    onTabSelected: (String) -> Unit
) {
    // 등분(비스크롤) 모드에서는 화면을 탭 개수만큼 균등 분할하므로, 탭이 많을 때(예: 탐색탭 7개)
    // 좌우 패딩이 크면 좁은 화면에서 글자가 줄바꿈된다. 비스크롤일 때는 패딩을 줄여 모두 한 줄에 보이게 한다.
    val tabHorizontalPadding = if (isScrollable) 16.dp else 4.dp
    val tabContent: @Composable () -> Unit = {
        tabs.forEach { tab ->
            Box(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints.copy(minWidth = 0))
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    }
                    .selectable(
                        selected = selectedTab == tab,
                        onClick = { onTabSelected(tab) }
                    )
                    .padding(horizontal = tabHorizontalPadding, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab,
                    style = PickeTheme.typography.labelMedium,
                    color = if(selectedTab == tab) PickeTheme.colors.primary else PickeTheme.colors.outline,
                    fontWeight = if(selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1
                )
            }
        }
    }

    if (isScrollable) {
        ScrollableTabRow(
            modifier = modifier.fillMaxWidth(),
            selectedTabIndex = tabs.indexOf(selectedTab),
            containerColor = Color.Transparent,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                    color = PickeTheme.colors.primary,
                )
            },
            divider = { HorizontalDivider(color = PickeTheme.colors.surfaceTertiary, thickness = 2.dp) }
        ) {
            tabContent()
        }
    } else {
        TabRow(
            modifier = modifier.fillMaxWidth(),
            selectedTabIndex = tabs.indexOf(selectedTab),
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                    color = PickeTheme.colors.primary,
                )
            },
            divider = { HorizontalDivider(color = PickeTheme.colors.borderDefault, thickness = 2.dp) }
        ) {
            tabContent()
        }
    }
}