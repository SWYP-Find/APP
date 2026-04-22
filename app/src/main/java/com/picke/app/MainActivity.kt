package com.picke.app

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.picke.app.ui.splash.SplashUiState
import com.picke.app.ui.splash.SplashViewModel
import com.picke.app.ui.theme.SwypAppTheme
import com.picke.app.util.DeepLinkEvent
import com.picke.app.util.DeepLinkManager
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            splashViewModel.uiState.value is SplashUiState.Loading
        }

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
        handleDeepLink(intent)
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

fun getKeyHash(context: Context) {
    try {
        val info: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
        }

        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.signingInfo?.apkContentsSigners
        } else {
            @Suppress("DEPRECATION")
            info.signatures
        }

        signatures?.forEach { signature ->
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHashFlow", "현재 기기의 키 해시값: ${Base64.encodeToString(md.digest(), Base64.DEFAULT)}")
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("KeyHashFlow", "패키지 이름을 찾을 수 없습니다.", e)
    } catch (e: NoSuchAlgorithmException) {
        Log.e("KeyHashFlow", "해당 알고리즘을 사용할 수 없습니다.", e)
    }
}