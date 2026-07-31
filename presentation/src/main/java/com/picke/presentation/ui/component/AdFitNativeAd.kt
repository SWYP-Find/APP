package com.picke.presentation.ui.component

import android.content.Context
import android.graphics.Outline
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.kakao.adfit.ads.na.AdFitAdInfoIconPosition
import com.kakao.adfit.ads.na.AdFitMediaView
import com.kakao.adfit.ads.na.AdFitNativeAdBinder
import com.kakao.adfit.ads.na.AdFitNativeAdLayout
import com.kakao.adfit.ads.na.AdFitNativeAdLoader
import com.kakao.adfit.ads.na.AdFitNativeAdRequest
import com.kakao.adfit.ads.na.AdFitNativeAdView
import com.kakao.adfit.ads.na.AdFitVideoAutoPlayPolicy
import com.picke.presentation.R

private const val TAG = "AdFitNativeFlow"

private const val COLOR_SURFACE = 0xFFFEFEFD.toInt()
private const val COLOR_BORDER = 0xFFEFEAE0.toInt()
private const val COLOR_TEXT_PRIMARY = 0xFF131212.toInt()
private const val COLOR_TEXT_MUTED = 0xFF888786.toInt()
private const val COLOR_PRIMARY = 0xFF893825.toInt()
private const val COLOR_WHITE = 0xFFFFFFFF.toInt()

/**
 * 카카오 애드핏 네이티브 광고. adUnitId가 비어있으면 아무것도 그리지 않는다.
 * AdFitMediaView 등 SDK 전용 View를 써야 해서 Compose 컴포넌트로는 직접 구현할 수 없어,
 * 카드 내부(아이콘/제목/부제/미디어/CTA 버튼)를 코드로 조립한 View 트리를 AndroidView로 감싼다.
 * 카드의 배경/테두리/모서리 둥글기는 바깥 Compose 쪽에서 처리한다.
 *
 * @param compact true면 아이콘+제목/부제 헤더, 카드 가운데의 작은 랜드스케이프 썸네일,
 * 우측 정렬된 캡슐형 CTA 버튼으로 구성된 저높이 레이아웃을 사용한다(추천 화면처럼 리스트 최상단에 넣을 때).
 */
@Composable
fun AdFitNativeAd(
    adUnitId: String,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    if (adUnitId.isBlank()) return

    val context = LocalContext.current
    val nativeAdLoader = remember(adUnitId) { AdFitNativeAdLoader.create(context, adUnitId) }
    val binderHolder = remember { arrayOfNulls<AdFitNativeAdBinder>(1) }
    // 로드 성공 전(혹은 실패 시)에는 빈 카드 껍데기가 보이지 않도록 높이를 0으로 접어둔다.
    var isAdLoaded by remember(adUnitId) { mutableStateOf(false) }

    AndroidView(
        modifier = if (isAdLoaded) {
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(COLOR_SURFACE))
                .border(1.dp, Color(COLOR_BORDER), RoundedCornerShape(8.dp))
        } else {
            modifier.height(0.dp)
        },
        factory = { ctx ->
            val (containerView, adLayout) = if (compact) {
                buildCompactNativeAdViewTree(ctx)
            } else {
                buildNativeAdViewTree(ctx)
            }

            val request = AdFitNativeAdRequest.Builder()
                .setAdInfoIconPosition(AdFitAdInfoIconPosition.RIGHT_TOP)
                .setVideoAutoPlayPolicy(AdFitVideoAutoPlayPolicy.WIFI_ONLY)
                .build()

            nativeAdLoader.loadAd(
                request,
                object : AdFitNativeAdLoader.AdLoadListener {
                    override fun onAdLoaded(binder: AdFitNativeAdBinder) {
                        Log.d(TAG, "[네이티브 광고 로드 성공] adUnitId=$adUnitId")
                        binderHolder[0]?.unbind()
                        binderHolder[0] = binder
                        binder.bind(adLayout)
                        isAdLoaded = true
                    }

                    override fun onAdLoadError(errorCode: Int) {
                        Log.w(TAG, "[네이티브 광고 로드 실패] adUnitId=$adUnitId, errorCode=$errorCode")
                    }
                }
            )

            containerView
        }
    )

    DisposableEffect(adUnitId) {
        onDispose {
            binderHolder[0]?.unbind()
            binderHolder[0] = null
        }
    }
}

/** dp -> px 변환 */
private fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

/**
 * 네이티브 광고 카드의 View 트리를 코드로 조립한다.
 * 프로필 아이콘 + 제목/브랜드명 + 미디어(이미지·비디오) + 홍보문구 + CTA 버튼 구조.
 */
