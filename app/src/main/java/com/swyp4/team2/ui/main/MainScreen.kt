package com.swyp4.team2.ui.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.swyp4.team2.AppRoute
import com.swyp4.team2.ui.component.CustomBottomNavigationBar
import com.swyp4.team2.ui.explore.ExploreScreen
import com.swyp4.team2.ui.home.HomeScreen
import com.swyp4.team2.ui.my.MyScreen
import com.swyp4.team2.ui.my.content.ContentActivityScreen
import com.swyp4.team2.ui.my.discussion.DiscussionHistoryScreen
import com.swyp4.team2.ui.my.notice.NoticeEventScreen
import com.swyp4.team2.ui.my.philosopher.PhilosopherTypeScreen
import com.swyp4.team2.ui.my.setting.SettingScreen
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme
import com.swyp4.team2.ui.todaybattle.TodayBattleScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.swyp4.team2.ui.theme.Beige200

@Composable
fun MainScreen(
    rootNavController : NavController
){
    val mainNavController = rememberNavController()

    var homeScrollTrigger by remember { mutableIntStateOf(0) }
    var exploreScrollTrigger by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Beige200,
        bottomBar = {
            CustomBottomNavigationBar(
                mainNavController = mainNavController,
                rootNavController = rootNavController,
                onHomeReselected = { homeScrollTrigger++ },
                onExploreReselected = { exploreScrollTrigger++ }
            )
        }
    ){ innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .background(SwypTheme.colors.surface),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ){
            composable(BottomNavItem.Home.route){
                HomeScreen(
                    scrollToTopTrigger = homeScrollTrigger,
                    onNavigateToAlarm = {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    },
                    onNavigateToVote = { contentId->
                        rootNavController.navigate(AppRoute.BattleRouting.createRoute(contentId))
                    },
                    onNavigateToTrendingBattle = { },
                    onNavigateToNewBattle = { },
                    onNavigateToBestBattle = { },
                    onNavigateToTodayPicke = { }
                )
            }
            composable(BottomNavItem.Explore.route){
                ExploreScreen(
                    scrollToTopTrigger = exploreScrollTrigger,
                    onNavigateToAlarm = {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    },
                    onNavigateToVote = { battleId ->
                        rootNavController.navigate(AppRoute.BattleRouting.createRoute(battleId))
                    }
                )
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
                    onBackClick = { mainNavController.popBackStack() },
                    /* onNavigateToComment = { itemId ->
                        rootNavController.navigate(AppRoute.PerspectiveDetail.createRoute(itemId))
                    }*/
                )
            }
            composable(AppRoute.ContentActivity.route) {
                ContentActivityScreen(
                    onBackClick = { mainNavController.popBackStack() },
                    onNavigateToComment = { itemId ->
                        rootNavController.navigate(AppRoute.Comment.createRoute(itemId))
                    }
                )
            }
            composable(AppRoute.PhilosopherType.route) {
                PhilosopherTypeScreen(onBackClick = { mainNavController.popBackStack() })
            }
            composable(AppRoute.NoticeEvent.route) {
                NoticeEventScreen(onBackClick = { mainNavController.popBackStack() })
            }
            composable(AppRoute.Setting.route){
                SettingScreen(
                    onBackClick = { mainNavController.popBackStack() },
                    onNavigateToSettingProfile = { rootNavController.navigate(AppRoute.SettingProfile.route) },
                    onNavigateToSettingAlarm = { rootNavController.navigate(AppRoute.SettingAlarm.route) },
                    onNavigateToPrivacyPolicy = { rootNavController.navigate(AppRoute.PrivacyPolicy.route) },
                    onNavigateToTermsOfService = { rootNavController.navigate(AppRoute.TermsOfService.route) },
                    onNavigateToLogin = { rootNavController.navigate(AppRoute.Login.route) }
                )
            }
        }
    }
}