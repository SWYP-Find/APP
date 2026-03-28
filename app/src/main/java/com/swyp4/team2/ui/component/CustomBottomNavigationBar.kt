package com.swyp4.team2.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.swyp4.team2.ui.main.BottomNavItem
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@SuppressLint("RestrictedApi")
@Composable
fun CustomBottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.TodayBattle,
        BottomNavItem.My
    )

    val bottomTabRoutes = items.map { it.route }

    NavigationBar(
        containerColor = SwypTheme.colors.surface,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val activeTabRoute = remember(navBackStackEntry) {
            navController.currentBackStack.value.lastOrNull { entry ->
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
                    if (isSelected) {
                        navController.popBackStack(route = item.route, inclusive = false)
                    } else {
                        navController.navigate(item.route) {
                            if (item.route != BottomNavItem.TodayBattle.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Gray900,
                    selectedTextColor = Gray900,
                    unselectedIconColor = Gray900.copy(alpha = 0.4f),
                    unselectedTextColor = Gray900.copy(alpha = 0.4f),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}