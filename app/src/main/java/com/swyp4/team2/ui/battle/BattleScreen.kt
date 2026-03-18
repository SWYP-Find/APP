package com.swyp4.team2.ui.battle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.ui.battle.model.BattleIntroItem
import com.swyp4.team2.ui.battle.model.dummyBattleIntroList
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun BattleScreen(
    onBackClick: () -> Unit,
    onEnterBattle: () -> Unit
){
    val pagerState = rememberPagerState(pageCount = { dummyBattleIntroList.size })

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black)
    ){
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            BattleContent(item = dummyBattleIntroList[page])
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // 커스텀 인디케이터 바
            TopIndicatorBar(
                currentPage = pagerState.currentPage,
                totalPages = dummyBattleIntroList.size
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 뒤로가기 & 공유 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(24.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "뒤로가기",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /* 공유 로직 */ }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "공유",
                        tint = Color.White
                    )
                }
            }
        }

        // 버튼 (배틀 입장하기)
        Button(
            onClick = onEnterBattle,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)
                .padding(bottom = 20.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8C3E26)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("배틀 입장하기", style = SwypTheme.typography.b1Medium, color = Color.White)
        }
    }
}

@Composable
fun BattleContent(item: BattleIntroItem) {
    Column(modifier = Modifier.fillMaxSize()) {
        // [상단] 배경 이미지 + 그라데이션 페이드 아웃
        Box(modifier = Modifier.fillMaxWidth().height(420.dp)) {
            AsyncImage(
                model = item.bgImageRes,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xFF161616)),
                                    startY = size.height * 0.4f // 위에서 40% 지점부터 어두워짐
                                )
                            )
                        }
                    },
                contentScale = ContentScale.Crop
            )

            // 텍스트 정보들 (이미지 위에 오버레이)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 해시태그
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item.tags.forEach { tag ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                text = "#$tag",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = SwypTheme.typography.label,
                                color = SwypTheme.colors.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = item.title,
                    style = SwypTheme.typography.h1SemiBold,
                    color = SwypTheme.colors.surface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = item.description,
                    style = SwypTheme.typography.b4Regular,
                    color = Gray500,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                // 시간 테두리 박스
                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(2.dp),
                    border = BorderStroke(1.dp, Gray700)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(R.drawable.ic_clock), null, Modifier.size(14.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(item.timeLeft, style = SwypTheme.typography.labelXSmall, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // [하단] VS 카드 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center // 🌟 Box를 쓰면 뷰를 겹치기 쉽습니다!
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) { // 카드 사이 2dp 간격
                OpinionCard(name = item.leftName, opinion = item.leftOpinion, quote = item.leftQuote)
                OpinionCard(name = item.rightName, opinion = item.rightOpinion, quote = item.rightQuote)
            }

            // 정중앙 VS 원형 뱃지
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color(0xFFEADBCE) // 베이지색
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("VS", style = SwypTheme.typography.b1Medium, color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun TopIndicatorBar(currentPage: Int, totalPages: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 작대기 부분
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 0 until totalPages) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(if (i == currentPage) Color.White else Color.White.copy(alpha = 0.3f))
                )
            }
        }

        // 텍스트 부분 (예: 1/4)
        Text(
            text = "${currentPage + 1}/$totalPages",
            style = SwypTheme.typography.labelXSmall,
            color = Color.White
        )
    }
}

@Composable
fun OpinionCard(name: String, opinion: String, quote: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF222222))
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = name, style = SwypTheme.typography.labelXSmall, color = Color(0xFFEADBCE))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = opinion, style = SwypTheme.typography.h3Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "\"$quote\"", style = SwypTheme.typography.labelXSmall, color = Color.Gray)
    }
}