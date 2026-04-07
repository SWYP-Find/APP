package com.picke.app

import ScenarioScreen
import android.content.Context
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsControllerCompat
import com.picke.app.ui.splash.SplashUiState
import com.picke.app.ui.splash.SplashViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }

        // val keyHash = Utility.getKeyHash(this)
        // Log.d("Hash", "내 키 해시값: $keyHash")

        MobileAds.initialize(this) {} // 광고 sdk 초기화

        enableEdgeToEdge() // 앱의 콘텐츠를 화면 끝에서부터 끝까지 꽉 채우기
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

        setContent {
            SwypAppTheme {
                AppNavigation()
            }
        }
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Beige200
    ) {
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
                        rootNavController.navigate(AppRoute.Main.route) {
                            popUpTo(AppRoute.Splash.route) { inclusive = true }
                        }
                        //rootNavController.navigate(AppRoute.Scenario.createRoute("79"))
                        //rootNavController.navigate(AppRoute.Perspective.createRoute("79"))
                    },
                    onNavigateToOnboarding = {
                        rootNavController.navigate(AppRoute.Onboarding.route) {
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
                        rootNavController.navigate(AppRoute.Main.route) {
                            popUpTo(AppRoute.Login.route) { inclusive = true }
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
            ) {
                BattleRoutingScreen(
                    onNavigateToPreVote = { id ->
                        rootNavController.navigate(AppRoute.PreVote.createRoute(id)) {
                            popUpTo(AppRoute.BattleRouting.route) { inclusive = true }
                        }
                    },
                    onNavigateToPerspective = { id ->
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
                        rootNavController.popBackStack()
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
                            // ✨ 핵심 1: 사후투표까지 완료했으므로 스택 대청소!
                            // Main(처음 페이지) 화면 위에 쌓여있던 모든 이전 꼬리들(이전 관점, 추천 등)을 싹 다 날리고,
                            // Main 화면 위에 '새로운 관점 화면' 딱 하나만 남깁니다.
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
                        // ✨ 핵심 2: 주석 해제!
                        // 위에서 PostVote가 스택을 싹 청소해줬기 때문에,
                        // 여기서 뒤로가기를 누르면 무조건 맨 처음 페이지(Main)로 돌아갑니다.
                        rootNavController.popBackStack()
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

            /*composable(
                route = AppRoute.OtherPhilosopher.route,
                arguments = listOf(
                    navArgument("reportId") { type = NavType.StringType }
                ),
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "kakao${BuildConfig.KAKAO_DEBUG_APPKEY}://kakaolink?reportId={reportId}"
                    }
                )
            ) { backStackEntry ->
                val reportId = backStackEntry.arguments?.getString("reportId") ?: ""

                OtherPhilosopherDetailScreen(
                    reportId = reportId,
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onGoToSplashClick = {
                        rootNavController.navigate(AppRoute.Splash.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }*/
        }
    }
}