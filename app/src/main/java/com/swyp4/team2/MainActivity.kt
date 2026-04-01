package com.swyp4.team2

import ScenarioScreen
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
import androidx.navigation.navDeepLink
import com.google.android.gms.ads.MobileAds
import com.kakao.sdk.common.util.Utility
import com.swyp4.team2.ui.alarm.AlarmScreen
import com.swyp4.team2.ui.comment.CommentScreen
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.login.LoginScreen
import com.swyp4.team2.ui.main.BottomNavItem
import com.swyp4.team2.ui.main.MainScreen
import com.swyp4.team2.ui.onboarding.OnboardingScreen
import com.swyp4.team2.ui.my.setting.alarm.SettingAlarmScreen
import com.swyp4.team2.ui.my.setting.policy.PrivacyPolicyScreen
import com.swyp4.team2.ui.my.setting.policy.TermsOfServiceScreen
import com.swyp4.team2.ui.my.setting.profile.SettingProfileScreen
import com.swyp4.team2.ui.perspective.PerspectiveScreen
import com.swyp4.team2.ui.recommend.RecommendScreen
import com.swyp4.team2.ui.splash.SplashScreen
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.todaybattle.TodayBattleScreen
import com.swyp4.team2.ui.vote.VoteRoute
import com.swyp4.team2.ui.vote.VoteScreen
import com.swyp4.team2.ui.vote.VoteType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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
                        // rootNavController.navigate(AppRoute.Scenario.createRoute("81"))
                        rootNavController.navigate(AppRoute.Perspective.createRoute("79"))
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

            // 오늘의 배틀 화면
            composable(BottomNavItem.TodayBattle.route){
                TodayBattleScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    },
                    onEnterBattle = { battleId ->
                        rootNavController.navigate(AppRoute.Scenario.createRoute(battleId))
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
                        rootNavController.navigate(AppRoute.Recommend.route)
                    },
                    onMoreClick = { itemId ->
                        rootNavController.navigate(AppRoute.Comment.createRoute(itemId))
                    }
                )
            }

            // 답글 화면
            composable(
                route = AppRoute.Comment.route,
                arguments = listOf(
                    navArgument("itemId") { type = NavType.StringType }
                )
            ){ backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""

                CommentScreen(
                    onBackClick = {
                        rootNavController.popBackStack()
                    }
                )
            }

            // 추천 화면
            composable(AppRoute.Recommend.route) {
                RecommendScreen(
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