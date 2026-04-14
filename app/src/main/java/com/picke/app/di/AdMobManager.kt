package com.picke.app.di

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.picke.app.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdMobManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var rewardedAd: RewardedAd? = null
    private val adUnitId = BuildConfig.ADMOB_REWARDED_AD_UNIT_ID

    fun loadAd(userId: String) {
        Log.d("AdMobManagerFlow", "1. [광고 로드 요청] userId: $userId 로드 시작")
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("AdMobManagerFlow", "❌ [광고 로드 실패]: ${adError.message}")
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d("AdMobManagerFlow", "2. [광고 로드 성공]")
                rewardedAd = ad

                val options = ServerSideVerificationOptions.Builder()
                    .setCustomData(userId)
                    .build()
                rewardedAd?.setServerSideVerificationOptions(options)

                Log.d("AdMobManagerFlow", "3. [SSV 설정 완료] custom_data(userId) = $userId")
            }
        })
    }

    fun showAd(activity: Activity, onRewardEarned: () -> Unit): Boolean {
        if (rewardedAd != null) {
            Log.d("AdMobManagerFlow", "4. [광고 띄우기 요청] 유저가 버튼을 클릭함")

            rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    Log.d("AdMobManagerFlow", "5. ▶️ [광고 노출 성공]")
                }

                override fun onAdDismissedFullScreenContent() {
                    Log.d("AdMobManagerFlow", "7. ⏹️ [광고 닫힘]")
                    rewardedAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e("AdMobManagerFlow", "❌ [광고 노출 실패]: ${adError.message}")
                    rewardedAd = null
                }
            }

            rewardedAd?.show(activity) { rewardItem ->
                Log.d("AdMobManagerFlow", "6. 🎉 [보상 조건 달성!!]")
                onRewardEarned()
            }
            return true
        } else {
            Log.d("AdMobManagerFlow", "⚠️ 아직 광고가 로딩되지 않았습니다.")
            return false
        }
    }
}