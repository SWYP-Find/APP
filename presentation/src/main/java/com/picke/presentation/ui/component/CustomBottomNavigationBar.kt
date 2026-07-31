package com.picke.presentation.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.picke.presentation.analytics.UiActionName
import com.picke.presentation.analytics.rememberAnalyticsTracker
import com.picke.presentation.ui.main.BottomNavItem
import com.picke.presentation.ui.theme.SwypTheme

@SuppressLint("RestrictedApi")
@Composable
fun CustomBottomNavigationBar(
    mainNavController: NavController,
    rootNavController: NavController,
    onHomeReselected: () -> Unit = {},
    onExploreReselected: () -> Unit = {}
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.TodayBattle,
        BottomNavItem.My
    )

    val bottomTabRoutes = items.map { it.route }
    val analyticsTracker = rememberAnalyticsTracker()

    NavigationBar(
        containerColor = SwypTheme.colors.surface,
    ) {
        val navBackStackEntry by mainNavController.currentBackStackEntryAsState()

        val activeTabRoute = remember(navBackStackEntry) {
            mainNavController.currentBackStack.value.lastOrNull { entry ->
                entry.destination.route in bottomTabRoutes
            }?.destination?.route
        }

        items.forEach { item ->
            val isSelected = activeTabRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = SwypTheme.typography.label
                    )
                },

                selected = isSelected,

                onClick = {
                    val tabAction = when (item.route) {
                        BottomNavItem.Home.route -> UiActionName.TAB_HOME
                        BottomNavItem.Explore.route -> UiActionName.TAB_EXPLORE
                        BottomNavItem.TodayBattle.route -> UiActionName.TAB_QUICK_BATTLE
                        else -> UiActionName.TAB_MYPAGE
                    }
                    analyticsTracker.trackUiAction(tabAction)

                    if (item.route == BottomNavItem.TodayBattle.route) {
                        rootNavController.navigate(BottomNavItem.TodayBattle.route)
                    }else {
                        if (isSelected) {
                            when (item.route) {
                                BottomNavItem.Home.route -> onHomeReselected()
                                BottomNavItem.Explore.route -> onExploreReselected()
                            }

                            mainNavController.popBackStack(
                                route = item.route,
                                inclusive = false
                            )
                        } else {
                            mainNavController.navigate(item.route) {
                                popUpTo(mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SwypTheme.colors.textPrimary,
                    selectedTextColor = SwypTheme.colors.textPrimary,
                    unselectedIconColor = SwypTheme.colors.textPrimary.copy(alpha = 0.4f),
                    unselectedTextColor = SwypTheme.colors.textPrimary.copy(alpha = 0.4f),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}