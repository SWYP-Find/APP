package com.picke.app.ui.component

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
import com.picke.app.ui.theme.Beige400
import com.picke.app.ui.theme.Beige600
import com.picke.app.ui.theme.SwypTheme

@Composable
fun CustomTabBar(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedTab: String,
    isScrollable: Boolean = false,
    onTabSelected: (String) -> Unit
) {
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
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab,
                    style = SwypTheme.typography.labelMedium,
                    color = if(selectedTab == tab) SwypTheme.colors.primary else SwypTheme.colors.outline,
                    fontWeight = if(selectedTab == tab) FontWeight.Bold else FontWeight.Normal
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
                    color = SwypTheme.colors.primary,
                )
            },
            divider = { HorizontalDivider(color = Beige400, thickness = 2.dp) }
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
                    color = SwypTheme.colors.primary,
                )
            },
            divider = { HorizontalDivider(color = Beige600, thickness = 2.dp) }
        ) {
            tabContent()
        }
    }
}