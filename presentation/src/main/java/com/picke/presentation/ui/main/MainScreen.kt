package com.picke.presentation.ui.main

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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.picke.presentation.AppRoute
import com.picke.presentation.analytics.ContentActionType
import com.picke.presentation.analytics.TrackScreenViews
import com.picke.presentation.analytics.rememberAnalyticsTracker
import com.picke.presentation.ui.component.CustomBottomNavigationBar
import com.picke.presentation.ui.explore.ExploreScreen
import com.picke.presentation.ui.home.HomeScreen
import com.picke.presentation.ui.my.MyScreen
import com.picke.presentation.ui.my.content.ContentActivityScreen
import com.picke.presentation.ui.my.discussion.DiscussionHistoryScreen
import com.picke.presentation.ui.my.makebattle.MakeBattleScreen
import com.picke.presentation.ui.my.notice.NoticeEventScreen
import com.picke.presentation.ui.my.philosopher.PhilosopherTypeScreen
import com.picke.presentation.ui.my.point.PointScreen
import com.picke.presentation.ui.my.setting.SettingScreen
import com.picke.presentation.ui.theme.SwypTheme
import com.picke.presentation.util.DeepLinkManager

@Composable
fun MainScreen(
    rootNavController : NavController,
    isNotificationSheetPending: Boolean = false,
){
    val mainNavController = rememberNavController()
    val analyticsTracker = rememberAnalyticsTracker()

    // 배틀 화면 등에서 "탐색 탭으로 이동" 같은 특정 탭 지정 진입이 예약되어 있으면 그 탭에서 시작하고,
    // 소비 즉시 리셋해서 이후의 일반적인 Main 진입에는 영향을 주지 않는다.
    val initialTabRoute = remember {
        DeepLinkManager.pendingTab?.also { DeepLinkManager.pendingTab = null } ?: BottomNavItem.Home.route
    }

    // 탭 NavHost 내부 화면들의 screen_view 자동 전송
    TrackScreenViews(mainNavController)

    var homeScrollTrigger by remember { mutableIntStateOf(0) }
    var exploreScrollTrigger by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = SwypTheme.colors.backgroundBrand,
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
            startDestination = initialTabRoute,
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
                    isNotificationSheetPending = isNotificationSheetPending,
                    onNavigateToAlarm = {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    },
                    onNavigateToVote = { contentId ->
                        analyticsTracker.trackContentAction(
                            ContentActionType.BATTLE_CARD_TAP,
                            contentId
                        )
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
                        analyticsTracker.trackContentAction(
                            ContentActionType.BATTLE_CARD_TAP,
                            battleId
                        )
                        rootNavController.navigate(AppRoute.BattleRouting.createRoute(battleId))
                    }
                )
            }
            composable(BottomNavItem.My.route){
                MyScreen(
                    onNavigateToAlarm = {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    },
                    onNavigateToSetting = {
                        mainNavController.navigate(AppRoute.Setting.route)
                    },
                    onNavigateToDiscussion = {
                        mainNavController.navigate(AppRoute.DiscussionHistory.route)
                    },
                    onNavigateToPhilosopher = {
                        mainNavController.navigate(AppRoute.PhilosopherType.route)
                    },
                    onNavigateToContent = {
                        mainNavController.navigate(AppRoute.ContentActivity.route)
                    },
                    onNavigateToNotice = {
                        mainNavController.navigate(AppRoute.NoticeEvent.createRoute())
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
                    onBackClick = { mainNavController.popBackStack() },
                    onNavigateToExplore = {
                        mainNavController.navigate(BottomNavItem.Explore.route) {
                            popUpTo(mainNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
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
            composable(
                route = AppRoute.NoticeEvent.route,
                arguments = listOf(navArgument("noticeId") { type = NavType.LongType; defaultValue = -1L })
            ) { backStackEntry ->
                val noticeId = backStackEntry.arguments?.getLong("noticeId")?.takeIf { it != -1L }
                NoticeEventScreen(
                    onBackClick = { mainNavController.popBackStack() },
                    initialNoticeId = noticeId
                )
            }
            composable(AppRoute.Setting.route){
                SettingScreen(
                    onBackClick = { mainNavController.popBackStack() },
                    onNavigateToSettingProfile = { rootNavController.navigate(AppRoute.SettingProfile.route) },
                    onNavigateToSettingAlarm = { rootNavController.navigate(AppRoute.SettingAlarm.route) },
                    onNavigateToPrivacyPolicy = { rootNavController.navigate(AppRoute.PrivacyPolicy.route) },
                    onNavigateToTermsOfService = { rootNavController.navigate(AppRoute.TermsOfService.route) },
                    onNavigateToWithdraw = { rootNavController.navigate(AppRoute.Withdraw.route) },
                    onNavigateToLogin = {
                        rootNavController.navigate(AppRoute.Login.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                )
            }
        }
    }
}