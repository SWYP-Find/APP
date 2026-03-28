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
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "개인정보처리방침",
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
                text = "PICKE 개인정보 처리방침\n\nPICKE(이하 \"회사\")은 「개인정보 보호법」, 「정보통신망 이용촉진 및 정보보호 등에 관한 법률」에 따라 정보주체의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리방침을 수립·공개합니다.\n\n\n제1조 (개인정보의 처리 목적)\n\n회사는 다음의 목적을 위하여 개인정보를 처리합니다. 처리하고 있는 개인정보는 다음의 목적 이외의 용도로는 이용되지 않으며, 이용 목적이 변경되는 경우에는 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.\n\n1. 회원 가입 및 관리\n- 회원 가입의사 확인, 회원제 서비스 제공에 따른 본인 식별·인증\n- 회원자격 유지·관리, 서비스 부정이용 방지\n- 각종 고지·통지, 고충처리 목적으로 개인정보를 처리합니다.\n\n2. 서비스 제공\n- 링크 저장 및 관리 서비스 제공\n- 콘텐츠 제공, AI 기반 링크 분석 및 추천 서비스 제공\n- 맞춤 서비스 제공, 본인인증 목적으로 개인정보를 처리합니다.",
                style = SwypTheme.typography.b4Medium,
                color = Gray600
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}