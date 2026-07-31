package com.picke.presentation.util

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.picke.domain.usecase.local.LocalPreferencesUseCases
import com.picke.domain.usecase.attendance.AttendanceUseCases
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.PointActionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 앱이 포그라운드로 올라올 때(콜드 스타트 + 백그라운드 복귀)마다
 * "오늘 최초 진입" 여부를 로컬에 저장된 마지막 출석 날짜와 비교해 판단하고,
 * 최초 진입이면 출석 체크 API를 호출한다.
 *
 * 콜드 스타트 시점의 onStart()는 SplashViewModel의 토큰 갱신이 끝나기 전에
 * 먼저 실행되어 아직 유효하지 않은 토큰으로 API가 호출되는 문제가 있어,
 * 최초 onStart()는 건너뛰고 SplashViewModel이 토큰 갱신 성공 후 [checkAttendanceIfNeeded]를
 * 직접 호출한다. 이후 백그라운드 복귀로 인한 onStart()부터는 이 클래스가 직접 처리한다.
 *
 * 서버가 하루 1회 지급을 최종적으로 보장하므로, 여기서의 날짜 비교는
 * 중복 호출을 줄이기 위한 최적화일 뿐 정합성의 최종 방어선은 아니다.
 */
@Singleton
class AppLifecycleObserver @Inject constructor(
    private val localPreferencesUseCases: LocalPreferencesUseCases,
    private val attendanceUseCases: AttendanceUseCases,
    private val analyticsTracker: AnalyticsTracker
) : DefaultLifecycleObserver {

    companion object {
        private const val TAG = "AppLifecycleObserver_Picke"
        private val ATTENDANCE_ZONE = ZoneId.of("Asia/Seoul")
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isFirstStart = true

    override fun onStart(owner: LifecycleOwner) {
        if (isFirstStart) {
            // 콜드 스타트: SplashViewModel이 토큰 갱신 성공 후 checkAttendanceIfNeeded()를 직접 호출함
            isFirstStart = false
            Log.d(TAG, "[출석] 콜드 스타트 최초 onStart - SplashViewModel에 위임, 스킵")
            return
        }
        checkAttendanceIfNeeded()
    }

    /**
     * 로그인/토큰이 유효한 상태에서 호출해야 한다.
     * 콜드 스타트: SplashViewModel의 토큰 갱신 성공 직후 호출.
     * 포그라운드 복귀: [onStart]에서 호출.
     */
    fun checkAttendanceIfNeeded() {
        val accessToken = localPreferencesUseCases.getAccessToken()
        if (accessToken.isNullOrBlank()) {
            Log.d(TAG, "[출석] 미로그인 상태 - 체크 스킵")
            return
        }

        val today = LocalDate.now(ATTENDANCE_ZONE).toString()
        if (localPreferencesUseCases.getLastAttendanceDate() == today) {
            Log.d(TAG, "[출석] 오늘($today) 이미 체크됨 - 스킵")
            return
        }

        Log.i(TAG, "[출석] 오늘($today) 최초 진입 판단 - 출석 체크 API 호출 시작")
        scope.launch {
            attendanceUseCases.checkAttendanceUseCase()
                .onSuccess { result ->
                    localPreferencesUseCases.saveLastAttendanceDate(today)
                    Log.i(
                        TAG,
                        "[출석] 체크 성공: +${result.pointsEarned}P (연속 ${result.consecutiveDays}일)"
                    )

                    val totalEarned = result.pointsEarned +
                            if (result.streakBonusEarned) result.streakBonusPoints else 0
                    analyticsTracker.trackPointAction(
                        type = PointActionType.ATTENDANCE_EARN,
                        amount = totalEarned,
                        balance = result.totalPoints
                    )
                }
                .onFailure { error ->
                    Log.w(TAG, "[출석] 체크 실패: ${error.message}")
                }
        }
    }
}
