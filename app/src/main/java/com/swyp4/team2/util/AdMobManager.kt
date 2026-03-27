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
    // 🚨 구글이 제공하는 '테스트용' 보상형 광고 ID입니다.
    // (나중에 앱 출시 직전에 AdMob 대시보드에서 발급받은 '진짜 ID'로 꼭! 바꿔야 합니다)

    /**
     * 1. 광고 장전하기 (화면에 진입할 때 미리 호출해 두면 유저가 기다리지 않습니다)
     * @param userId 백엔드가 "누가 광고를 봤는지" 알 수 있게 해주는 고유 식별표
     */
    fun loadAd(userId: String) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("AdMobManager", "광고 로드 실패: ${adError.message}")
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d("AdMobManager", "광고 로드 성공!")
                rewardedAd = ad

                // 🌟 [핵심] SSV (서버 측 검증) 세팅!
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
                    Log.d("AdMobManager", "광고 화면이 닫혔습니다.")
                    // 사용자가 광고를 껐으니, 다음을 위해 메모리에서 비워줍니다.
                    // (뷰모델이나 화면 쪽에서 다시 loadAd()를 호출해서 새 알을 장전해야 합니다)
                    rewardedAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e("AdMobManager", "광고 띄우기 실패: ${adError.message}")
                    rewardedAd = null
                }
            }

            // 진짜로 화면에 광고 띄우기!
            rewardedAd?.show(activity) { rewardItem ->
                // 🌟 유저가 영상을 스킵 없이 끝까지 다 봤을 때 이곳이 실행됩니다!
                Log.d("AdMobManager", "보상 획득 완료! (구글이 천수님 백엔드로 지급 요청을 보냄)")
                onRewardEarned()
            }
        } else {
            Log.d("AdMobManager", "아직 광고가 로딩되지 않았습니다. 잠시 후 다시 시도해주세요.")
        }
    }
}