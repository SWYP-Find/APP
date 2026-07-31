package com.picke.presentation.ui.login

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.AuthCodeClient
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.analytics.OnboardingStep
import com.picke.presentation.analytics.rememberAnalyticsTracker
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.TermsOfServiceBottomSheet
import com.picke.presentation.ui.theme.SwypAppTheme
import com.picke.presentation.ui.theme.SwypTheme

private const val TAG = "LoginScreen_Picke"

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToMain: (isNewUser: Boolean) -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val analyticsTracker = rememberAnalyticsTracker()
    var showTermsSheet by rememberSaveable { mutableStateOf(false) }
    var pendingIsNewUser by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        analyticsTracker.trackOnboardingStep(OnboardingStep.LOGIN_SHOWN)
    }

    // 뒤로가기 -> 앱 종료
    BackHandler {
        (context as? Activity)?.finish()
    }

    // 구글 로그인 런처
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.serverAuthCode?.let { authCode ->
                if (BuildConfig.DEBUG) Log.d(TAG, "[SDK] 구글 인가 코드 획득 성공 -> ViewModel 전달")
                viewModel.handleSocialLoginSuccess("google", authCode)
            } ?: Log.e(TAG, "[ERROR] 구글 인가 코드가 null입니다.")
        } catch (e: ApiException) {
            val hint = when (e.statusCode) {
                10 -> "DEVELOPER_ERROR: 클라이언트 ID 타입 오류 또는 SHA-1 미등록"
                12500 -> "Google Play Services 미지원 기기"
                12501 -> "사용자가 로그인 취소"
                7 -> "NETWORK_ERROR: 네트워크 연결 확인 필요"
                else -> "알 수 없는 오류"
            }
            Log.e(TAG, "[ERROR] 구글 SDK 로그인 실패 (Code: ${e.statusCode} / $hint)", e)
            Toast.makeText(context, "구글 로그인에 실패했습니다. \n다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                if (state.needsTermsAgreement) {
                    Log.i(TAG, "[NAV] 로그인 성공 -> 약관 동의 필요, 바텀시트 표시 (신규 유저: ${state.isNewUser})")
                    pendingIsNewUser = state.isNewUser
                    showTermsSheet = true
                    analyticsTracker.trackOnboardingStep(OnboardingStep.TERMS_SHOWN)
                } else {
                    Log.i(TAG, "[NAV] 로그인 성공 -> 메인으로 이동 (신규 유저: ${state.isNewUser})")
                    onNavigateToMain(state.isNewUser)
                }
            }

            is LoginUiState.Error -> {
                Log.e(TAG, "[ERROR] 로그인 실패: ${state.message}")
                Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }

            else -> {}
        }
    }

    if (showTermsSheet) {
        TermsOfServiceBottomSheet(
            onConfirm = {
                viewModel.markTermsAgreed()
                analyticsTracker.trackOnboardingStep(OnboardingStep.TERMS_AGREED)
                showTermsSheet = false
                Log.i(TAG, "[NAV] 약관 동의 완료 -> 메인으로 이동 (신규 유저: $pendingIsNewUser)")
                onNavigateToMain(pendingIsNewUser)
            }
        )
    }

    LoginScreenContent(
        isLoading = uiState is LoginUiState.Loading,
        onKakaoClick = {
            analyticsTracker.trackOnboardingStep(OnboardingStep.KAKAO_START, method = "kakao")
            loginWithKakaoForAuthCode(context, viewModel) { token ->
                if (BuildConfig.DEBUG) Log.d(TAG, "[FLOW] 카카오 인가 코드 획득 완료 -> ViewModel 전달")
                viewModel.handleSocialLoginSuccess("kakao", token)
            }
        },
        onGoogleClick = {
            Log.d(TAG, "[FLOW] 구글 로그인 버튼 클릭")
            if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isEmpty()) {
                Log.e(TAG, "[ERROR] GOOGLE_WEB_CLIENT_ID가 설정되지 않았습니다. local.properties를 확인하세요.")
                Toast.makeText(context, "로그인 설정 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestServerAuthCode(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInClient.signOut().addOnCompleteListener {
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
                }
            }
        },
    )
}

@Composable
private fun LoginScreenContent(
    isLoading: Boolean,
    onKakaoClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SwypTheme.colors.backgroundSubtle),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = SwypTheme.typography.h4SemiBold,
                color = SwypTheme.colors.neutral200,
                text = stringResource(R.string.login_your_think)
            )
            Image(
                painter = painterResource(id = R.drawable.logo_picke),
                contentDescription = "Picke Logo",
                modifier = Modifier.size(width = 240.dp, height = 100.dp)
            )
            Spacer(modifier = Modifier.height(72.dp))
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = SwypTheme.colors.primaryDarkest)
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                CustomButton(
                    text = stringResource(R.string.login_with_kakao),
                    onClick = onKakaoClick,
                    backgroundColor = Color(0xFFFEE500),
                    textColor = SwypTheme.colors.textPrimary,
                    iconResId = R.drawable.logo_login_kakao
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    text = stringResource(R.string.login_with_google),
                    onClick = onGoogleClick,
                    backgroundColor = Color.White,
                    textColor = SwypTheme.colors.textPrimary,
                    iconResId = R.drawable.logo_login_google
                )
            }
        }
    }
}

@Preview(showSystemUi = true, name = "Login - 기본")
@Composable
private fun LoginScreenPreview() {
    SwypAppTheme {
        LoginScreenContent(isLoading = false)
    }
}

@Preview(showSystemUi = true, name = "Login - 로딩중")
@Composable
private fun LoginScreenLoadingPreview() {
    SwypAppTheme {
        LoginScreenContent(isLoading = true)
    }
}

// 카카오 SDK 실행 헬퍼 함수
private fun loginWithKakaoForAuthCode(
    context: Context,
    viewModel: LoginViewModel,
    onSuccess: (String) -> Unit
) {
    val callback: (String?, Throwable?) -> Unit = { authCode, error ->
        if (error != null) {
            Log.e(TAG, "[ERROR] 카카오 계정 로그인 실패", error)
            Toast.makeText(context, "카카오 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        } else if (authCode != null) {
            onSuccess(authCode)
        }
    }

    if (AuthCodeClient.instance.isKakaoTalkLoginAvailable(context)) {
        Log.d(TAG, "[SDK] 카카오톡 앱으로 로그인 시도")
        AuthCodeClient.instance.authorizeWithKakaoTalk(context) { authCode, error ->
            if (error != null) {
                Log.w(TAG, "[SDK] 카카오톡 앱 로그인 실패 -> 계정 로그인 시도")
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Log.d(TAG, "[STATE] 유저가 카카오 로그인을 취소함")
                    viewModel.resetState()
                    return@authorizeWithKakaoTalk
                }
                // AuthCodeClient.instance.authorizeWithKakaoAccount(context, callback = callback)
                AuthCodeClient.instance.authorizeWithKakaoAccount(
                    context,
                    prompts = listOf(Prompt.LOGIN),
                    callback = callback
                )
            } else if (authCode != null) {
                onSuccess(authCode)
            }
        }
    } else {
        Log.d(TAG, "[SDK] 카카오 계정(웹)으로 로그인 시도")
        // AuthCodeClient.instance.authorizeWithKakaoAccount(context, callback = callback)
        AuthCodeClient.instance.authorizeWithKakaoAccount(
            context,
            prompts = listOf(Prompt.LOGIN),
            callback = callback
        )
    }
}