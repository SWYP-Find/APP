package com.swyp4.team2

import ScenarioScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.navigation.navDeepLink
import com.google.android.gms.ads.MobileAds
import com.kakao.sdk.common.util.Utility
import com.swyp4.team2.ui.alarm.AlarmScreen
import com.swyp4.team2.ui.curation.CurationScreen
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.login.LoginScreen
import com.swyp4.team2.ui.main.MainScreen
import com.swyp4.team2.ui.my.philosopher.OtherPhilosopherDetailScreen
import com.swyp4.team2.ui.onboarding.OnboardingScreen
import com.swyp4.team2.ui.my.setting.alarm.SettingAlarmScreen
import com.swyp4.team2.ui.my.setting.policy.PrivacyPolicyScreen
import com.swyp4.team2.ui.my.setting.policy.TermsOfServiceScreen
import com.swyp4.team2.ui.my.setting.profile.SettingProfileScreen
import com.swyp4.team2.ui.perspective.PerspectiveDetailScreen
import com.swyp4.team2.ui.perspective.PerspectiveScreen
import com.swyp4.team2.ui.splash.SplashScreen
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.vote.VoteRoute
import com.swyp4.team2.ui.vote.VoteScreen
import com.swyp4.team2.ui.vote.VoteType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", "내 키 해시값: $keyHash")

        MobileAds.initialize(this) {}
        enableEdgeToEdge()
        setContent {
            SwypAppTheme {
                AppNavigation()
            }
        }
    }
}

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
            modifier = Modifier.fillMaxSize()
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
                        rootNavController.navigate(AppRoute.PostVote.createRoute(submittedBattleId))
                        // rootNavController.navigate(AppRoute.Scenario.createRoute(battleId))
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
                        rootNavController.navigate(AppRoute.PostVote.createRoute(contentId))
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
                        // rootNavController.popBackStack()
                    },
                    onNextClick = {
                        rootNavController.navigate(AppRoute.Curation.route)
                    },
                    onMoreClick = { submittedBattleId ->
                        rootNavController.navigate(AppRoute.PerspectiveDetail.createRoute(submittedBattleId))
                    }
                )
            }

            // 답글 화면
            composable(
                route = AppRoute.PerspectiveDetail.route,
                arguments = listOf(
                    navArgument("battleId") { type = NavType.StringType }
                )
            ){ backStackEntry ->
                val battleId = backStackEntry.arguments?.getString("battleId") ?: ""

                PerspectiveDetailScreen(
                    // itemId = itemId,
                    onBackClick = {
                        rootNavController.popBackStack()
                    }
                )
            }

            // 큐레이션 화면
            composable(AppRoute.Curation.route) {
                CurationScreen(
                    onCloseClick = {
                        rootNavController.popBackStack(AppRoute.Main.route, inclusive = false)
                    },
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onItemClick = {
                        rootNavController.navigate(AppRoute.PreVote.route)
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
            }
        }
    }
}