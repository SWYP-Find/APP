package com.picke.presentation.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.distinctUntilChangedBy

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AnalyticsTrackerEntryPoint {
    fun analyticsTracker(): AnalyticsTracker
}

/** 컴포저블에서 AnalyticsTracker 싱글턴을 얻는다 (ViewModel 없이 버튼 탭 등을 추적할 때 사용) */
@Composable
fun rememberAnalyticsTracker(): AnalyticsTracker {
    val appContext = LocalContext.current.applicationContext
    return remember(appContext) {
        EntryPointAccessors.fromApplication(appContext, AnalyticsTrackerEntryPoint::class.java)
            .analyticsTracker()
    }
}

/**
 * NavController 백스택 변화를 관찰해 §2 화면 enum에 등재된 화면만 screen_view를 자동 전송한다.
 * (화면당 1회, 재진입 시 재발화 - 매핑에 없는 route는 전송하지 않음)
 */
@Composable
fun TrackScreenViews(navController: NavController) {
    val tracker = rememberAnalyticsTracker()
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow
            .distinctUntilChangedBy { it.id }
            .collect { entry ->
                val screen = entry.destination.route?.let { AnalyticsScreen.fromRoute(it) }
                    ?: return@collect
                tracker.trackScreenView(screen)
            }
    }
}