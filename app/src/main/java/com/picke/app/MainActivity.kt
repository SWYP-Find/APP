package com.picke.app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.picke.app.notification.FCMService
import com.picke.app.ui.notification.NotificationPermissionBottomSheet
import com.picke.app.ui.splash.SplashUiState
import com.picke.app.ui.splash.SplashViewModel
import com.picke.app.ui.theme.SwypAppTheme
import com.picke.app.util.DeepLinkEvent
import com.picke.app.util.DeepLinkManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("MainActivity", "알림 권한 결과: $isGranted")
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            splashViewModel.uiState.value is SplashUiState.Loading
        }

        handleFcmIntent(intent)
        handleDeepLink(intent)

        enableEdgeToEdge()

        setContent {
            SwypAppTheme {
                val uiState by splashViewModel.uiState.collectAsState()
                var showNotificationSheet by remember { mutableStateOf(false) }

                // 스플래시 로딩이 끝나면 알림 권한 체크 (최초 1회)
                if (uiState !is SplashUiState.Loading) {
                    val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    val alreadyAsked = prefs.getBoolean("notification_permission_asked", false)
                    if (!alreadyAsked && !showNotificationSheet) {
                        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            ContextCompat.checkSelfPermission(
                                this, Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        } else true

                        if (!hasPermission) showNotificationSheet = true
                    }
                }

                AppNavigation(splashViewModel)

                if (showNotificationSheet) {
                    NotificationPermissionBottomSheet(
                        onDismiss = { showNotificationSheet = false },
                        onAgree = {
                            showNotificationSheet = false
                            markNotificationPermissionAsked()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        },
                        onDisagree = {
                            showNotificationSheet = false
                            markNotificationPermissionAsked()
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleFcmIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleFcmIntent(intent: Intent?) {
        val type = intent?.getStringExtra(FCMService.EXTRA_FCM_TYPE) ?: return
        val battleId = intent.getStringExtra(FCMService.EXTRA_FCM_BATTLE_ID)
        val commentId = intent.getStringExtra(FCMService.EXTRA_FCM_COMMENT_ID)

        Log.d("MainActivity", "FCM 알림 탭 - type: $type, battleId: $battleId, commentId: $commentId")

        when (type) {
            FCMService.TYPE_BATTLE -> battleId?.let {
                DeepLinkManager.pendingBattleId = it
                DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToBattle(it))
            }
            FCMService.TYPE_COMMENT -> commentId?.let {
                DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToComment(it))
            }
            FCMService.TYPE_ALARM -> {
                DeepLinkManager.deepLinkEvent.tryEmit(DeepLinkEvent.GoToAlarm)
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

    private fun markNotificationPermissionAsked() {
        getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("notification_permission_asked", true)
            .apply()
    }
}