package com.swyp4.team2.ui.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToMain: ()->Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // 구글 로그인 결과를 처리하는 Launcher 설정
    /*val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            // 구글은 보통 accessToken 대신 idToken을 서버로 보냅니다.
            account.idToken?.let { token ->
                // ViewModel에 구글 로그인 성공 처리 함수가 필요합니다.
                // 이전에 우리가 하나로 통합했던 viewModel.loginWithSocialToken("GOOGLE", token) 형태를 추천해요!
                viewModel.handleGoogleLoginSuccess(token)
            }
        } catch (e: ApiException) {
            Log.e("GoogleLogin", "구글 로그인 실패: ${e.statusCode}", e)
        }
    }*/

    LaunchedEffect(uiState){
        if(uiState is LoginUiState.Success){
            onNavigateToMain()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("SWYP0402", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    loginWithKakao(context){ token ->
                        viewModel.handleSocialLoginSuccess("KAKAO", token)
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFFEE500)),
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ){
                Text("카카오로 로그인하기", color = Color.Black)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // val intent = getGoogleSignInIntent(context)
                    // googleSignInLauncher.launch(intent)
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFF2F2F2)), // 구글 버튼용 밝은 회색
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ) {
                Text("구글로 로그인하기", color = Color.Black)
            }

            if(uiState is LoginUiState.Loading){
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            if(uiState is LoginUiState.Error){
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = (uiState as LoginUiState.Error).message, color = Color.Red)
            }
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
private fun loginWithKakao(context: Context, onSuccess: (String)->Unit){
    val callback: (OAuthToken?, Throwable?) -> Unit = {token, error ->
        if(error != null){
            Log.e("KakaoLogin", "카카오 로그인 실패", error)
        } else if(token != null){
            onSuccess(token.accessToken)
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)){ // 사용자의 폰에 카카오톡 앱이 설치되어 있을때
        UserApiClient.instance.loginWithKakaoTalk(context){ token, error ->
            if(error != null){
                Log.e("KakaoLogin", "카카오톡 로그인 실패", error)
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            } else if (token != null) {
                onSuccess(token.accessToken)
            }
        }
    } else { // 미설치
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }
}
