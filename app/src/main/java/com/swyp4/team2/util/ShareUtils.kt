package com.swyp4.team2.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Picture
import android.widget.Toast
import androidx.activity.result.ActivityResultRegistry
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun shareToInstagramStory(
    context: Context,
    picture: Picture
){
    try{
        // 1. picture를 이미지 파일인 bitmap으로 인화한다.
        val bitmap = Bitmap.createBitmap(
            picture.width,
            picture.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.parseColor("#1B264F"))
        canvas.drawPicture(picture)

        // 2. 이미지를 우리가 만든 임시 폴더(cache/shared_image)에 PNG 파일로 저장한다.
        val cachePath = File(context.cacheDir, "shared_images")
        cachePath.mkdirs()
        val imageFile = File(cachePath, "instagram_story.png")
        val stream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        // 3. FileProvider 출입증 만들기
        val authority = "com.swyp4.team2.fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, imageFile)

        // 4. 인스타그램 앱을 부르는 Intent 상자 만들기
        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply{
            setDataAndType(uri, "image/png")
            putExtra("interative_asset_uri", uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // 5. 인스타그램 앱에 권한 주고 실행하기
        val activity = context as? Activity
        activity?.grantUriPermission("com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        if (context.packageManager.resolveActivity(intent, 0) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "인스타그램이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }

    } catch (e: Exception) {
        e.printStackTrace()
        android.util.Log.e("InstagramShare", "🚨 인스타 공유 실패 진짜 원인: ", e)
        Toast.makeText(context, "이미지 처리 중 에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
    }

}