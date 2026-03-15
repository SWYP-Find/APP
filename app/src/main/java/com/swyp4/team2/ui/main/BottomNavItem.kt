package com.swyp4.team2.ui.main

import com.swyp4.team2.R


sealed class BottomNavItem(val route: String, val title: String, val icon: Int){
    object Home : BottomNavItem("tab_home", "홈", R.drawable.ic_nav_home)
    object Explore : BottomNavItem("tab_explore", "탐색", R.drawable.ic_nav_explore)
    object Battle : BottomNavItem("tab_battle", "빠른배틀", R.drawable.ic_nav_battle)
    object My : BottomNavItem("tab_my", "마이", R.drawable.ic_nav_my)
}