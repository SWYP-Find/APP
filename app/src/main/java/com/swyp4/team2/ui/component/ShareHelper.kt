import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import java.io.File
import java.io.FileOutputStream

// 1. 비트맵을 File 객체로 변환 (카카오 업로드용)
fun saveBitmapToFile(context: Context, bitmap: Bitmap): File? {
    return try {
        val cachePath = File(context.cacheDir, "shared_images")
        cachePath.mkdirs()
        val file = File(cachePath, "kakao_share.png")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// 2. 캡처된 이미지를 카카오에 업로드 후 템플릿 공유 실행
fun shareCapturedImageToKakao(
    context: Context,
    bitmap: Bitmap,
    resultId: String,
    philosopherName: String,
    description: String
) {
    val file = saveBitmapToFile(context, bitmap) ?: return

    // 1단계: 카카오 서버에 이미지 업로드
    ShareClient.instance.uploadImage(file) { imageUploadResult, error ->
        if (error != null) {
            Toast.makeText(context, "이미지 업로드 실패: ${error.message}", Toast.LENGTH_SHORT).show()
        } else if (imageUploadResult != null) {
            // 카카오 서버가 임시로 발급해준 이미지 URL!
            val uploadedImageUrl = imageUploadResult.infos.original.url

            // 2단계: 발급받은 URL을 넣어서 FeedTemplate 조립
            val feed = FeedTemplate(
                content = Content(
                    title = "나의 철학자 유형은 [$philosopherName]!",
                    description = description,
                    imageUrl = uploadedImageUrl, // 🔥 방금 업로드된 URL을 여기에 넣습니다!
                    link = Link(
                        webUrl = "https://play.google.com/store/apps/details?id=com.swyp4.team2",
                        mobileWebUrl = "https://play.google.com/store/apps/details?id=com.swyp4.team2",
                        androidExecutionParams = mapOf("resultId" to resultId)
                    )
                ),
                buttons = listOf(
                    Button(
                        title = "내 철학 유형 알아보러 가기",
                        link = Link(
                            webUrl = "https://play.google.com/store/apps/details?id=com.swyp4.team2",
                            mobileWebUrl = "https://play.google.com/store/apps/details?id=com.swyp4.team2",
                            androidExecutionParams = mapOf("resultId" to resultId)
                        )
                    )
                )
            )

            // 3단계: 카카오톡 열기
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