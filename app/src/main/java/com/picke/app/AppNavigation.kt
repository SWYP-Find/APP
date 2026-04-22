package com.picke.app

import ScenarioScreen
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.picke.app.R
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.picke.app.ui.alarm.AlarmScreen
import com.picke.app.ui.comment.CommentScreen
import com.picke.app.ui.login.LoginScreen
import com.picke.app.ui.main.BottomNavItem
import com.picke.app.ui.main.MainScreen
import com.picke.app.ui.my.philosopher.PhilosopherTypeScreen
import com.picke.app.ui.my.setting.alarm.SettingAlarmScreen
import com.picke.app.ui.my.setting.policy.PrivacyPolicyScreen
import com.picke.app.ui.my.setting.policy.TermsOfServiceScreen
import com.picke.app.ui.my.setting.profile.SettingProfileScreen
import com.picke.app.ui.my.setting.withdraw.WithdrawScreen
import com.picke.app.ui.onboarding.OnboardingScreen
import com.picke.app.ui.perspective.PerspectiveScreen
import com.picke.app.ui.recommend.RecommendScreen
import com.picke.app.ui.routing.BattleRoutingScreen
import com.picke.app.ui.splash.SplashUiState
import com.picke.app.ui.splash.SplashViewModel
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Primary500
import com.picke.app.ui.todaybattle.TodayBattleScreen
import com.picke.app.ui.vote.VoteRoute
import com.picke.app.ui.vote.VoteType
import com.picke.app.util.DeepLinkEvent
import com.picke.app.util.DeepLinkManager
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation(splashViewModel: SplashViewModel) {
    val rootNavController = rememberNavController()
    val uiState by splashViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is SplashUiState.NavigateToOnboarding -> {
                rootNavController.navigate(AppRoute.Onboarding.route) { popUpTo(0) }
            }
            is SplashUiState.NavigateToLogin -> {
                rootNavController.navigate(AppRoute.Login.route) { popUpTo(0) }
            }
            is SplashUiState.NavigateToMain -> {
                rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) }
            }
            is SplashUiState.NavigateToOtherPhilosopher -> {
                rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) }
                rootNavController.navigate(AppRoute.OtherPhilosopher.createRoute(state.reportId))
            }
            is SplashUiState.NavigateToBattle -> {
                rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) }
                rootNavController.navigate(AppRoute.BattleRouting.createRoute(state.battleId))
            }
            is SplashUiState.Loading -> { /* 가만히 스플래시 유지 */ }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Beige200
    ) {
        LaunchedEffect(Unit) {
            DeepLinkManager.deepLinkEvent.collect { event ->
                rootNavController.navigate(AppRoute.Main.route) {
                    popUpTo(AppRoute.Main.route) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
                kotlinx.coroutines.delay(100)
                when (event) {
                    is DeepLinkEvent.GoToBattle -> rootNavController.navigate(AppRoute.BattleRouting.createRoute(event.battleId))
                    is DeepLinkEvent.GoToReport -> rootNavController.navigate(AppRoute.OtherPhilosopher.createRoute(event.reportId))
                }
            }
        }

        NavHost(
            navController = rootNavController,
            startDestination = "blank_start",
            modifier = Modifier.fillMaxSize(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable("blank_start") {
                val isDeepLink = DeepLinkManager.pendingReportId != null || DeepLinkManager.pendingBattleId != null
                Box(
                    modifier = Modifier.fillMaxSize().background(Primary500),
                    contentAlignment = Alignment.Center
                ) { }
            }

            composable(AppRoute.Onboarding.route) {
                OnboardingScreen(
                    onNavigateToLogin = {
                        rootNavController.navigate(AppRoute.Login.route) {
                            popUpTo(AppRoute.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = AppRoute.Login.route,
                exitTransition = { fadeOut(animationSpec = tween(100)) }
            ) {
                LoginScreen(
                    onNavigateToMain = {
                        val pendingReport = DeepLinkManager.pendingReportId
                        val pendingBattle = DeepLinkManager.pendingBattleId

                        rootNavController.navigate(AppRoute.Main.route) {
                            popUpTo(AppRoute.Login.route) { inclusive = true }
                        }

                        if (pendingReport != null || pendingBattle != null) {
                            coroutineScope.launch {
                                kotlinx.coroutines.delay(100)
                                if (pendingReport != null) {
                                    rootNavController.navigate(AppRoute.OtherPhilosopher.createRoute(pendingReport))
                                    DeepLinkManager.pendingReportId = null
                                } else if (pendingBattle != null) {
                                    rootNavController.navigate(AppRoute.BattleRouting.createRoute(pendingBattle))
                                    DeepLinkManager.pendingBattleId = null
                                }
                            }
                        }
                    },
                )
            }

            composable(route = AppRoute.Main.route) {
                MainScreen(rootNavController = rootNavController)
            }

            composable(
                route = AppRoute.BattleRouting.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId") ?: ""
                BattleRoutingScreen(
                    onNavigateToPreVote = { id ->
                        DeepLinkManager.pendingBattleId = null
                        rootNavController.navigate(AppRoute.PreVote.createRoute(id)) {
                            popUpTo(AppRoute.BattleRouting.route) { inclusive = true }
                        }
                    },
                    onNavigateToPerspective = { id ->
                        DeepLinkManager.pendingBattleId = null
                        rootNavController.navigate(AppRoute.Perspective.createRoute(id)) {
                            popUpTo(AppRoute.BattleRouting.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(BottomNavItem.TodayBattle.route) {
                TodayBattleScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    onEnterBattle = { battleId ->
                        rootNavController.navigate(AppRoute.BattleRouting.createRoute(battleId))
                    }
                )
            }

            composable(AppRoute.Alarm.route) {
                AlarmScreen(onBackClick = { rootNavController.popBackStack() })
            }

            composable(AppRoute.SettingAlarm.route) {
                SettingAlarmScreen(onBackClick = { rootNavController.popBackStack() })
            }

            composable(AppRoute.SettingProfile.route) {
                SettingProfileScreen(onBackClick = { rootNavController.popBackStack() })
            }

            composable(
                route = AppRoute.PreVote.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId") ?: ""
                VoteRoute(
                    voteType = VoteType.PRE,
                    onBackClick = {
                        val prevRoute = rootNavController.previousBackStackEntry?.destination?.route
                        if (prevRoute == null || prevRoute == AppRoute.Splash.route) {
                            rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) { inclusive = true } }
                        } else {
                            rootNavController.popBackStack()
                        }
                    },
                    onVoteSubmit = { submittedBattleId ->
                        rootNavController.navigate(AppRoute.Scenario.createRoute(submittedBattleId)) {
                            popUpTo(AppRoute.PreVote.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = AppRoute.Scenario.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val contentId = backStackEntry.arguments?.getString("battleId") ?: ""
                ScenarioScreen(
                    battleId = contentId,
                    onBackClick = { rootNavController.popBackStack() },
                    onNextClick = {
                        rootNavController.navigate(AppRoute.PostVote.createRoute(contentId)) {
                            popUpTo(AppRoute.Scenario.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = AppRoute.PostVote.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId") ?: ""
                VoteRoute(
                    voteType = VoteType.POST,
                    onBackClick = { rootNavController.popBackStack() },
                    onVoteSubmit = { submittedBattleId ->
                        rootNavController.navigate(AppRoute.Perspective.createRoute(submittedBattleId)) {
                            popUpTo(AppRoute.Main.route) { inclusive = false }
                        }
                    }
                )
            }

            composable(
                route = AppRoute.Perspective.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId") ?: ""
                PerspectiveScreen(
                    onBackClick = {
                        val prevRoute = rootNavController.previousBackStackEntry?.destination?.route
                        if (prevRoute == null || prevRoute == AppRoute.Splash.route || prevRoute == AppRoute.Login.route) {
                            rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) { inclusive = true } }
                        } else {
                            rootNavController.popBackStack()
                        }
                    },
                    onNextClick = { itemId -> rootNavController.navigate(AppRoute.Recommend.createRoute(itemId)) },
                    onMoreClick = { itemId -> rootNavController.navigate(AppRoute.Comment.createRoute(itemId)) }
                )
            }

            composable(
                route = AppRoute.Comment.route,
                arguments = listOf(navArgument("itemId") { type = NavType.StringType })
            ) {
                CommentScreen(onBackClick = { rootNavController.popBackStack() })
            }

            composable(
                route = AppRoute.Recommend.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) {
                RecommendScreen(
                    onCloseClick = { rootNavController.popBackStack(AppRoute.Main.route, inclusive = false) },
                    onBackClick = { rootNavController.popBackStack() },
                    onItemClick = { clickedBattleId ->
                        rootNavController.navigate(AppRoute.BattleRouting.createRoute(clickedBattleId))
                    }
                )
            }

            composable(AppRoute.PrivacyPolicy.route) {
                PrivacyPolicyScreen(onBackClick = { rootNavController.popBackStack() })
            }

            composable(AppRoute.TermsOfService.route) {
                TermsOfServiceScreen(onBackClick = { rootNavController.popBackStack() })
            }

            composable(
                route = AppRoute.OtherPhilosopher.route,
                arguments = listOf(navArgument("reportId") { type = NavType.StringType })
            ) { backStackEntry ->
                val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
                PhilosopherTypeScreen(
                    reportId = reportId,
                    onBackClick = {
                        val prevRoute = rootNavController.previousBackStackEntry?.destination?.route
                        if (prevRoute == null || prevRoute == AppRoute.Splash.route || prevRoute == AppRoute.Login.route) {
                            rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) { inclusive = true } }
                        } else {
                            rootNavController.popBackStack()
                        }
                    },
                    onGoToSplashClick = {
                        DeepLinkManager.pendingReportId = null
                        rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) { inclusive = true } }
                    }
                )
            }

            composable(AppRoute.Withdraw.route) {
                WithdrawScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    onNavigateToLogin = {
                        rootNavController.navigate(AppRoute.Login.route) { popUpTo(0) { inclusive = true } }
                    }
                )
            }
        }
    }
}