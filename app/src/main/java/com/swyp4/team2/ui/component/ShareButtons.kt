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
    resultId: String,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
            .clickable {
                val defaultFeed = FeedTemplate(
                    content = Content(
                        title = "나는 칸트형이에요",
                        description = "",
                        imageUrl = "https://your-server.com/images/kant-thumbnail.jpg", // 디자이너가 줄 썸네일 URL (임시)
                        link = Link(
                            // 웹 화면이 없으므로 임시로 플레이스토어 주소나 앱 대표 주소를 넣습니다.
                            webUrl = "https://play.google.com/store/apps/details?id=com.swyp4.team2",
                            mobileWebUrl = "https://play.google.com/store/apps/details?id=com.swyp4.team2",
                            // 🌟 핵심! 앱이 깔려있으면 이 파라미터를 들고 앱을 켭니다!
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

                if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
                    ShareClient.instance.shareDefault(
                        context,
                        defaultFeed
                    ) { sharingResult, error ->
                        if (error != null) {
                            Toast.makeText(
                                context,
                                "카카오톡 공유 실패: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (sharingResult != null) {
                            context.startActivity(sharingResult.intent)
                        }
                    }
                } else {
                    Toast.makeText(context, "카카오톡이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "카카오톡",
                color = Color.Gray,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
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