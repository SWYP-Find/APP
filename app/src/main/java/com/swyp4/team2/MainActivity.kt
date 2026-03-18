package com.swyp4.team2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swyp4.team2.ui.alarm.AlarmScreen
import com.swyp4.team2.ui.battle.BattleScreen
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.debate.DebateScreen
import com.swyp4.team2.ui.login.LoginScreen
import com.swyp4.team2.ui.main.BottomNavItem
import com.swyp4.team2.ui.main.MainScreen
import com.swyp4.team2.ui.onboarding.OnboardingScreen
import com.swyp4.team2.ui.my.setting.SettingScreen
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

        //val keyHash = Utility.getKeyHash(this)
        //Log.d("Hash", "내 키 해시값: $keyHash")

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

    NavHost(navController = rootNavController, startDestination = AppRoute.Main.route) {
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
                }
            )
        }

        // 로그인 화면
        composable(AppRoute.Login.route) {
            LoginScreen(
                onNavigateToMain = {
                    // 카카오 로그인 성공 시! 온보딩 화면으로 이동시킵니다.
                    rootNavController.navigate(AppRoute.Onboarding.route) {
                        // 사용자가 뒤로가기를 눌렀을 때 다시 로그인 화면으로 오지 않게 스택에서 없애버림
                        popUpTo(AppRoute.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 온보딩 화면
        composable(AppRoute.Onboarding.route) {
            OnboardingScreen(
                onNavigateToMain = {
                    rootNavController.navigate(AppRoute.Main.route){
                         popUpTo(AppRoute.Onboarding.route) {inclusive=true}
                     }
                }
            )
        }

        // 메인 화면
        composable(AppRoute.Main.route) {
            MainScreen(rootNavController = rootNavController)
        }

        // tts 화면
        composable(AppRoute.Debate.route){
            DebateScreen()
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
                item = dummyVoteItem,
                onBackClick = {
                    rootNavController.popBackStack()
                },
                onVoteSubmit = {
                    rootNavController.navigate(AppRoute.Debate.route)
                }
            )
        }

        // 사후 투표 화면
        composable(AppRoute.PostVote.route) {
            VoteScreen(
                voteType = VoteType.POST,
                item = dummyVoteItem,
                onBackClick = {
                    rootNavController.popBackStack()
                },
                onVoteSubmit = { /* 결과 화면으로 이동 */ }
            )
        }
    }
}