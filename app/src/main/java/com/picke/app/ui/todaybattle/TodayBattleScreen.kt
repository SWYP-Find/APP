package com.picke.app.ui.todaybattle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.picke.app.R
import com.picke.app.ui.component.CustomButton
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Beige400
import com.picke.app.ui.theme.Beige50
import com.picke.app.ui.theme.Beige600
import com.picke.app.ui.theme.Beige800
import com.picke.app.ui.theme.Beige900
import com.picke.app.ui.theme.Gray400
import com.picke.app.ui.theme.Gray700
import com.picke.app.ui.theme.Gray800
import com.picke.app.ui.theme.Gray900
import com.picke.app.ui.theme.Primary300
import com.picke.app.ui.theme.Secondary200
import com.picke.app.ui.theme.Secondary500
import com.picke.app.ui.theme.Secondary700
import com.picke.app.ui.theme.SwypTheme
import com.picke.app.ui.todaybattle.model.TodayBattleUiModel

@Composable
fun TodayBattleScreen(
    viewModel: TodayBattleViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onEnterBattle: (String) -> Unit
){
    val uiState by viewModel.uiState.collectAsState()
    val battleList = uiState.battleList

    val pagerState = rememberPagerState(pageCount = { battleList.size })
    var selectedOptionId by remember(pagerState.currentPage) { mutableStateOf<String?>(null) }
    val isButtonEnabled = selectedOptionId != null

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black)
        ) {
            // 상단 뒤로가기 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(20.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "뒤로가기",
                        tint = Color.White
                    )
                }
            }
            // 정중앙 스피너
            CircularProgressIndicator(
                color = Beige200,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        return
    }

    if (battleList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black)
        ) {
            // 상단 뒤로가기 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(20.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "뒤로가기",
                        tint = Color.White
                    )
                }
            }

            // 정중앙 안내 문구
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "빈 화면 로고",
                    modifier = Modifier.size(width = 160.dp, height = 120.dp),
                    tint = Beige600
                )
                Text(
                    text = "아직 빠른 배틀이 선정되지 않았어요\n조금만 기다려주세요!",
                    style = SwypTheme.typography.b3Regular,
                    color = Beige400,
                    textAlign = TextAlign.Center // 두 줄일 때 예쁘게 보이도록 가운데 정렬 추가
                )
            }
        }
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        bottomBar = {
            CustomButton(
                text = stringResource(R.string.battle_start),
                onClick = {
                    if (isButtonEnabled) {
                        val currentBattleId = battleList[pagerState.currentPage].battleId
                        viewModel.submitPreVote(
                            battleId = currentBattleId.toLong(),
                            optionId = selectedOptionId!!.toLong(),
                            onSuccess = {
                                onEnterBattle(currentBattleId)
                            }
                        )
                    }
                },
                modifier = Modifier.navigationBarsPadding()
                    .padding(20.dp),
                backgroundColor = if (isButtonEnabled) SwypTheme.colors.primary else Primary300,
                textColor = Beige50
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                BattleContent(
                    item = battleList[page],
                    selectedOptionId = selectedOptionId,
                    onOptionSelect = { optionId -> selectedOptionId = optionId }
                )
            }

            // 상단 UI (인디케이터 & 뒤로가기 버튼)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                TopIndicatorBar(
                    currentPage = pagerState.currentPage,
                    totalPages = battleList.size
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.size(20.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = "뒤로가기",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* 공유 로직 */ }, modifier = Modifier.size(16.dp)) {
                        // 공유 아이콘 부분
                    }
                }
            }
        }
    }
}

@Composable
fun BattleContent(
    item: TodayBattleUiModel,
    selectedOptionId: String?,
    onOptionSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // [상단] 배경 이미지 + 그라데이션 페이드 아웃
        Box(
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.6f),
                                        Color.Black
                                    ),
                                    startY = 0f,
                                    endY = size.height
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
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
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

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = item.title,
                    style = SwypTheme.typography.h1SemiBold,
                    color = SwypTheme.colors.surface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.description,
                    style = SwypTheme.typography.b3Regular,
                    color = Gray400,
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
                        Icon(painterResource(R.drawable.ic_clock), null, Modifier.size(12.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(item.timeLeft, style = SwypTheme.typography.labelXSmall, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // [하단] VS 카드 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (item.options.isNotEmpty()) {
                    val optionA = item.options[0]
                    OpinionCard(
                        name = optionA.name,
                        opinion = optionA.opinion,
                        quote = optionA.quote,
                        isSelected = selectedOptionId == optionA.optionId, // 이제 둘 다 String이라 비교가 잘 됩니다!
                        onClick = { onOptionSelect(optionA.optionId) }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (item.options.size > 1) {
                    val optionB = item.options[1]
                    OpinionCard(
                        name = optionB.name,
                        opinion = optionB.opinion,
                        quote = optionB.quote,
                        isSelected = selectedOptionId == optionB.optionId,
                        onClick = { onOptionSelect(optionB.optionId) }
                    )
                }
            }

            // 정중앙 VS 원형 뱃지
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Secondary200
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("VS", style = SwypTheme.typography.b3SemiBold, color = Color.Black)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TopIndicatorBar(currentPage: Int, totalPages: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
            color = Color.White.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun OpinionCard(
    name: String,
    opinion: String,
    quote: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Secondary700 else Color.Transparent
    val bgColor = if (isSelected) Gray900 else Gray800

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = name, style = SwypTheme.typography.labelXSmall, color = Secondary500)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = opinion, style = SwypTheme.typography.h3Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "\"$quote\"", style = SwypTheme.typography.labelXSmall, color = Color.White.copy(0.3f))
    }
}