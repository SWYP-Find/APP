package com.swyp4.team2.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swyp4.team2.AppRoute
import com.swyp4.team2.ui.battle.BattleScreen
import com.swyp4.team2.ui.component.CustomBottomNavigationBar
import com.swyp4.team2.ui.explore.ExploreScreen
import com.swyp4.team2.ui.home.HomeScreen
import com.swyp4.team2.ui.my.MyScreen
import com.swyp4.team2.ui.my.content.ContentActivityScreen
import com.swyp4.team2.ui.my.history.DiscussionHistoryScreen
import com.swyp4.team2.ui.my.notice.NoticeEventScreen
import com.swyp4.team2.ui.my.philosopher.PhilosopherTypeScreen
import com.swyp4.team2.ui.my.setting.SettingScreen
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun MainScreen(
    rootNavController : NavController
){
    val mainNavController = rememberNavController()

    Scaffold(
        containerColor = SwypTheme.colors.background,
        bottomBar = {
            CustomBottomNavigationBar(mainNavController)
        }
    ){ innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(BottomNavItem.Home.route){
                HomeScreen(
                    onNavigateToAlarm = {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    }
                )
            }
            composable(BottomNavItem.Explore.route){
                ExploreScreen(
                    onNavigateToAlarm = {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    }
                )
            }
            composable(BottomNavItem.Battle.route){
                BattleScreen()
            }
            composable(BottomNavItem.My.route){
                MyScreen(
                    onNavigateToAlarm= {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    },
                    onNavigateToSetting = {
                        mainNavController.navigate(AppRoute.Setting.route)
                    },
                    onNavigateToDiscussion= {
                        mainNavController.navigate(AppRoute.DiscussionHistory.route)
                    },
                    onNavigateToPhilosopher= {
                        mainNavController.navigate(AppRoute.PhilosopherType.route)
                    },
                    onNavigateToContent= {
                        mainNavController.navigate(AppRoute.ContentActivity.route)
                    },
                    onNavigateToNotice={
                        mainNavController.navigate(AppRoute.NoticeEvent.route)
                    },
                )
            }


            composable(AppRoute.DiscussionHistory.route) {
                DiscussionHistoryScreen(
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
            composable(AppRoute.ContentActivity.route) {
                ContentActivityScreen(
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
            composable(AppRoute.PhilosopherType.route) {
                PhilosopherTypeScreen(
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
            composable(AppRoute.NoticeEvent.route) {
                NoticeEventScreen(
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
            composable(AppRoute.Setting.route){
                SettingScreen(
                    onBackClick = {
                        mainNavController.popBackStack()
                    },
                    onNavigateToSettingProfile = {
                        rootNavController.navigate(AppRoute.SettingProfile.route)
                    },
                    onNavigateToSettingAlarm = {
                        rootNavController.navigate(AppRoute.SettingAlarm.route)
                    },
                    onOpenWebLink = {
                        // 웹 링크 열기
                    }
                )
            }
        }
    }
}

