package com.swyp4.team2.util

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
import com.swyp4.team2.BuildConfig

class AdMobManager(private val context: Context) {

    private var rewardedAd: RewardedAd? = null
    private val adUnitId = BuildConfig.ADMOB_REWARDED_AD_UNIT_ID

    fun loadAd(userId: String) {
        Log.d("AdMobManagerFlow", "1. [광고 로드 요청] userId: $userId 로드 시작")
        Log.d("AdMobManagerFlow", "1. [광고 로드 요청] adUnitId: $adUnitId 로드 시작")
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("AdMobManagerFlow", "❌ [광고 로드 실패]: ${adError.message}")
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d("AdMobManagerFlow", "2. [광고 로드 성공]")
                rewardedAd = ad

                // 🌟 SSV (서버 측 검증) 세팅
                val options = ServerSideVerificationOptions.Builder()
                    .setCustomData(userId)
                    .build()
                rewardedAd?.setServerSideVerificationOptions(options)

                // 👉 백엔드 개발자에게 보여줄 핵심 로그 1
                Log.d("AdMobManagerFlow", "3. [SSV 설정 완료] 구글 서버에 전달될 custom_data(userId) = $userId")
            }
        })
    }

    fun showAd(activity: Activity, onRewardEarned: () -> Unit): Boolean {
        if (rewardedAd != null) {
            Log.d("AdMobManagerFlow", "4. [광고 띄우기 요청] 유저가 버튼을 클릭함")

            rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    Log.d("AdMobManagerFlow", "5. ▶️ [광고 노출 성공] 화면에 영상이 재생되기 시작함")
                }

                override fun onAdDismissedFullScreenContent() {
                    Log.d("AdMobManagerFlow", "7. ⏹️ [광고 닫힘] 유저가 X 버튼을 눌러 화면을 닫음")
                    rewardedAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e("AdMobManagerFlow", "❌ [광고 노출 실패]: ${adError.message}")
                    rewardedAd = null
                }
            }

            rewardedAd?.show(activity) { rewardItem ->
                // 👉 백엔드 개발자에게 보여줄 핵심 로그 2
                Log.d("AdMobManagerFlow", "6. 🎉 [보상 조건 달성!!] 유저가 영상을 끝까지 다 봄")
                Log.d("AdMobManagerFlow", "   - AdMob 보상 타입: ${rewardItem.type}, 수량: ${rewardItem.amount}")
                Log.d("AdMobManagerFlow", "   - 💡 안드로이드의 역할은 여기서 끝입니다. 이제 구글 서버가 백엔드로 SSV 웹훅을 발송해야 합니다.")

                onRewardEarned()
            }
            return true
        } else {
            Log.d("AdMobManagerFlow", "⚠️ 아직 광고가 로딩되지 않았습니다. 잠시 후 다시 시도해주세요.")
            return false
        }
    }
}