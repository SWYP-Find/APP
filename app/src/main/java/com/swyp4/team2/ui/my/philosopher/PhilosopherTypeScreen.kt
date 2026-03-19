package com.swyp4.team2.ui.my.philosopher

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Beige100
import com.swyp4.team2.ui.theme.Beige300
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import shareCapturedImageToKakao

@Composable
fun PhilosopherTypeScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    val onKakaoShareClick = {
        coroutineScope.launch {
            try {
                // 1. Box로 감싼 HeaderSection 캡처
                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()

                // 2. 카카오톡에 캡처한 이미지와 함께 공유!
                shareCapturedImageToKakao(
                    context = context,
                    bitmap = bitmap,
                    resultId = "12345", // 실제 데이터로 교체
                    philosopherName = "칸트형", // 실제 데이터로 교체
                    description = "결과보다 과정을 중시하는 원칙주의자" // 실제 데이터로 교체
                )
            } catch (e: Exception) {
                Toast.makeText(context, "캡처 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        containerColor = SwypTheme.colors.background,
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.my_menu_philosopher),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = onBackClick,
                backgroundColor = SwypTheme.colors.background,
                actions = {
                    IconButton(onClick = { onKakaoShareClick()}) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = "공유",
                            tint = Gray900
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(
                modifier = Modifier.drawWithContent {
                    graphicsLayer.record { this@drawWithContent.drawContent() }
                    drawLayer(graphicsLayer)
                }
            ) {
                // 1. 철학자 유형 헤더
                PhilosopherHeaderSection()
            }

            // 2. 성향 분석 (레이더 차트 + 막대 바)
            TraitAnalysisSection()

            // 3. 내 취향 리포트
            TasteReportSection()

            // 4. 궁합 유형
            ChemistrySection()

            // 5. 공유하기 버튼
            Button(
                onClick = { /* 공유 로직 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SwypTheme.colors.primary),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "공유하기", style = SwypTheme.typography.b1Medium, color = SwypTheme.colors.surface)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_share), // 공유 아이콘 교체 필요
                    contentDescription = null,
                    tint = SwypTheme.colors.surface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// 헤더 섹션
@Composable
fun PhilosopherHeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(4.dp))
            .border(1.dp, Beige400, RoundedCornerShape(4.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "나의 철학자 유형", style = SwypTheme.typography.labelMedium, color = Color(0xFF8C3E26))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "칸트형", style = SwypTheme.typography.h1SemiBold, color = Gray900)

        Spacer(modifier = Modifier.height(24.dp))

        // 프로필 이미지
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Beige300),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile_xunzi), // 칸트 이미지로 교체 필요
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "결과보다 과정을 중시하고, 보편적 도덕 법칙을 따르는 원칙주의자. 어떤 상황에서도 흔들리지 않는 기준을 가진 사람입니다.",
            style = SwypTheme.typography.b3Regular,
            color = Gray600,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 키워드 태그
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val tags = listOf("의무론", "규칙 중시", "보편 원칙", "이성적")
            tags.forEach { tag ->
                Box(
                    modifier = Modifier
                        .border(1.dp, Beige600, RoundedCornerShape(2.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = tag, style = SwypTheme.typography.labelXSmall, color = Color(0xFF8C3E26))
                }
            }
        }
    }
}

// 성향 분석 섹션 (레이더 차트 + 수치)
@Composable
fun TraitAnalysisSection() {
    Column {
        Text(text = "성향 분석", style = SwypTheme.typography.h4SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(1.dp, Beige400, RoundedCornerShape(4.dp))
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔥 레이더 차트 호출
            Box(modifier = Modifier.size(220.dp)) {
                RadarChart(
                    scores = listOf(0.92f, 0.85f, 0.45f, 0.45f, 0.88f, 0.72f), // 백엔드 수치를 0~1.0으로 변환해서 넣기
                    labels = listOf("원칙", "논리", "일관성", "공감", "실용", "직관")
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 점수 막대 바 (2열 배치)
            val traits = listOf(
                Pair("원칙", 92), Pair("이성", 85),
                Pair("개인", 72), Pair("변화", 38),
                Pair("내면", 88), Pair("이상", 45)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in 0..2) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        ScoreBar(modifier = Modifier.weight(1f), label = traits[i * 2].first, score = traits[i * 2].second)
                        ScoreBar(modifier = Modifier.weight(1f), label = traits[i * 2 + 1].first, score = traits[i * 2 + 1].second)
                    }
                }
            }
        }
    }
}

// 개별 점수 바 컴포넌트
@Composable
fun ScoreBar(modifier: Modifier = Modifier, label: String, score: Int) {
    Row(
        modifier = modifier
            .background(Beige100, RoundedCornerShape(4.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = SwypTheme.typography.label, color = Gray600)
        Spacer(modifier = Modifier.width(8.dp))

        // 막대 바
        Box(modifier = Modifier.weight(1f).height(4.dp).background(Beige400, CircleShape)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(score / 100f) // 점수 비율만큼 너비 차지
                    .height(4.dp)
                    .background(Color(0xFF8C3E26), CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(text = score.toString(), style = SwypTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Gray900)
    }
}

// 내 취향 리포트 섹션
@Composable
fun TasteReportSection() {
    Column {
        Text(text = "내 취향 리포트", style = SwypTheme.typography.h4SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(1.dp, Beige400, RoundedCornerShape(4.dp))
        ) {
            // 상단 3개 요약 수치
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ReportStatItem("47", "총 참여")
                Divider(modifier = Modifier.height(40.dp).width(1.dp), color = Beige400)
                ReportStatItem("12", "의견 전환")
                Divider(modifier = Modifier.height(40.dp).width(1.dp), color = Beige400)
                ReportStatItem("68%", "배틀 승률")
            }

            HorizontalDivider(color = Beige400)

            // 하단 리스트
            val categories = listOf("철학" to "20회", "문학" to "13회", "예술" to "8회", "사회" to "5회")
            categories.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(text = "0${index + 1}", style = SwypTheme.typography.labelMedium, color = Color(0xFFCBA572))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "#${item.first}", style = SwypTheme.typography.labelMedium, color = Gray900)
                    }
                    Text(text = item.second, style = SwypTheme.typography.label, color = Gray500)
                }
                if (index < categories.size - 1) HorizontalDivider(color = Beige100)
            }
        }
    }
}

@Composable
fun ReportStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = SwypTheme.typography.h2SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = SwypTheme.typography.labelXSmall, color = Gray500)
    }
}

// 궁합 유형 섹션
@Composable
fun ChemistrySection() {
    Column {
        Text(text = "궁합 유형", style = SwypTheme.typography.h4SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // BEST
            ChemistryCard(modifier = Modifier.weight(1f), isBest = true, name = "플라톤형", desc = "본질과 이상을 좇으며 더 나은 세계를 꿈꾸는 사람.")
            // WORST
            ChemistryCard(modifier = Modifier.weight(1f), isBest = false, name = "니체형", desc = "기존의 틀을 부수고 자기만의 길을 가는 사람")
        }
    }
}

@Composable
fun ChemistryCard(modifier: Modifier = Modifier, isBest: Boolean, name: String, desc: String) {
    val titleColor = if (isBest) Color(0xFFCBA572) else Gray600
    val titleText = if (isBest) "👍 BEST" else "👎 WORST"

    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(4.dp))
            .border(1.dp, Beige400, RoundedCornerShape(4.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = titleText, style = SwypTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = titleColor)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(Beige300)) {
            // 철학자 아이콘
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = name, style = SwypTheme.typography.b2Medium, color = Gray900)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = desc, style = SwypTheme.typography.labelXSmall, color = Gray500, textAlign = TextAlign.Center)
    }
}

@Composable
fun RadarChart(
    scores: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 2 * 0.7f // 글자가 들어갈 공간을 위해 0.7배 축소
        val angleStep = (2 * Math.PI / 6).toFloat()

        val primaryColor = Color(0xFF8C3E26) // 테마 갈색
        val gridColor = Color(0xFFE2CFA7)    // 거미줄 베이지색

        // 1. 거미줄 배경 그리기 (3단계)
        for (step in 1..3) {
            val stepRadius = radius * (step / 3f)
            val path = Path()
            for (i in 0..5) {
                val angle = i * angleStep - (Math.PI / 2).toFloat() // 맨 위부터 시작
                val x = centerX + stepRadius * cos(angle)
                val y = centerY + stepRadius * sin(angle)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color = gridColor, style = Stroke(width = 1.dp.toPx()))
        }

        // 중심에서 뻗어나가는 선 6개
        for (i in 0..5) {
            val angle = i * angleStep - (Math.PI / 2).toFloat()
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)
            drawLine(color = gridColor, start = Offset(centerX, centerY), end = Offset(x, y), strokeWidth = 1.dp.toPx())
        }

        // 2. 내 데이터 점수 그리기 (폴리곤)
        val dataPath = Path()
        scores.forEachIndexed { index, score ->
            val angle = index * angleStep - (Math.PI / 2).toFloat()
            val x = centerX + radius * score * cos(angle)
            val y = centerY + radius * score * sin(angle)

            if (index == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)

            // 꼭짓점에 점 찍기
            drawCircle(color = primaryColor, radius = 4.dp.toPx(), center = Offset(x, y))
        }
        dataPath.close()

        // 데이터 영역 색칠 및 테두리
        drawPath(dataPath, color = primaryColor.copy(alpha = 0.15f)) // 반투명 배경
        drawPath(dataPath, color = primaryColor, style = Stroke(width = 2.dp.toPx())) // 굵은 테두리

        // 3. 라벨(글자) 그리기
        val textPaint = android.graphics.Paint().apply { // 🌟 여기를 android.graphics.Paint()로 변경!
            color = android.graphics.Color.parseColor("#555555")
            textSize = 12.sp.toPx()
            textAlign = android.graphics.Paint.Align.CENTER // 🌟 여기도 명시
        }

        labels.forEachIndexed { index, label ->
            val angle = index * angleStep - (Math.PI / 2).toFloat()
            // 반지름보다 살짝 더 바깥쪽에 텍스트 위치 (1.2배)
            val textX = centerX + radius * 1.25f * cos(angle)
            val textY = centerY + radius * 1.25f * sin(angle) + (textPaint.textSize / 3)

            drawContext.canvas.nativeCanvas.drawText(label, textX, textY, textPaint)
        }
    }
}