private fun buildNativeAdViewTree(context: Context): Pair<AdFitNativeAdView, AdFitNativeAdLayout> {
    val boldFont = ResourcesCompat.getFont(context, R.font.pretendard_semibold)
    val regularFont = ResourcesCompat.getFont(context, R.font.pretendard_regular)

    val profileIconView = ImageView(context).apply {
        layoutParams = LinearLayout.LayoutParams(context.dp(36), context.dp(36))
        scaleType = ImageView.ScaleType.CENTER_CROP
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setOval(0, 0, view.width, view.height)
            }
        }
    }

    val titleTextView = TextView(context).apply {
        setTextColor(COLOR_TEXT_PRIMARY)
        textSize = 14f
        typeface = boldFont ?: Typeface.DEFAULT_BOLD
        maxLines = 1
    }

    val profileNameTextView = TextView(context).apply {
        setTextColor(COLOR_TEXT_MUTED)
        textSize = 12f
        typeface = regularFont ?: Typeface.DEFAULT
        maxLines = 1
        setPadding(0, context.dp(2), 0, 0)
    }

    val textColumn = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
            marginStart = context.dp(10)
            gravity = Gravity.CENTER_VERTICAL
        }
        addView(titleTextView)
        addView(profileNameTextView)
    }

    val headerRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        addView(profileIconView)
        addView(textColumn)
    }

    val mediaView = AdFitMediaView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = context.dp(12)
        }
    }

    val bodyTextView = TextView(context).apply {
        setTextColor(COLOR_TEXT_MUTED)
        textSize = 13f
        typeface = regularFont ?: Typeface.DEFAULT
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = context.dp(8)
        }
    }

    val callToActionButton = Button(context).apply {
        setTextColor(COLOR_WHITE)
        textSize = 14f
        typeface = boldFont ?: Typeface.DEFAULT_BOLD
        isAllCaps = false
        stateListAnimator = null
        setBackgroundColor(COLOR_PRIMARY)
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            context.dp(44)
        ).apply {
            topMargin = context.dp(12)
        }
    }

    val rootColumn = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        val padding = context.dp(14)
        setPadding(padding, padding, padding, padding)
        addView(headerRow)
        addView(mediaView)
        addView(bodyTextView)
        addView(callToActionButton)
    }

    val containerView = AdFitNativeAdView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(rootColumn)
    }

    val adLayout = AdFitNativeAdLayout.Builder(containerView)
        .setContainerViewClickable(false)
        .setTitleView(titleTextView)
        .setBodyView(bodyTextView)
        .setProfileIconView(profileIconView)
        .setProfileNameView(profileNameTextView)
        .setMediaView(mediaView)
        .setCallToActionButton(callToActionButton)
        .build()

    return containerView to adLayout
}

/**
 * 저높이 네이티브 광고 View 트리 (카카오톡 소재 스타일).
 * [아이콘 + 제목/부제] 헤더 아래, 카드 가운데에 작은 랜드스케이프 썸네일을 두고,
 * 그 아래 우측 정렬된 캡슐형 CTA 버튼을 배치해 풀와이드 카드보다 높이를 훨씬 낮춘다.
 */
private fun buildCompactNativeAdViewTree(context: Context): Pair<AdFitNativeAdView, AdFitNativeAdLayout> {
    val boldFont = ResourcesCompat.getFont(context, R.font.pretendard_semibold)
    val regularFont = ResourcesCompat.getFont(context, R.font.pretendard_regular)

    val profileIconView = ImageView(context).apply {
        layoutParams = LinearLayout.LayoutParams(context.dp(40), context.dp(40))
        scaleType = ImageView.ScaleType.CENTER_CROP
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, context.dp(8).toFloat())
            }
        }
    }

    val titleTextView = TextView(context).apply {
        setTextColor(COLOR_TEXT_PRIMARY)
        textSize = 15f
        typeface = boldFont ?: Typeface.DEFAULT_BOLD
        maxLines = 1
    }

    val profileNameTextView = TextView(context).apply {
        setTextColor(COLOR_TEXT_MUTED)
        textSize = 12f
        typeface = regularFont ?: Typeface.DEFAULT
        maxLines = 1
        setPadding(0, context.dp(2), 0, 0)
    }

    val textColumn = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
            marginStart = context.dp(10)
            gravity = Gravity.CENTER_VERTICAL
        }
        addView(titleTextView)
        addView(profileNameTextView)
    }

    val headerRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        addView(profileIconView)
        addView(textColumn)
    }

    // 풀와이드가 아닌, 카드 가운데의 작은 랜드스케이프 썸네일.
    val mediaView = AdFitMediaView(context).apply {
        layoutParams = LinearLayout.LayoutParams(context.dp(140), context.dp(70)).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            topMargin = context.dp(14)
        }
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, context.dp(10).toFloat())
            }
        }
    }

    // 필수 바인딩 대상이라 뷰 자체는 유지하되, 이 레이아웃에서는 시각적으로 노출하지 않는다.
    val bodyTextView = TextView(context).apply {
        setTextColor(COLOR_TEXT_MUTED)
        textSize = 12f
        typeface = regularFont ?: Typeface.DEFAULT
        visibility = View.GONE
    }

    val callToActionButton = Button(context).apply {
        setTextColor(COLOR_WHITE)
        textSize = 13f
        typeface = boldFont ?: Typeface.DEFAULT_BOLD
        isAllCaps = false
        stateListAnimator = null
        setPadding(context.dp(18), context.dp(8), context.dp(18), context.dp(8))
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = context.dp(20).toFloat()
            setColor(COLOR_PRIMARY)
        }
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END
            topMargin = context.dp(14)
        }
    }

    val rootColumn = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        val padding = context.dp(14)
        setPadding(padding, padding, padding, padding)
        addView(headerRow)
        addView(mediaView)
        addView(callToActionButton)
        addView(bodyTextView)
    }

    val containerView = AdFitNativeAdView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(rootColumn)
    }

    val adLayout = AdFitNativeAdLayout.Builder(containerView)
        .setContainerViewClickable(false)
        .setTitleView(titleTextView)
        .setBodyView(bodyTextView)
        .setProfileIconView(profileIconView)
        .setProfileNameView(profileNameTextView)
        .setMediaView(mediaView)
        .setCallToActionButton(callToActionButton)
        .build()

    return containerView to adLayout
}
