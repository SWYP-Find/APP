package com.swyp4.team2.ui.my.setting.policy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun TermsOfServiceScreen(
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "이용약관",
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = Beige200
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 우측 상단 날짜
            Text(
                text = "2026-03-01",
                style = SwypTheme.typography.b5Medium,
                color = Gray300,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 본문 내용
            Text(
                text = "PICKE 서비스 이용약관\n\n제1조 (목적)\n본 약관은 PICKE(이하 \"회사\")가 제공하는 지식 콘텐츠 큐레이션 및 가치관 분석 서비스의 이용과 관련하여 회사와 이용자 간의 권리, 의무 및 책임사항, 서비스 이용조건 및 절차 등을 규정함을 목적으로 합니다.\n\n제2조 (정의)\n1. \"서비스\"란 회사가 제공하는 PICKE 웹/앱 및 모바일 애플리케이션을 통해 제공되는 지식 콘텐츠 시청, 투표 참여, 가치관 성향 리포트, 커뮤니티 활동 등의 모든 서비스를 의미합니다.\n2. \"콘텐츠\"란 회사가 서비스 내에 게시한 텍스트, 오디오, 이미지, 인터랙티브 대본 등을 말합니다.\n3. \"포인트(내공)\"란 서비스 내 활동(투표, 승리, 댓글 등)에 따라 부여되는 가상의 수치로, 등급 상승 및 서비스 내 특정 기능 이용에 사용되는 데이터를 말합니다.",
                style = SwypTheme.typography.b4Medium,
                color = Gray600
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}