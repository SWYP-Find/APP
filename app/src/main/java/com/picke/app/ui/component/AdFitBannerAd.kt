package com.picke.app.ui.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView

private const val TAG = "AdFitBannerFlow"

/**
 * 카카오 애드핏 배너 광고. adUnitId가 비어있으면(local.properties 미설정 등) 아무것도 그리지 않는다.
 * BannerAdView는 노출 크기에 따라 자동으로 wrap_content 되므로 modifier로 별도 크기를 강제하지 않는다.
 */
@Composable
fun AdFitBannerAd(
    adUnitId: String,
    modifier: Modifier = Modifier,
) {
    if (adUnitId.isBlank()) return

    val lifecycleOwner = LocalLifecycleOwner.current
    val adViewHolder = remember { arrayOfNulls<BannerAdView>(1) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            BannerAdView(context).apply {
                setAdUnitId(adUnitId)
                setAdListener(object : AdListener {
                    override fun onAdLoaded() {
                        Log.d(TAG, "[광고 로드 성공] adUnitId=$adUnitId")
                    }

                    override fun onAdFailed(errorCode: Int) {
                        Log.w(TAG, "[광고 로드 실패] adUnitId=$adUnitId, errorCode=$errorCode")
                    }

                    override fun onAdClicked() {
                        Log.d(TAG, "[광고 클릭] adUnitId=$adUnitId")
                    }
                })
                adViewHolder[0] = this
                loadAd()
            }
        }
    )

    DisposableEffect(lifecycleOwner, adUnitId) {
        val observer = LifecycleEventObserver { _, event ->
            val adView = adViewHolder[0] ?: return@LifecycleEventObserver
            when (event) {
                Lifecycle.Event.ON_RESUME -> adView.resume()
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adViewHolder[0]?.destroy()
            adViewHolder[0] = null
        }
    }
}
