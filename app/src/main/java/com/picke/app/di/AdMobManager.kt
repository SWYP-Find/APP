package com.picke.app.di

import android.app.Activity
import android.content.Context
// AdMob 미사용으로 SDK import 비활성화 (추후 재사용 예정)
// import com.google.android.gms.ads.AdError
// import com.google.android.gms.ads.AdRequest
// import com.google.android.gms.ads.FullScreenContentCallback
// import com.google.android.gms.ads.LoadAdError
// import com.google.android.gms.ads.rewarded.RewardedAd
// import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
// import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.picke.app.BuildConfig
import com.picke.app.analytics.AnalyticsTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// AdMob 광고 로직 전체 비활성화 (추후 재사용 예정). 호출부 호환을 위해 항상 광고 없음(false)으로 동작하는 no-op으로 남겨둠.
@Singleton
class AdMobManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analyticsTracker: AnalyticsTracker
) {

    companion object {
        /** 보상형 광고 1회 시청 시 지급 포인트 */
        private const val AD_REWARD_POINT = 20
    }

    private val adUnitId = BuildConfig.ADMOB_REWARDED_AD_UNIT_ID

    fun onMobileAdsInitialized() {
        // AdMob SDK 초기화 로직 비활성화
        // isSdkInitialized = true
        // Log.d("AdMobManagerFlow", "✅ [SDK 초기화 완료]")
        // pendingUserId?.let { loadAd(it) }
    }

    fun loadAd(userId: String) {
        // AdMob 광고 로드 로직 비활성화
        // if (!isSdkInitialized) {
        //     Log.d("AdMobManagerFlow", "⏳ [SDK 미초기화] userId 큐에 저장: $userId")
        //     pendingUserId = userId
        //     return
        // }
        // pendingUserId = null
        // Log.d("AdMobManagerFlow", "1. [광고 로드 요청] userId: $userId 로드 시작")
        // val adRequest = AdRequest.Builder().build()
        //
        // RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
        //     override fun onAdFailedToLoad(adError: LoadAdError) {
        //         Log.e("AdMobManagerFlow", "❌ [광고 로드 실패]: ${adError.message}")
        //         rewardedAd = null
        //     }
        //
        //     override fun onAdLoaded(ad: RewardedAd) {
        //         Log.d("AdMobManagerFlow", "2. [광고 로드 성공]")
        //         rewardedAd = ad
        //
        //         val options = ServerSideVerificationOptions.Builder()
        //             .setCustomData(userId)
        //             .build()
        //         rewardedAd?.setServerSideVerificationOptions(options)
        //
        //         Log.d("AdMobManagerFlow", "3. [SSV 설정 완료] custom_data(userId) = $userId")
        //     }
        // })
    }

    /**
     * @param placement 광고 노출 위치 식별자 (Mixpanel ad_revenue.placement, snake_case)
     */
    fun showAd(activity: Activity, placement: String, onRewardEarned: () -> Unit): Boolean {
        // AdMob 광고 노출 로직 비활성화. 항상 광고 준비 안 됨으로 반환.
        // if (rewardedAd != null) {
        //     Log.d("AdMobManagerFlow", "4. [광고 띄우기 요청] 유저가 버튼을 클릭함")
        //
        //     rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
        //         override fun onAdShowedFullScreenContent() {
        //             Log.d("AdMobManagerFlow", "5. ▶️ [광고 노출 성공]")
        //         }
        //
        //         override fun onAdDismissedFullScreenContent() {
        //             Log.d("AdMobManagerFlow", "7. ⏹️ [광고 닫힘]")
        //             rewardedAd = null
        //         }
        //
        //         override fun onAdFailedToShowFullScreenContent(adError: AdError) {
        //             Log.e("AdMobManagerFlow", "❌ [광고 노출 실패]: ${adError.message}")
        //             rewardedAd = null
        //         }
        //     }
        //
        //     rewardedAd?.show(activity) { rewardItem ->
        //         Log.d("AdMobManagerFlow", "6. 🎉 [보상 조건 달성!!]")
        //         analyticsTracker.trackAdRevenue(placement)
        //         analyticsTracker.trackPointAction(PointActionType.AD_EARN, AD_REWARD_POINT)
        //         onRewardEarned()
        //     }
        //     return true
        // } else {
        //     Log.d("AdMobManagerFlow", "⚠️ 아직 광고가 로딩되지 않았습니다.")
        //     return false
        // }
        return false
    }
}