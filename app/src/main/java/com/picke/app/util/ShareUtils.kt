package com.picke.app.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import java.io.File
import java.io.FileOutputStream

/**
 * [공통 로직] 비트맵을 캐시 폴더에 파일로 저장하는 함수
 * @param fileName 저장할 파일명 (예: "kakao_share.png", "instagram_story.png")
 */
private fun saveBitmapToCache(context: Context, bitmap: Bitmap, fileName: String): File? {
    return try {
        val cachePath = File(context.cacheDir, "shared_images")
        cachePath.mkdirs()
        val file = File(cachePath, fileName)
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 카카오톡 공유기능
 */
fun shareCapturedImageToKakao(
    context: Context,
    bitmap: Bitmap,
    resultId: String,
    philosopherName: String,
    description: String
) {
    // 1. 공통 함수를 이용해 이미지 저장
    val file = saveBitmapToCache(context, bitmap, "kakao_share.png") ?: return

    val currentPackageName = context.packageName
    val playStoreUrl = "https://play.google.com/store/apps/details?id=$currentPackageName"

    // 2. 카카오 서버에 이미지 업로드
    ShareClient.instance.uploadImage(file) { imageUploadResult, error ->
        if (error != null) {
            Toast.makeText(context, "이미지 업로드 실패: ${error.message}", Toast.LENGTH_SHORT).show()
        } else if (imageUploadResult != null) {
            val uploadedImageUrl = imageUploadResult.infos.original.url

            // 3. FeedTemplate 조립
            val feed = FeedTemplate(
                content = Content(
                    title = "나의 철학자 유형은 [$philosopherName]!",
                    description = description,
                    imageUrl = uploadedImageUrl,
                    link = Link(
                        webUrl = playStoreUrl,
                        mobileWebUrl = playStoreUrl,
                        androidExecutionParams = mapOf("resultId" to resultId)
                    )
                ),
                buttons = listOf(
                    Button(
                        title = "내 철학 유형 알아보러 가기",
                        link = Link(
                            webUrl = playStoreUrl,
                            mobileWebUrl = playStoreUrl,
                            androidExecutionParams = mapOf("resultId" to resultId)
                        )
                    )
                )
            )

            // 4. 카카오톡 열기
            if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
                ShareClient.instance.shareDefault(context, feed) { sharingResult, shareError ->
                    if (shareError != null) {
                        Toast.makeText(context, "공유 실패", Toast.LENGTH_SHORT).show()
                    } else if (sharingResult != null) {
                        context.startActivity(sharingResult.intent)
                    }
                }
            } else {
                Toast.makeText(context, "카카오톡이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

/**
 * 인스타그램 스토리 공유기능
 */
fun shareToInstagramStory(
    context: Context,
    bitmap: Bitmap
) {
    try {
        // 1. 공통 함수를 이용해 이미지 저장
        val imageFile = saveBitmapToCache(context, bitmap, "instagram_story.png")
        if (imageFile == null) {
            Toast.makeText(context, "이미지 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. FileProvider 출입증 만들기
        val authority = "${context.packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, imageFile)

        // 3. 인스타그램 앱을 부르는 Intent 상자 만들기
        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
            type = "image/png"
            putExtra("interactive_asset_uri", uri)
            putExtra("top_background_color", "#F4EFEA")
            putExtra("bottom_background_color", "#E2CFA7")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // 4. 인스타그램 앱에 읽기 권한 임시 부여
        val activity = context as? Activity
        activity?.grantUriPermission("com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // 5. 인스타그램 앱 설치 여부 확인 후 실행
        if (context.packageManager.resolveActivity(intent, 0) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "인스타그램이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }

    } catch (e: Exception) {
        e.printStackTrace()
        android.util.Log.e("InstagramShare", "🚨 인스타 공유 실패 원인: ", e)
        Toast.makeText(context, "이미지 처리 중 에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
    }
}