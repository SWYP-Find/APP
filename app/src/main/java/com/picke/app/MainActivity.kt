package com.picke.app

import ScenarioScreen
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.ads.MobileAds
import com.kakao.sdk.common.util.Utility
import com.picke.app.ui.alarm.AlarmScreen
import com.picke.app.ui.comment.CommentScreen
import com.picke.app.ui.theme.SwypAppTheme
import com.picke.app.ui.login.LoginScreen
import com.picke.app.ui.main.BottomNavItem
import com.picke.app.ui.main.MainScreen
import com.picke.app.ui.onboarding.OnboardingScreen
import com.picke.app.ui.my.setting.alarm.SettingAlarmScreen
import com.picke.app.ui.my.setting.policy.PrivacyPolicyScreen
import com.picke.app.ui.my.setting.policy.TermsOfServiceScreen
import com.picke.app.ui.my.setting.profile.SettingProfileScreen
import com.picke.app.ui.perspective.PerspectiveScreen
import com.picke.app.ui.recommend.RecommendScreen
import com.picke.app.ui.routing.BattleRoutingScreen
import com.picke.app.ui.splash.SplashScreen
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.todaybattle.TodayBattleScreen
import com.picke.app.ui.vote.VoteRoute
import com.picke.app.ui.vote.VoteType
import dagger.hilt.android.AndroidEntryPoint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import androidx.activity.SystemBarStyle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowInsetsControllerCompat
import com.picke.app.ui.my.philosopher.PhilosopherTypeScreen
import com.picke.app.ui.splash.SplashUiState
import com.picke.app.ui.splash.SplashViewModel
import com.picke.app.util.DeepLinkEvent
import com.picke.app.util.DeepLinkManager
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }

        // 🌟 1. 기존에 길게 있던 딥링크 if문을 지우고, 이 한 줄로 교체합니다!
        handleDeepLink(intent)

        MobileAds.initialize(this) {} // 광고 sdk 초기화
        enableEdgeToEdge() // 앱의 콘텐츠를 화면 끝에서부터 끝까지 꽉 채우기

        setContent {
            SwypAppTheme {
                AppNavigation()
            }
        }
    }

    // 앱이 백그라운드에 켜져 있을 때 딥링크를 낚아채는 함수 추가!
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    // 웹 링크(picke.store)와 카카오 링크(kakaolink)를 모두 처리하는 만능 함수 추가!
    private fun handleDeepLink(intent: Intent?) {
        val uri = intent?.data ?: return
        Log.d("DeepLinkFlow", "딥링크 감지됨: $uri")

        var targetBattleId: String? = null
        var targetReportId: String? = null

        // 1) 일반 웹 링크인 경우 (인스타, 웹사이트)
        if (uri.host == "picke.store") {
            if (uri.path?.startsWith("/report/") == true) targetReportId = uri.lastPathSegment
            if (uri.path?.startsWith("/battle/") == true) targetBattleId = uri.lastPathSegment
        }
        // 2) 카카오톡 공유 링크인 경우 (호스트가 kakaolink로 들어옴)
        else if (uri.host == "kakaolink") {
            targetReportId = uri.getQueryParameter("reportId")
            targetBattleId = uri.getQueryParameter("battleId")
        }

        // 수첩(DeepLinkManager)에 목적지 저장
        if (targetReportId != null) DeepLinkManager.pendingReportId = targetReportId
        if (targetBattleId != null) DeepLinkManager.pendingBattleId = targetBattleId

        // 앱이 이미 켜져있는 상태라면 즉시 화면 이동 이벤트를 발사!
        if (targetReportId != null) DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToReport(targetReportId))
        if (targetBattleId != null) DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToBattle(targetBattleId))

        // 네비게이션이 무한 반복되지 않도록 쓴 링크는 지워주기
        intent.data = null
    }
}

