package com.picke.app

import ScenarioScreen
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.picke.app.analytics.ContentActionType
import com.picke.app.analytics.OnboardingStep
import com.picke.app.analytics.TrackScreenViews
import com.picke.app.analytics.rememberAnalyticsTracker
import com.picke.app.ui.component.NotificationPermissionBottomSheet
import com.picke.app.ui.component.TermsOfServiceBottomSheet
import com.picke.app.ui.alarm.AlarmScreen
import com.picke.app.ui.my.makebattle.MakeBattleScreen
import com.picke.app.ui.my.notice.NoticeEventScreen
import com.picke.app.ui.my.point.PointScreen
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
import com.picke.app.ui.battleentry.BattleRoutingScreen
import com.picke.app.ui.splash.SplashUiState
import com.picke.app.ui.splash.SplashViewModel
import com.picke.app.ui.theme.SwypTheme
import com.picke.app.ui.todaybattle.TodayBattleScreen
import com.picke.app.ui.vote.VoteRoute
import com.picke.app.ui.vote.VoteType
import com.picke.app.util.DeepLinkEvent
import com.picke.app.util.DeepLinkManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation(splashViewModel: SplashViewModel) {
    val rootNavController = rememberNavController()
    val uiState by splashViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val analyticsTracker = rememberAnalyticsTracker()

    // §2 화면 enum에 등재된 화면의 screen_view 자동 전송
    TrackScreenViews(rootNavController)

    var showNotificationSheet by remember { mutableStateOf(false) }
    var showTermsSheet by remember { mutableStateOf(false) }

    LaunchedEffect(showTermsSheet) {
        if (showTermsSheet) analyticsTracker.trackOnboardingStep(OnboardingStep.TERMS_SHOWN)
    }
    LaunchedEffect(showNotificationSheet) {
        if (showNotificationSheet) analyticsTracker.trackOnboardingStep(OnboardingStep.PERMISSION_ASKED)
    }

    val requestNotificationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* FCM 토큰 발급은 추후 연동 */ }

    fun checkAndShowNotificationSheet(isNewUser: Boolean) {
        if (!isNewUser) return                                  // 신규 가입자에게만
        // 신규 가입자는 이전에 물어본 적이 있든 없든, 알림 권한이 켜져 있든 아니든 무조건 안내 시트를 띄운다.
        showNotificationSheet = true
    }

    fun markNotificationPermissionAsked() {
        splashViewModel.markNotificationPermissionAsked()
    }

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
                if (state.needsTermsAgreement) showTermsSheet = true
            }
            is SplashUiState.NavigateToOtherPhilosopher -> {
                rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) }
                rootNavController.navigate(AppRoute.OtherPhilosopher.createRoute(state.reportId))
                if (state.needsTermsAgreement) showTermsSheet = true
            }
            is SplashUiState.NavigateToBattle -> {
                rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) }
                rootNavController.navigate(AppRoute.BattleRouting.createRoute(state.battleId))
                if (state.needsTermsAgreement) showTermsSheet = true
            }
            is SplashUiState.Loading -> { /* 가만히 스플래시 유지 */ }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SwypTheme.colors.backgroundBrand
    ) {
        LaunchedEffect(Unit) {
            DeepLinkManager.deepLinkEvent.collect { event ->
                // 스플래시 로딩이 끝날 때까지 대기 (NavigateToMain이 popUpTo(0)으로 백스택을 지우기 전에
                // DeepLink 화면으로 이동하면 스플래시 완료 시 덮어씌워지므로, 로딩 완료 후 이동)
                splashViewModel.uiState.first { it !is SplashUiState.Loading }
                kotlinx.coroutines.delay(150)

                rootNavController.navigate(AppRoute.Main.route) {
                    popUpTo(AppRoute.Main.route) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
                kotlinx.coroutines.delay(100)
                when (event) {
                    is DeepLinkEvent.GoToBattle -> rootNavController.navigate(AppRoute.BattleRouting.createRoute(event.battleId))
                    is DeepLinkEvent.GoToTodayBattle -> rootNavController.navigate(AppRoute.TodayBattle.createRoute(event.battleId))
                    is DeepLinkEvent.GoToReport -> rootNavController.navigate(AppRoute.OtherPhilosopher.createRoute(event.reportId))
                    is DeepLinkEvent.GoToAlarm -> rootNavController.navigate(AppRoute.Alarm.route)
                    is DeepLinkEvent.GoToPerspective -> {
                        if (event.commentId != null) {
                            rootNavController.navigate(AppRoute.Comment.createRoute(event.perspectiveId, event.commentId))
                        } else {
                            rootNavController.navigate(AppRoute.Perspective.createRoute(event.perspectiveId))
                        }
                    }
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
                    modifier = Modifier.fillMaxSize().background(SwypTheme.colors.primary),
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
                    onNavigateToMain = { isNewUser ->
                        val pendingReport = DeepLinkManager.pendingReportId
                        val pendingBattle = DeepLinkManager.pendingBattleId

                        analyticsTracker.trackOnboardingStep(OnboardingStep.HOME_ENTERED)
                        rootNavController.navigate(AppRoute.Main.route) {
                            popUpTo(AppRoute.Login.route) { inclusive = true }
                        }
                        checkAndShowNotificationSheet(isNewUser)

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
                MainScreen(
                    rootNavController = rootNavController,
                    isNotificationSheetPending = showNotificationSheet
                )
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

            composable(
                route = AppRoute.TodayBattle.route,
                arguments = listOf(
                    navArgument("battleId") { type = NavType.StringType; nullable = true; defaultValue = null }
                )
            ) { backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId")
                TodayBattleScreen(
                    initialBattleId = battleId,
                    onBackClick = { rootNavController.popBackStack() },
                    onNavigateToScenario = { id ->
                        rootNavController.navigate(AppRoute.Scenario.createRoute(id)) {
                            popUpTo(AppRoute.TodayBattle.route) { inclusive = true }
                        }
                    },
                    onNavigateToPerspective = { id ->
                        rootNavController.navigate(AppRoute.Perspective.createRoute(id)) {
                            popUpTo(AppRoute.TodayBattle.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(AppRoute.Alarm.route) {
                AlarmScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    onNavigateToPreVote = { battleId ->
                        rootNavController.navigate(AppRoute.PreVote.createRoute(battleId))
                    },
                    onNavigateToComment = { perspectiveId, commentId ->
                        rootNavController.navigate(AppRoute.Comment.createRoute(perspectiveId, commentId))
                    },
                    onNavigateToPoint = {
                        rootNavController.navigate(AppRoute.Point.route)
                    },
                    onNavigateToNotice = { noticeId ->
                        rootNavController.navigate(AppRoute.NoticeEvent.createRoute(noticeId))
                    },
                    onNavigateToTodayBattle = {
                        DeepLinkManager.pendingTab = BottomNavItem.TodayBattle.route
                        rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) }
                    }
                )
            }

            composable(
                route = AppRoute.NoticeEvent.route,
                arguments = listOf(navArgument("noticeId") { type = NavType.LongType; defaultValue = -1L })
            ) { backStackEntry ->
                val noticeId = backStackEntry.arguments?.getLong("noticeId")?.takeIf { it != -1L }
                NoticeEventScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    initialNoticeId = noticeId
                )
            }

            composable(AppRoute.Point.route) {
                PointScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    onNavigateToMakeBattle = {
                        rootNavController.navigate(AppRoute.MakeBattle.route)
                    }
                )
            }

            composable(AppRoute.MakeBattle.route) {
                MakeBattleScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    onNavigateToExplore = {
                        DeepLinkManager.pendingTab = BottomNavItem.Explore.route
                        rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) }
                    }
                )
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
                    },
                    onNavigateToExplore = {
                        DeepLinkManager.pendingTab = BottomNavItem.Explore.route
                        rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) }
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
                    },
                    onNavigateToExplore = {
                        DeepLinkManager.pendingTab = BottomNavItem.Explore.route
                        rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) }
                    }
                )
            }

            composable(
                route = AppRoute.Perspective.route,
                arguments = listOf(
                    navArgument("battleId") { type = NavType.StringType },
                    navArgument("commentId") { type = NavType.StringType; nullable = true; defaultValue = null }
                )
            ) { backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId") ?: ""
                val commentId = backStackEntry.arguments?.getString("commentId")
                PerspectiveScreen(
                    scrollToCommentId = commentId,
                    onBackClick = {
                        val prevRoute = rootNavController.previousBackStackEntry?.destination?.route
                        if (prevRoute == null || prevRoute == AppRoute.Splash.route || prevRoute == AppRoute.Login.route) {
                            rootNavController.navigate(AppRoute.Main.route) { popUpTo(0) { inclusive = true } }
                        } else {
                            rootNavController.popBackStack()
                        }
                    },
                    onNextClick = { itemId -> rootNavController.navigate(AppRoute.Recommend.createRoute(itemId)) },
                    onMoreClick = { itemId, firstOptionId -> rootNavController.navigate(AppRoute.Comment.createRoute(itemId, firstOptionId)) }
                )
            }

            composable(
                route = AppRoute.Comment.route,
                arguments = listOf(
                    navArgument("itemId") { type = NavType.StringType },
                    navArgument("firstOptionId") { type = NavType.LongType; defaultValue = 0L },
                    navArgument("commentId") { type = NavType.StringType; nullable = true; defaultValue = null }
                )
            ) { backStackEntry ->
                val commentId = backStackEntry.arguments?.getString("commentId")
                CommentScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    scrollToCommentId = commentId
                )
            }

            composable(
                route = AppRoute.Recommend.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) {
                RecommendScreen(
                    onCloseClick = {
                        analyticsTracker.trackContentAction(ContentActionType.BATTLE_RECOMMEND_CLOSE)
                        rootNavController.popBackStack(AppRoute.Main.route, inclusive = false)
                    },
                    onBackClick = { rootNavController.popBackStack() },
                    onItemClick = { clickedBattleId ->
                        analyticsTracker.trackContentAction(ContentActionType.BATTLE_CARD_TAP, clickedBattleId)
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

    if (showTermsSheet) {
        TermsOfServiceBottomSheet(
            onConfirm = {
                splashViewModel.markTermsAgreed()
                analyticsTracker.trackOnboardingStep(OnboardingStep.TERMS_AGREED)
                showTermsSheet = false
            }
        )
    }

    if (showNotificationSheet) {
        NotificationPermissionBottomSheet(
            onDismiss = {
                showNotificationSheet = false
                markNotificationPermissionAsked()
            },
            onAgree = {
                showNotificationSheet = false
                markNotificationPermissionAsked()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                // TODO: FCM 토큰 발급 API 연동
            },
            onDisagree = {
                showNotificationSheet = false
                markNotificationPermissionAsked()
            }
        )
    }
}