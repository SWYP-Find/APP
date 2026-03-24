package com.swyp4.team2

import ScenarioScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kakao.sdk.common.util.Utility
import com.swyp4.team2.ui.alarm.AlarmScreen
import com.swyp4.team2.ui.curation.CurationScreen
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.login.LoginScreen
import com.swyp4.team2.ui.main.MainScreen
import com.swyp4.team2.ui.onboarding.OnboardingScreen
import com.swyp4.team2.ui.my.setting.alarm.SettingAlarmScreen
import com.swyp4.team2.ui.my.setting.profile.SettingProfileScreen
import com.swyp4.team2.ui.splash.SplashScreen
import com.swyp4.team2.ui.vote.VoteScreen
import com.swyp4.team2.ui.vote.model.VoteType
import com.swyp4.team2.ui.vote.model.dummyVoteItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", "내 키 해시값: $keyHash")

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

    NavHost(
        navController = rootNavController,
        startDestination = AppRoute.Splash.route,
        modifier = Modifier.fillMaxSize()
    ) {
        // 스플래시 화면
        composable(AppRoute.Splash.route){
            SplashScreen(
                onNavigateToLogin = {
                    rootNavController.navigate(AppRoute.Login.route){
                        popUpTo(AppRoute.Splash.route) {inclusive=true}
                    }
                },
                onNavigateToMain = {
                    rootNavController.navigate(AppRoute.Main.route){
                        popUpTo(AppRoute.Splash.route) {inclusive=true}
                    }
                },
                onNavigateToOnboarding = {
                    rootNavController.navigate(AppRoute.Onboarding.route){
                        popUpTo(AppRoute.Splash.route) {inclusive=true}
                    }
                }
            )
        }

        // 온보딩 화면
        composable(AppRoute.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin = {
                    rootNavController.navigate(AppRoute.Login.route){
                        popUpTo(AppRoute.Onboarding.route) {inclusive=true}
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
            enterTransition = {
                fadeIn(animationSpec = tween(50)) + slideInVertically(initialOffsetY = { it / 10 })
            }
        ) {
            MainScreen(rootNavController = rootNavController)
        }

        // 알림 화면
        composable(AppRoute.Alarm.route){
            AlarmScreen(
                onBackClick = {
                    rootNavController.popBackStack()
                }
            )
        }

        // 알림 설정 화면
        composable(AppRoute.SettingAlarm.route){
            SettingAlarmScreen(
                onBackClick = {
                    rootNavController.popBackStack()
                }
            )
        }


        // 프로필 편집 화면
        composable(AppRoute.SettingProfile.route){
            SettingProfileScreen(
                onBackClick = {
                    rootNavController.popBackStack()
                }
            )
        }

        // 사전 투표 화면
        composable(AppRoute.PreVote.route) {
            VoteScreen(
                voteType = VoteType.PRE,
                uiModel = dummyVoteItem,
                onBackClick = {
                    rootNavController.popBackStack()
                },
                onVoteSubmit = {
                    rootNavController.navigate(AppRoute.Scenario.route)
                }
            )
        }

        // tts 화면
        composable(AppRoute.Scenario.route){
            ScenarioScreen(
                battleId = "",
                onBackClick = {
                    rootNavController.popBackStack()
                },
            )
        }

        // 사후 투표 화면
        composable(AppRoute.PostVote.route) {
            VoteScreen(
                voteType = VoteType.POST,
                uiModel = dummyVoteItem,
                onBackClick = {
                    rootNavController.popBackStack()
                },
                onVoteSubmit = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable(AppRoute.Curation.route){
            CurationScreen(
                onCloseClick = {
                    // TODO
                },
                onBackClick = {
                    rootNavController.popBackStack()
                },
                onItemClick = {
                    // 🔥 여기서 사전투표(PreVote) 혹은 투표 화면으로 이동!
                    rootNavController.navigate(AppRoute.PreVote.route)
                    // 나중에 API 연결 시 라우트 예시:
                    // rootNavController.navigate("pre_vote_route/$id")
                }
            )
        }
    }
}