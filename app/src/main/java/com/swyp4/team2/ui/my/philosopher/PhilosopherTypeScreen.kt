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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp4.team2.BuildConfig
import com.swyp4.team2.domain.model.Chemistry
import com.swyp4.team2.domain.model.MainPhilosopherDetail
import com.swyp4.team2.domain.model.TasteReport
import com.swyp4.team2.domain.model.TraitAnalysis
import com.swyp4.team2.ui.component.CustomButton
import com.swyp4.team2.ui.component.ProfileImage
import com.swyp4.team2.ui.component.ShareDialog
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.Primary900
import com.swyp4.team2.util.shareCapturedImageToKakao
import com.swyp4.team2.util.shareToInstagramStory
import kotlinx.coroutines.launch

@Composable
fun PhilosopherTypeScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PhilosopherTypeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val report = uiState.report

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    var showShareDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchPhilosopherReport()
    }

    val onKakaoShareClick = {
        coroutineScope.launch {
            try {
                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()

                shareCapturedImageToKakao(
                    context = context,
                    bitmap = bitmap,
                    resultId = report?.reportId ?: "",
                    philosopherName = report?.mainPhilosopher?.name ?: "알 수 없음",
                    description = report?.mainPhilosopher?.description ?: ""
                )
            } catch (e: Exception) {
                Toast.makeText(context, "캡처 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
    val onInstaShareClick = {
        coroutineScope.launch {
            try {
                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                shareToInstagramStory(context = context, bitmap = bitmap)
            } catch (e: Exception) {
                Toast.makeText(context, "캡처 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        containerColor = Beige200,
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.my_menu_philosopher),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = onBackClick,
                backgroundColor = Beige200,
                actions = {
                    if (report?.hasTestResult == true) {
                        IconButton(onClick = { showShareDialog = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "공유",
                                tint = Gray900,
                                modifier = modifier.size(16.dp)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary900)
            }
        }
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                if (report != null && !report.hasTestResult) {
                    LockedPhilosopherHeaderSection()
                    LockedTraitAnalysisSection()
                }
                else if (report != null && report.hasTestResult) {
                    Box(
                        modifier = Modifier.drawWithContent {
                            graphicsLayer.record { this@drawWithContent.drawContent() }
                            drawLayer(graphicsLayer)
                        }
                    ) {
                        // 1. 철학자 유형 헤더
                        report.mainPhilosopher?.let { PhilosopherHeaderSection(it) }
                    }

                    // 2. 성향 분석
                    report.traitAnalysis?.let { TraitAnalysisSection(it) }

                    // 3. 취향 리포트
                    report.tasteReport?.let { TasteReportSection(it) }

                    // 4. 궁합 유형
                    report.chemistry?.let { ChemistrySection(it) }

                    // 5. 공유하기 버튼
                    CustomButton(
                        text = stringResource(R.string.my_share),
                        onClick = { showShareDialog = true },
                        modifier = Modifier.padding(bottom = 24.dp),
                        backgroundColor = SwypTheme.colors.primary,
                        textColor = Color.White,
                        iconResId = R.drawable.ic_share
                    )
                }
            }
        }

        if (showShareDialog) {
            ShareDialog(
                onDismiss = { showShareDialog = false },
                onKakaoClick = {
                    showShareDialog = false
                    onKakaoShareClick()
                },
                onInstaClick = {
                    showShareDialog = false
                    onInstaShareClick()
                },
                onFacebookClick = {
                    showShareDialog = false
                },
                onCopyLinkClick = {
                    val reportId = report?.reportId ?: ""
                    val packageName = context.packageName
                    val appKey = BuildConfig.KAKAO_DEBUG_APPKEY

                    // 안드로이드 시스템이 해석하는 인텐트 URI 생성
                    val deepLinkUrl = "intent://kakaolink?reportId=$reportId#Intent;scheme=kakao${BuildConfig.KAKAO_DEBUG_APPKEY};package=$packageName;end"

                    // 클립보드에 텍스트 복사
                    clipboardManager.setText(AnnotatedString(deepLinkUrl))

                    showShareDialog = false
                    Toast.makeText(context, "링크가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun LockedPhilosopherHeaderSection() {
    val cardShape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 0.dp, bottomStart = 4.dp, bottomEnd = 4.dp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, cardShape)
            .border(1.dp, Beige400, cardShape)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Primary500))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "나의 철학자 유형", style = SwypTheme.typography.labelMedium, color = Color(0xFF8C3E26))
            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "??형", style = SwypTheme.typography.h2SemiBold, color = Gray900)

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFEBEBEB), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.img_lock),
                    contentDescription = "잠금",
                    modifier = Modifier.size(52.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "아직 분석할 기록이 부족해요.\n배틀에 참여하면 성향을 확인할 수 있어요!",
                style = SwypTheme.typography.b3SemiBold,
                color = Gray500,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun LockedTraitAnalysisSection() {
    Column {
        Text(text = "성향 분석", style = SwypTheme.typography.h4SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(1.dp, Beige400, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            // 흐릿한 레이더 차트 배경
            RadarChart(
                scores = listOf(0.92f, 0.85f, 0.45f, 0.45f, 0.88f, 0.72f),
                labels = listOf("원칙", "이성", "개인", "변화", "내면", "이상"),
                modifier = Modifier.size(200.dp).alpha(0.15f),
            )

            // 중앙 텍스트
            Text(
                text = "배틀 5개에 참여하시면\n잠금을 풀 수 있어요!",
                style = SwypTheme.typography.b3SemiBold,
                color = Gray900,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

// 헤더 섹션
@Composable
fun PhilosopherHeaderSection(philosopher: MainPhilosopherDetail) {
    val cardShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 4.dp,
        bottomEnd = 4.dp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, cardShape)
            .border(1.dp, Beige400, cardShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Primary500)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "나의 철학자 유형", style = SwypTheme.typography.labelMedium, color = Color(0xFF8C3E26))
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = philosopher.name, style = SwypTheme.typography.h1SemiBold, color = Gray900)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 프로필 이미지
            ProfileImage(
                model = philosopher.imageUrl ?: R.drawable.ic_profile_mengzi, // profile.profileImg,
                modifier = Modifier.size(80.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = philosopher.description,
                style = SwypTheme.typography.b3Regular,
                color = Gray600,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 키워드 태그
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                philosopher.tags.forEach { tag ->
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
}

// 성향 분석 섹션 (레이더 차트 + 수치)
@Composable
fun TraitAnalysisSection(analysis: TraitAnalysis) {
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
            // 레이더 차트 호출
            Box(modifier = Modifier.size(220.dp)) {
                RadarChart(
                    scores = listOf(
                        analysis.principle / 100f,
                        analysis.logic / 100f,
                        analysis.individual / 100f,
                        analysis.change / 100f,
                        analysis.inner / 100f,
                        analysis.ideal / 100f
                    ),
                    labels = listOf("원칙", "논리", "일관성", "공감", "실용", "직관")
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 점수 막대 바 (2열 배치)
            val traits = listOf(
                Pair("원칙", analysis.principle), Pair("이성", analysis.logic),
                Pair("개인", analysis.individual), Pair("변화", analysis.change),
                Pair("내면", analysis.inner), Pair("이상", analysis.ideal)
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
        Box(modifier = Modifier.weight(1f).height(4.dp).background(Beige300, CircleShape)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(score / 100f) // 점수 비율만큼 너비 차지
                    .height(4.dp)
                    .background(Beige500, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(text = score.toString(), style = SwypTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Gray900)
    }
}

// 내 취향 리포트 섹션
@Composable
fun TasteReportSection(report: TasteReport) {
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
                ReportStatItem(report.totalParticipation.toString(), "총 참여")
                Divider(modifier = Modifier.height(40.dp).width(1.dp), color = Beige400)
                ReportStatItem(report.opinionChanges.toString(), "의견 전환")
                Divider(modifier = Modifier.height(40.dp).width(1.dp), color = Beige400)
                ReportStatItem("${report.winRate}%", "배틀 승률")
            }

            HorizontalDivider(color = Beige400)

            // 하단 리스트
            report.topCategories.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(text = "0${index + 1}", style = SwypTheme.typography.labelMedium, color = Color(0xFFCBA572))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "#${item.category}", style = SwypTheme.typography.labelMedium, color = Gray900)
                    }
                    Text(text = "${item.count}회", style = SwypTheme.typography.label, color = Gray500)
                }
                if (index < report.topCategories.size - 1) HorizontalDivider(color = Beige100)
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
fun ChemistrySection(chemistry: Chemistry) {
    Column {
        Text(text = "궁합 유형", style = SwypTheme.typography.h4SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ChemistryCard(
                modifier = Modifier.weight(1f),
                isBest = true,
                name = chemistry.best.name,
                desc = chemistry.best.description,
                imageUrl = chemistry.best.imageUrl
            )
            ChemistryCard(
                modifier = Modifier.weight(1f),
                isBest = false,
                name = chemistry.worst.name,
                desc = chemistry.worst.description,
                imageUrl = chemistry.worst.imageUrl
            )
        }
    }
}

@Composable
fun ChemistryCard(
    modifier: Modifier = Modifier,
    isBest: Boolean,
    name: String,
    desc: String,
    imageUrl: Any?
) {
    val titleColor = if (isBest) Color(0xFFCBA572) else Gray600
    val titleText = if (isBest) stringResource(R.string.my_best) else stringResource(R.string.my_worst)
    val iconResId = if (isBest) R.drawable.ic_best else R.drawable.ic_worst

    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(4.dp))
            .border(1.dp, Beige400, RoundedCornerShape(4.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = if (isBest) "Best" else "Worst",
                tint = titleColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = titleText,
                style = SwypTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = titleColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileImage(
            model = imageUrl ?: R.drawable.ic_profile_mengzi,
            modifier = Modifier.size(56.dp),
        )
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
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 2 * 0.7f // 글자가 들어갈 공간을 위해 0.7배 축소
        val angleStep = (2 * Math.PI / 6).toFloat()

        val primaryColor = Color(0xFF8C3E26) // 테마 갈색
        val gridColor = Color(0xFFE2CFA7)    // 베이지색


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
        drawPath(dataPath, color = primaryColor.copy(alpha = 0.15f))
        drawPath(dataPath, color = primaryColor, style = Stroke(width = 2.dp.toPx()))

        // 3. 라벨(글자) 그리기
        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#555555")
            textSize = 12.sp.toPx()
            textAlign = android.graphics.Paint.Align.CENTER
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