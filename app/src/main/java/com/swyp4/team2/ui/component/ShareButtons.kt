package com.swyp4.team2.ui.component

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Picture
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.swyp4.team2.util.shareToInstagramStory

@SuppressLint("ServiceCast")
@Composable
fun CopyLinkButton(
    linkToCopy: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
            .clickable {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                val clip = ClipData.newPlainText("ShareLink", linkToCopy)

                clipboard.setPrimaryClip(clip)

                Toast.makeText(context, "링크가 복사되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "링크 복사",
                color = Color.Gray,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun KakaoShareButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit // 클릭 이벤트 추가
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFEE500))
            .clickable { onClick() } // 클릭 시 전달받은 로직 실행
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("카카오톡으로 공유", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun InstagramShareButton(
    picture: Picture,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE1306C))
            .clickable {
                shareToInstagramStory(context, picture)
            }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "인스타그램 스토리",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}