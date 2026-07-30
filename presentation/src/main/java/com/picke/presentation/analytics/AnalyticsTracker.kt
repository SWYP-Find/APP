package com.picke.presentation.analytics

import android.content.Context
import android.os.Build
import android.util.Log
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.picke.presentation.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mixpanel 이벤트 전송 중앙 창구.
 *
 * - 명세서(크로스플랫폼 계약서)의 이벤트만 typed 메서드로 노출해 오타/스키마 불일치를 방지한다.
 * - 모든 전송은 try-catch로 감싸 트래킹 실패가 앱 동작에 영향을 주지 않도록 한다.
 * - 속성에 PII(이메일/실명/토큰) 금지. 유저 키는 user_tag만 사용.
 */
@Singleton
class AnalyticsTracker @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mixpanel: MixpanelAPI
) {
    companion object {
        private const val TAG = "AnalyticsTracker_Picke"
    }

    /** 직전에 전송한 screen_view의 screen 값 (referrer 자동 첨부용) */
    @Volatile
    private var lastScreen: String? = null

    init {
        registerBaseSuperProperties()
    }

    // region 슈퍼 프로퍼티 / 유저 식별
    private val appVersionName: String
        get() = try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }

    private val appVersionCode: String
        get() = try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                packageInfo.versionCode.toString()
            }
        } catch (e: Exception) {
            "0"
        }

    /** os_type / app_version / build 슈퍼 프로퍼티 등록. is_logged_in은 최초 1회만 false로 초기화 */
    private fun registerBaseSuperProperties() {
        safely("registerBaseSuperProperties") {
            mixpanel.registerSuperProperties(JSONObject().apply {
                put(AnalyticsProp.OS_TYPE, "android")
                put(AnalyticsProp.APP_VERSION, appVersionName)
                put(AnalyticsProp.BUILD, appVersionCode)
            })
            mixpanel.registerSuperPropertiesOnce(JSONObject().apply {
                put(AnalyticsProp.IS_LOGGED_IN, false)
            })
        }
    }

    /**
     * 로그인 성공 시 호출. identify(user_tag) + 로그인 슈퍼 프로퍼티 + 유저 프로퍼티 등록.
     * 신규 가입이면 sign_up 이벤트 1회 전송 + signup_date setOnce.
     */
    fun onLogin(userTag: String, provider: String, isNewUser: Boolean) {
        safely("onLogin") {
            mixpanel.identify(userTag)
            mixpanel.registerSuperProperties(JSONObject().apply {
                put(AnalyticsProp.IS_LOGGED_IN, true)
                put(AnalyticsProp.LOGIN_PROVIDER, provider)
            })
            mixpanel.people.set(JSONObject().apply {
                put(AnalyticsProp.PROVIDER, provider)
            })
            if (isNewUser) {
                mixpanel.people.setOnce(JSONObject().apply {
                    put(
                        AnalyticsProp.SIGNUP_DATE,
                        DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                    )
                })
                track(AnalyticsEvent.SIGN_UP) {
                    put(AnalyticsProp.METHOD, provider)
                }
            }
        }
    }

    /** 자동 로그인(스플래시) 시 호출. identify + 로그인 슈퍼 프로퍼티 갱신 */
    fun onSessionStart(userTag: String, provider: String?) {
        safely("onSessionStart") {
            mixpanel.identify(userTag)
            mixpanel.registerSuperProperties(JSONObject().apply {
                put(AnalyticsProp.IS_LOGGED_IN, true)
                if (provider != null) put(AnalyticsProp.LOGIN_PROVIDER, provider)
            })
        }
    }

    /** 로그아웃/회원탈퇴 시 호출. reset() 후 기본 슈퍼 프로퍼티 재등록 + is_logged_in=false */
    fun onLogout() {
        safely("onLogout") {
            mixpanel.reset()
            mixpanel.registerSuperProperties(JSONObject().apply {
                put(AnalyticsProp.OS_TYPE, "android")
                put(AnalyticsProp.APP_VERSION, appVersionName)
                put(AnalyticsProp.BUILD, appVersionCode)
                put(AnalyticsProp.IS_LOGGED_IN, false)
            })
        }
    }

    /** 철학자 유형 산출/갱신 시 유저 프로퍼티 갱신 */
    fun setPhilosopherType(type: String) {
        safely("setPhilosopherType") {
            mixpanel.people.set(JSONObject().apply {
                put(AnalyticsProp.PHILOSOPHER_TYPE, type)
            })
        }
    }

    /** 포인트 잔액 변동 시 유저 프로퍼티 갱신 */
    fun setPointBalance(balance: Int) {
        safely("setPointBalance") {
            mixpanel.people.set(JSONObject().apply {
                put(AnalyticsProp.POINT_BALANCE, balance)
            })
        }
    }

    // endregion

    // region 화면 / 상호작용

    /** screen_view. referrer는 직전 화면으로 자동 첨부 */
    fun trackScreenView(screen: String) {
        val referrer = lastScreen
        lastScreen = screen
        track(AnalyticsEvent.SCREEN_VIEW) {
            put(AnalyticsProp.SCREEN, screen)
            if (referrer != null) put(AnalyticsProp.REFERRER, referrer)
        }
    }

    /** ui_action. screen 생략 시 마지막으로 조회한 화면 사용 */
    fun trackUiAction(action: String, screen: String? = null) {
        val screenValue = screen ?: lastScreen ?: return
        track(AnalyticsEvent.UI_ACTION) {
            put(AnalyticsProp.ACTION, action)
            put(AnalyticsProp.SCREEN, screenValue)
        }
    }

    fun trackContentAction(action: String, contentId: String? = null, section: String? = null) {
        track(AnalyticsEvent.CONTENT_ACTION) {
            put(AnalyticsProp.ACTION, action)
            if (contentId != null) put(AnalyticsProp.CONTENT_ID, contentId)
            if (section != null) put(AnalyticsProp.SECTION, section)
        }
    }

    // endregion

    // region 핵심 퍼널

    /** battle_step. is_changed는 post_vote에서만 전달 */
    fun trackBattleStep(
        stepName: String,
        contentId: String,
        choice: String? = null,
        isChanged: Boolean? = null
    ) {
        track(AnalyticsEvent.BATTLE_STEP) {
            put(AnalyticsProp.STEP_NAME, stepName)
            put(AnalyticsProp.CONTENT_ID, contentId)
            if (choice != null) put(AnalyticsProp.CHOICE, choice)
            if (isChanged != null) put(AnalyticsProp.IS_CHANGED, isChanged)
        }
    }

    fun trackOnboardingStep(step: String, method: String? = null) {
        track(AnalyticsEvent.ONBOARDING_STEP) {
            put(AnalyticsProp.STEP, step)
            if (method != null) put(AnalyticsProp.METHOD, method)
        }
    }

    /** 댓글 등록 성공 */
    fun trackCommunityAction(contentId: String, commentLength: Int) {
        track(AnalyticsEvent.COMMUNITY_ACTION) {
            put(AnalyticsProp.CONTENT_ID, contentId)
            put(AnalyticsProp.COMMENT_LENGTH, commentLength)
        }
    }

    /** 리포트 조회 전용 (공유는 trackShareAction 사용) */
    fun trackReportView(topIndicator: String? = null) {
        track(AnalyticsEvent.REPORT_ACTION) {
            put(AnalyticsProp.ACTION_TYPE, ReportActionType.VIEW)
            if (topIndicator != null) put(AnalyticsProp.TOP_INDICATOR, topIndicator)
        }
    }

    /** 보상형 광고 시청 완료 */
    fun trackAdRevenue(placement: String) {
        track(AnalyticsEvent.AD_REVENUE) {
            put(AnalyticsProp.PLACEMENT, placement)
        }
    }

    fun trackPointAction(type: String, amount: Int, balance: Int? = null) {
        track(AnalyticsEvent.POINT_ACTION) {
            put(AnalyticsProp.TYPE, type)
            put(AnalyticsProp.AMOUNT, amount)
            if (balance != null) put(AnalyticsProp.BALANCE, balance)
        }
    }

    fun trackNotificationAction(action: String, unreadCount: Int? = null) {
        track(AnalyticsEvent.NOTIFICATION_ACTION) {
            put(AnalyticsProp.ACTION, action)
            if (unreadCount != null) put(AnalyticsProp.UNREAD_COUNT, unreadCount)
        }
    }

    /** 모든 공유는 이 이벤트로 통일 (recap 리포트 공유 포함) */
    fun trackShareAction(target: String, channel: String? = null) {
        track(AnalyticsEvent.SHARE_ACTION) {
            put(AnalyticsProp.TARGET, target)
            if (channel != null) put(AnalyticsProp.CHANNEL, channel)
        }
    }

    // endregion

    private fun track(event: String, builder: JSONObject.() -> Unit = {}) {
        safely("track($event)") {
            val props = JSONObject().apply(builder)
            mixpanel.track(event, props)
            if (BuildConfig.DEBUG) Log.d(TAG, "[TRACK] $event $props")
        }
    }

    private inline fun safely(label: String, block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            Log.e(TAG, "[FLOW] $label 실패: ${e.message}")
        }
    }
}