fun getKeyHash(context: Context) {
    try {
        val info: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
        }

        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.signingInfo?.apkContentsSigners
        } else {
            @Suppress("DEPRECATION")
            info.signatures
        }

        if (signatures != null) {
            for (signature in signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT)

                Log.d("KeyHashFlow", "현재 기기의 키 해시값: $keyHash")
            }
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("KeyHashFlow", "패키지 이름을 찾을 수 없습니다.", e)
    } catch (e: NoSuchAlgorithmException) {
        Log.e("KeyHashFlow", "해당 알고리즘을 사용할 수 없습니다.", e)
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation() {
    val rootNavController = rememberNavController()
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

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
                    is DeepLinkEvent.GoToBattle -> {
                        rootNavController.navigate(AppRoute.BattleRouting.createRoute(event.battleId))
                    }
                    is DeepLinkEvent.GoToReport -> {
                        rootNavController.navigate(AppRoute.OtherPhilosopher.createRoute(event.reportId))
                    }
                }
            }
        }

        NavHost(
            navController = rootNavController,
            startDestination = AppRoute.Splash.route,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            // 스플래시 화면
            composable(AppRoute.Splash.route) {
                SplashScreen(
                    onNavigateToLogin = {
                        rootNavController.navigate(AppRoute.Login.route) {
                            popUpTo(AppRoute.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToMain = {
                        val pendingReport = DeepLinkManager.pendingReportId
                        val pendingBattle = DeepLinkManager.pendingBattleId

                        when {
                            pendingReport != null -> {
                                rootNavController.navigate(AppRoute.Main.route) {
                                    popUpTo(AppRoute.Splash.route) { inclusive = true }
                                }
                                rootNavController.navigate(AppRoute.OtherPhilosopher.createRoute(pendingReport))
                            }
                            pendingBattle != null -> {
                                rootNavController.navigate(AppRoute.Main.route) {
                                    popUpTo(AppRoute.Splash.route) { inclusive = true }
                                }
                                rootNavController.navigate(AppRoute.BattleRouting.createRoute(pendingBattle))
                            }
                            else -> {
                                rootNavController.navigate(AppRoute.Main.route) {
                                    popUpTo(AppRoute.Splash.route) { inclusive = true }
                                }
                            }
                        }
                        // rootNavController.navigate(AppRoute.Main.route) {
                        // popUpTo(AppRoute.Splash.route) { inclusive = true }
                        // }
                        //rootNavController.navigate(AppRoute.Scenario.createRoute("79"))
                        //rootNavController.navigate(AppRoute.Perspective.createRoute("79"))
                    },
                    onNavigateToOnboarding = {
                        rootNavController.navigate(AppRoute.Onboarding.route) {
                            popUpTo(AppRoute.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToOtherPhilosopher = { reportId ->
                        rootNavController.navigate(AppRoute.OtherPhilosopher.createRoute(reportId)) {
                            popUpTo(AppRoute.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToBattle = { battleId ->
                        rootNavController.navigate(AppRoute.BattleRouting.createRoute(battleId)) {
                            popUpTo(AppRoute.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // 온보딩 화면
            composable(AppRoute.Onboarding.route) {
                OnboardingScreen(
                    onNavigateToLogin = {
                        rootNavController.navigate(AppRoute.Login.route) {
                            popUpTo(AppRoute.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // 로그인 화면
            composable(
                route = AppRoute.Login.route,
                exitTransition = {
                    fadeOut(animationSpec = tween(100))
                }
            ) {
                LoginScreen(
                    onNavigateToMain = {
                        val pendingReport = DeepLinkManager.pendingReportId
                        val pendingBattle = DeepLinkManager.pendingBattleId

                        // 1. 무조건 메인 화면을 깝니다.
                        rootNavController.navigate(AppRoute.Main.route) {
                            popUpTo(AppRoute.Login.route) { inclusive = true }
                        }

                        // 2. 딥링크 목적지가 있다면 0.1초 쉬고 얹어줍니다!
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

            // 메인 화면
            composable(
                route = AppRoute.Main.route,
            ) {
                MainScreen(rootNavController = rootNavController)
            }

            // 배틀 라우팅
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

            // 오늘의 배틀 화면
            composable(BottomNavItem.TodayBattle.route){
                TodayBattleScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onEnterBattle = { battleId ->
                        rootNavController.navigate(AppRoute.BattleRouting.createRoute(battleId))
                    }
                )
            }

            // 알림 화면
            composable(AppRoute.Alarm.route) {
                AlarmScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    }
                )
            }

            // 알림 설정 화면
            composable(AppRoute.SettingAlarm.route) {
                SettingAlarmScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    }
                )
            }

            // 프로필 편집 화면
            composable(AppRoute.SettingProfile.route) {
                SettingProfileScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    }
                )
            }

            // 사전 투표 화면
            composable(
                route = AppRoute.PreVote.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId") ?: ""

                VoteRoute(
                    voteType = VoteType.PRE,
                    onBackClick = {
                        val prevRoute = rootNavController.previousBackStackEntry?.destination?.route

                        // 만약 바닥에 깔린 화면이 없거나, 스플래시 화면이라면? -> 무조건 메인으로 강제 이동
                        if (prevRoute == null || prevRoute == AppRoute.Splash.route) {
                            rootNavController.navigate(AppRoute.Main.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            // 바닥에 메인이나 다른 화면이 정상적으로 잘 깔려있다면 원래대로 뒤로 가기
                            rootNavController.popBackStack()
                        }
                    },
                    onVoteSubmit = { submittedBattleId ->
                        rootNavController.navigate(AppRoute.Scenario.createRoute(submittedBattleId)) {
                            popUpTo(AppRoute.PreVote.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // 시나리오 (TTS) 화면
            composable(
                route = AppRoute.Scenario.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val contentId = backStackEntry.arguments?.getString("battleId") ?: ""

                ScenarioScreen(
                    battleId = contentId,
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onNextClick = {
                        rootNavController.navigate(AppRoute.PostVote.createRoute(contentId)) {
                            popUpTo(AppRoute.Scenario.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // 사후 투표 화면
            composable(
                route = AppRoute.PostVote.route,
                arguments = listOf(navArgument("battleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId") ?: ""

                VoteRoute(
                    voteType = VoteType.POST,
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onVoteSubmit = { submittedBattleId ->
                        rootNavController.navigate(AppRoute.Perspective.createRoute(submittedBattleId)) {
                            popUpTo(AppRoute.Main.route) { inclusive = false }
                        }
                    }
                )
            }

            // 관점 화면
            composable(
                route = AppRoute.Perspective.route,
                arguments = listOf(
                    navArgument("battleId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId") ?: ""

                PerspectiveScreen(
                    onBackClick = {
                        val prevRoute = rootNavController.previousBackStackEntry?.destination?.route
                        if (prevRoute == null || prevRoute == AppRoute.Splash.route || prevRoute == AppRoute.Login.route) {
                            rootNavController.navigate(AppRoute.Main.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            rootNavController.popBackStack()
                        }
                    },
                    onNextClick = { itemId->
                        rootNavController.navigate(AppRoute.Recommend.createRoute(itemId))
                    },
                    onMoreClick = { itemId ->
                        rootNavController.navigate(AppRoute.Comment.createRoute(itemId))
                    }
                )
            }

            // 답글 화면
            composable(
                route =
                    AppRoute.Comment.route,
                arguments = listOf(
                    navArgument("itemId") { type = NavType.StringType }
                )
            ){
                CommentScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    }
                )
            }

            // 추천 화면
            composable(
                route = AppRoute.Recommend.route,
                arguments = listOf(
                    navArgument("battleId") { type = NavType.StringType }
                )
            ) {
                RecommendScreen(
                    onCloseClick = {
                        rootNavController.popBackStack(AppRoute.Main.route, inclusive = false)
                    },
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onItemClick = {clickedBattleId ->
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
                            rootNavController.navigate(AppRoute.Main.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            rootNavController.popBackStack()
                        }
                    },
                    onGoToSplashClick = {
                        DeepLinkManager.pendingReportId = null
                        rootNavController.navigate(AppRoute.Splash.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

        }
    }
}