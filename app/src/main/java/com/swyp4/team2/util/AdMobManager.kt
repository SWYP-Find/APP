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

    /**
     * 1. 광고 장전하기 (화면에 진입할 때 미리 호출해 두면 유저가 기다리지 않습니다)
     * @param userId 백엔드가 "누가 광고를 봤는지" 알 수 있게 해주는 고유 식별표
     */
    fun loadAd(userId: String) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("AdMobManagerFlow", "광고 로드 실패: ${adError.message}")
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d("AdMobManagerFlow", "광고 로드 성공!")
                rewardedAd = ad

                // 🌟 SSV (서버 측 검증) 세팅
                // 구글 서버가 우리 백엔드 서버를 찌를 때, 이 userId를 그대로 넘겨줍니다.
                val options = ServerSideVerificationOptions.Builder()
                    .setCustomData(userId)
                    .build()
                rewardedAd?.setServerSideVerificationOptions(options)
            }
        })
    }

    /**
     * 2. 광고 띄우기 (유저가 "무료 충전" 버튼을 눌렀을 때 호출)
     * @param onRewardEarned 30초 영상을 끝까지 다 봤을 때 실행할 콜백 (포인트 새로고침)
     */
    fun showAd(activity: Activity, onRewardEarned: () -> Unit) {
        if (rewardedAd != null) {
            // 광고 화면이 닫히거나 실패했을 때의 이벤트 처리
            rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("AdMobManagerFlow", "광고 화면이 닫혔습니다.")
                    rewardedAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e("AdMobManagerFlow", "광고 띄우기 실패: ${adError.message}")
                    rewardedAd = null
                }
            }

            rewardedAd?.show(activity) { rewardItem ->
                Log.d("AdMobManagerFlow", "보상 획득 완료! (구글이 천수님 백엔드로 지급 요청을 보냄)")
                onRewardEarned()
            }
        } else {
            Log.d("AdMobManagerFlow", "아직 광고가 로딩되지 않았습니다. 잠시 후 다시 시도해주세요.")
        }
    }
}