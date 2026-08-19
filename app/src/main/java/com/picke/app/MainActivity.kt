package com.picke.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import com.picke.app.BuildConfig
import com.picke.app.data.local.TokenManager
import com.picke.app.notification.FCMService
import com.picke.app.ui.main.BottomNavItem
import com.picke.app.ui.splash.SplashUiState
import com.picke.app.ui.splash.SplashViewModel
import com.picke.app.ui.theme.SwypAppTheme
import com.picke.app.util.DeepLinkEvent
import com.picke.app.util.DeepLinkManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// FragmentActivity: 카카오 애드핏 앱 전환 팝업 광고(AdFitPopupAdDialogFragment)를 띄우려면
// supportFragmentManager가 필요해서 ComponentActivity에서 변경. 앱 테마가 AppCompat 테마가
// 아니라서(Theme.Material 계열) AppCompatActivity 대신 더 가벼운 FragmentActivity를 사용한다.
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject lateinit var tokenManager: TokenManager

    private val splashViewModel: SplashViewModel by viewModels()

    companion object {
        private const val TAG = "MainActivity_Picke"
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            splashViewModel.uiState.value is SplashUiState.Loading
        }

        if (BuildConfig.DEBUG) Log.d(TokenManager.TAG, "[LOCAL] 저장된 FCM 토큰: ${tokenManager.getFcmToken()}")

        handleFcmIntent(intent)
        handleDeepLink(intent)

        enableEdgeToEdge()

        setContent {
            SwypAppTheme {
                AppNavigation(splashViewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleFcmIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleFcmIntent(intent: Intent?) {
        if (intent == null) return
        val type = intent.getStringExtra(FCMService.EXTRA_FCM_TYPE)
            ?: intent.getStringExtra("type") ?: return
        val battleId = intent.getStringExtra(FCMService.EXTRA_FCM_BATTLE_ID)
            ?: intent.getStringExtra("battleId")
        val perspectiveId = intent.getStringExtra(FCMService.EXTRA_FCM_PERSPECTIVE_ID)
            ?: intent.getStringExtra("perspectiveId")
        val commentId = intent.getStringExtra(FCMService.EXTRA_FCM_COMMENT_ID)
            ?: intent.getStringExtra("commentId")

        Log.d("MainActivity", "FCM 알림 탭 - type: $type, battleId: $battleId, perspectiveId: $perspectiveId, commentId: $commentId")

        when (type) {
            FCMService.TYPE_BATTLE -> battleId?.let {
                DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToTodayBattle(it))
            }
            FCMService.TYPE_COMMENT -> perspectiveId?.let {
                DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToPerspective(it, commentId))
            }
            FCMService.TYPE_ALARM -> {
                DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToAlarm)
            }
            FCMService.TYPE_DAILY_MESSAGE -> {
                DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToTodayBattle(""))
            }
        }

        intent.removeExtra(FCMService.EXTRA_FCM_TYPE)
    }

    private fun handleDeepLink(intent: Intent?) {
        val uri = intent?.data ?: return
        Log.d("DeepLinkFlow", "딥링크 감지됨: $uri")

        var targetBattleId: String? = null
        var targetReportId: String? = null

        if (uri.host == "picke.store") {
            if (uri.path?.startsWith("/recap/") == true) targetReportId = uri.lastPathSegment
            if (uri.path?.startsWith("/battle/") == true) targetBattleId = uri.lastPathSegment
        } else if (uri.host == "kakaolink") {
            targetReportId = uri.getQueryParameter("reportId")
            targetBattleId = uri.getQueryParameter("battleId")
        }

        if (targetReportId != null) DeepLinkManager.pendingReportId = targetReportId
        if (targetBattleId != null) DeepLinkManager.pendingBattleId = targetBattleId

        if (targetReportId != null) DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToReport(targetReportId))
        if (targetBattleId != null) DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToBattle(targetBattleId))

        intent.data = null
    }

}