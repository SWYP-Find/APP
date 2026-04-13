package com.picke.app.ui.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.picke.app.AppRoute
import com.picke.app.ui.component.CustomBottomNavigationBar
import com.picke.app.ui.explore.ExploreScreen
import com.picke.app.ui.home.HomeScreen
import com.picke.app.ui.my.MyScreen
import com.picke.app.ui.my.content.ContentActivityScreen
import com.picke.app.ui.my.discussion.DiscussionHistoryScreen
import com.picke.app.ui.my.makebattle.MakeBattleScreen
import com.picke.app.ui.my.notice.NoticeEventScreen
import com.picke.app.ui.my.philosopher.PhilosopherTypeScreen
import com.picke.app.ui.my.point.PointScreen
import com.picke.app.ui.my.setting.SettingScreen
import com.picke.app.ui.my.setting.withdraw.WithdrawScreen
import com.picke.app.ui.theme.SwypTheme
import com.picke.app.ui.theme.Beige200

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
                    onNavigateToPoint = {
                        mainNavController.navigate(AppRoute.Point.route)
                    }
                )
            }

            composable(AppRoute.Point.route){
                PointScreen(
                    onBackClick = { mainNavController.popBackStack() },
                    onNavigateToMakeBattle = { mainNavController.navigate(AppRoute.MakeBattle.route) }
                )
            }

            composable(AppRoute.MakeBattle.route){
                MakeBattleScreen(
                    onBackClick = { mainNavController.popBackStack() }
                )
            }

            composable(AppRoute.DiscussionHistory.route) {
                DiscussionHistoryScreen(
                    onBackClick = { mainNavController.popBackStack() },
                    onNavigateToDetail = { battleId ->
                        rootNavController.navigate(AppRoute.Perspective.createRoute(battleId))
                    }
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
                    onNavigateToWithdraw = { rootNavController.navigate(AppRoute.Withdraw.route)}
                )
            }
        }
    }
}