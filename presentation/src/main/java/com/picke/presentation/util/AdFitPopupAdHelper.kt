package com.picke.presentation.util

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.kakao.adfit.ads.popup.AdFitPopupAd
import com.kakao.adfit.ads.popup.AdFitPopupAdDialogFragment
import com.kakao.adfit.ads.popup.AdFitPopupAdLoader
import com.kakao.adfit.ads.popup.AdFitPopupAdRequest

private const val TAG = "AdFitPopupFlow"

// 앱 프로세스 하나당 한 번만 시도하기 위한 플래그. 홈 탭을 재진입할 때마다 다시 뜨지 않도록
// (프로세스가 살아있는 동안만 유지되며, 앱을 완전히 껐다 켜면 다시 최초 1회 노출된다)
private var hasAttemptedTransitionAd = false

/**
 * 카카오 애드핏 앱 전환 팝업 광고를 요청하고, 성공하면 다이얼로그로 노출한다.
 * adUnitId가 비어있으면(local.properties 미설정 등) 아무 동작도 하지 않는다.
 * 앱을 켜서 홈에 최초 진입할 때만 노출하고, 이후 홈 탭을 재진입해도 다시 뜨지 않는다.
 */
fun showAdFitTransitionPopupAd(activity: FragmentActivity, adUnitId: String) {
    if (adUnitId.isBlank()) return
    if (hasAttemptedTransitionAd) return
    hasAttemptedTransitionAd = true
    if (activity.isFinishing || activity.isDestroyed) return

    val popupAdLoader = AdFitPopupAdLoader.create(activity, adUnitId)

    if (popupAdLoader.isLoading || popupAdLoader.isBlockedByRequestPolicy) {
        Log.d(
            TAG,
            "[팝업 광고 요청 스킵] isLoading=${popupAdLoader.isLoading}, isBlockedByRequestPolicy=${popupAdLoader.isBlockedByRequestPolicy}"
        )
        return
    }

    popupAdLoader.loadAd(
        AdFitPopupAdRequest.build(AdFitPopupAd.Type.Transition),
        object : AdFitPopupAdLoader.OnAdLoadListener {
            override fun onAdLoaded(ad: AdFitPopupAd) {
                Log.d(TAG, "[팝업 광고 로드 성공] adUnitId=$adUnitId")
                if (activity.isFinishing || activity.isDestroyed) return
                AdFitPopupAdDialogFragment(ad).show(activity.supportFragmentManager, AdFitPopupAdDialogFragment.TAG)
            }

            override fun onAdLoadError(errorCode: Int) {
                Log.w(TAG, "[팝업 광고 로드 실패] adUnitId=$adUnitId, errorCode=$errorCode")
            }
        }
    )
}
