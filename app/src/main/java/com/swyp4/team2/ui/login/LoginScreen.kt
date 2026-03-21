package com.swyp4.team2.ui.login

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomButton
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary300
import com.swyp4.team2.ui.theme.SwypTheme
import com.kakao.sdk.auth.AuthCodeClient
import com.swyp4.team2.BuildConfig

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToMain: ()->Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.serverAuthCode?.let { authCode ->
                Log.d("LoginFlow", "▶️ 구글 런처: 인가 코드 빼오기 성공! ViewModel로 넘깁니다.")
                viewModel.handleSocialLoginSuccess("google", authCode) // 백엔드 스펙에 맞게 소문자로 변경 추천
            } ?: Log.e("LoginFlow", "▶️ 구글 런처: 엥? serverAuthCode가 null입니다!")
        } catch (e: ApiException) {
            Log.e("LoginFlow", "▶️ 구글 런처: 구글 SDK 로그인 실패 (StatusCode: ${e.statusCode})", e)
        }
    }
    LaunchedEffect(uiState){
        when (val state = uiState) {
            is LoginUiState.Success -> {
                Log.d("LoginFlow", "✅ 화면 이동: 메인(또는 온보딩)으로 넘어갑니다! (신규 유저: ${state.isNewUser})")
                onNavigateToMain()
            }
            is LoginUiState.Error -> {
                Log.d("LoginFlow", "❌ 에러 발생: ${state.message}")
            }
            else -> { }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF893825)),
        contentAlignment = Alignment.Center
    ){
        // 중앙: 중앙 텍스트 + 로고
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = SwypTheme.typography.h4SemiBold,
                color = Primary300,
                text = stringResource(R.string.login_your_think)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_login_logo),
                contentDescription = "Pické 로고",
                modifier = Modifier.size(width = 320.dp, height = 120.dp)
            )
            Spacer(modifier = Modifier.height(72.dp))
        }

        // 하단: 소셜 로그인 버튼
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO 로딩 디자인?
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (uiState is LoginUiState.Error) {
                Text(
                    text = (uiState as LoginUiState.Error).message,
                    color = Color(0xFFFF6B6B),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // 카카오 로그인 버튼
            CustomButton(
                text = stringResource(R.string.login_with_kakao),
                onClick = {
                    // onNavigateToMain()
                    Log.d("LoginFlow", "👆 카카오 로그인 버튼 클릭!")
                    loginWithKakaoForAuthCode(context) { token ->
                        Log.d("LoginFlow", "▶️ 카카오 헬퍼: 인가 코드 받기 성공! ViewModel로 넘깁니다.")
                        viewModel.handleSocialLoginSuccess("kakao", token)
                    }
                },
                backgroundColor = Color(0xFFFEE500),
                textColor = Gray900,
                iconResId = R.drawable.ic_kakao
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 구글 로그인 버튼
            CustomButton(
                text = stringResource(R.string.login_with_google),
                onClick = {
                    // onNavigateToMain()
                    Log.d("LoginFlow", "👆 구글 로그인 버튼 클릭!")
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestServerAuthCode(BuildConfig.GOOGLE_WEB_CLIENT_ID) // 백엔드용 클라이언트 ID
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
                },
                backgroundColor = Color.White,
                textColor = Gray900,
                iconResId = R.drawable.ic_google
            )
        }
    }
}



/*private fun getGoogleSignInIntent(context: Context): Intent {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        // 백엔드 개발자에게 받은 구글 웹 클라이언트 ID를 여기에 넣어야 합니다!
        .requestIdToken("여기에_구글_웹_클라이언트_ID_입력.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    return googleSignInClient.signInIntent
}*/


// 카카오 SDK 실행 헬퍼 함수
// 카카오 '인가 코드(Auth Code)'를 받아오는 함수
// 헬퍼 함수
private fun loginWithKakaoForAuthCode(context: Context, onSuccess: (String) -> Unit) {
    val callback: (String?, Throwable?) -> Unit = { authCode, error ->
        if (error != null) {
            Log.e("LoginFlow", "카카오 계정 로그인 실패", error)
        } else if (authCode != null) {
            onSuccess(authCode)
        }
    }

    if (AuthCodeClient.instance.isKakaoTalkLoginAvailable(context)) {
        AuthCodeClient.instance.authorizeWithKakaoTalk(context) { authCode, error ->
            if (error != null) {
                Log.e("LoginFlow", "카카오톡으로 로그인 실패", error)
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Log.d("LoginFlow", "사용자가 카카오 로그인 취소함")
                    return@authorizeWithKakaoTalk
                }
                AuthCodeClient.instance.authorizeWithKakaoAccount(context, callback = callback)
            } else if (authCode != null) {
                onSuccess(authCode)
            }
        }
    } else {
        AuthCodeClient.instance.authorizeWithKakaoAccount(context, callback = callback)
    }
}
