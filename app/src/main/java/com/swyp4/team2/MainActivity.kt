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
import com.swyp4.team2.ui.login.LoginScreen
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
    // 화면 이동을 조종하는 리모컨 역할
    val navController = rememberNavController()

    // NavHost: 화면들이 담기는 빈 상자.
    // startDestination = "login" 으로 설정해서 켜자마자 로그인 화면이 뜨게 합니다.
    NavHost(navController = navController, startDestination = "login") {

        // 1. 로그인 화면
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

        // 2. 온보딩 화면 (아직 안 만들었으니 임시 글자만 띄움)
        composable("onboarding") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "온보딩 화면 스크린 (개발 예정!)")
            }
        }

        // 3. 메인 화면 (나중에 네비게이션 바 4개 들어갈 곳)
        composable("main") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "메인 화면 스크린 (개발 예정!)")
            }
        }
    }
}