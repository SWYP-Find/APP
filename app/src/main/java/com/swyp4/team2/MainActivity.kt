package com.swyp4.team2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.kakao.sdk.common.util.Utility
import com.swyp4.team2.ui.debate.DebateScreen
import com.swyp4.team2.ui.login.LoginScreen
import com.swyp4.team2.ui.main.MainScreen
import com.swyp4.team2.ui.onboarding.OnboardingScreen
import com.swyp4.team2.ui.splash.SplashScreen
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
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        // 스플래시 화면
        composable("splash"){
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login"){
                        popUpTo("splash") {inclusive=true}
                    }
                },
                onNavigateToMain = {
                    navController.navigate("main"){
                        popUpTo("splash") {inclusive=true}
                    }
                }
            )
        }

        // 로그인 화면
        composable("login") {
            LoginScreen(
                onNavigateToMain = {
                    // 카카오 로그인 성공 시! 온보딩 화면으로 이동시킵니다.
                    navController.navigate("onboarding") {
                        // 사용자가 뒤로가기를 눌렀을 때 다시 로그인 화면으로 오지 않게 스택에서 없애버림
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 온보딩 화면
        composable("onboarding") {
            OnboardingScreen(
                onNavigateToMain = {
                     navController.navigate("main"){
                         popUpTo("onboarding") {inclusive=true}
                     }
                }
            )
        }

        // 메인 화면
        composable("main") {
            MainScreen()
        }

        composable("debate"){
            DebateScreen()
        }
    }
}