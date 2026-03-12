package com.swyp4.team2.ui.home

import android.graphics.Picture
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp4.team2.ui.component.CopyLinkButton
import com.swyp4.team2.ui.component.InstagramShareButton
import com.swyp4.team2.ui.component.KakaoShareButton

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val picture = remember { Picture() }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            /*KakaoShareButton(
                resultId = "8942",
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(4.dp))
            CopyLinkButton(
                linkToCopy = "https://www.naver.com/",
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(4.dp))*/

            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .drawWithCache {
                        // 📸 [카메라 설치] 여기서부터 아래에 적힌 모든 디자인을 녹화합니다!
                        val width = this.size.width.toInt()
                        val height = this.size.height.toInt()
                        onDrawWithContent {
                            val pictureCanvas = Canvas(picture.beginRecording(width, height))
                            draw(this, this.layoutDirection, pictureCanvas, this.size) {
                                this@onDrawWithContent.drawContent()
                            }
                            picture.endRecording()
                            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                        }
                    }
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    // 🌟 [카드 스타일 변경] 배경색을 짙은 네이비로, 모서리를 둥글게, 골드 테두리 추가
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF1B264F))
                    .border(2.dp, Color(0xFFEBC17B), RoundedCornerShape(32.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "내 딜레마",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "성향 리포트",
                        color = Color.White,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF2E3D59))
                            .padding(vertical = 12.dp, horizontal = 24.dp)
                    ) {
                        Text(
                            text = "원칙 중심형",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            InstagramShareButton(
                picture = picture,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
    }
}