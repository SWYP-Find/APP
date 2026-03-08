package com.swyp4.team2.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector){
    object Home : BottomNavItem("tab_home", "홈", Icons.Filled.Home)
    object Explore : BottomNavItem("tab_explore", "탐색", Icons.Default.Search)
    object Battle : BottomNavItem("tab_battle", "오늘의 배틀", Icons.Default.PlayArrow)
    object Profile : BottomNavItem("tab_profile", "마이페이지", Icons.Default.Person)
}