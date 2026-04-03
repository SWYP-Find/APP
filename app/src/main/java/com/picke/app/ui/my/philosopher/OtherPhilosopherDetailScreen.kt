/*
package com.swyp4.team2.ui.my.philosopher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.app.R
import com.swyp4.team2.ui.component.CustomButton
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Primary900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun OtherPhilosopherDetailScreen(
    reportId: String,
    onBackClick: () -> Unit,
    onGoToSplashClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PhilosopherTypeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val report = uiState.report
    val scrollState = rememberScrollState()

    LaunchedEffect(reportId) {
        viewModel.fetchOtherPhilosopherReport(reportId)
    }

    Scaffold(
        containerColor = Beige200,
        topBar = {
            CustomTopAppBar(
                title = "철학자 유형",
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = onBackClick,
                backgroundColor = Beige200,
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
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                if (report != null && report.hasTestResult) {

                    // 1. 철학자 유형 헤더
                    report.mainPhilosopher?.let { PhilosopherHeaderSection(it) }

                    // 2. 성향 분석
                    report.traitAnalysis?.let { TraitAnalysisSection(it) }

                    // 3. 취향 리포트
                    report.tasteReport?.let { TasteReportSection(it) }

                    // 4. 궁합 유형
                    report.chemistry?.let { ChemistrySection(it) }

                    // 5. 하단 버튼 변경
                    CustomButton(
                        text = "나도 테스트 해보기",
                        onClick = onGoToSplashClick,
                        modifier = Modifier.padding(bottom = 24.dp),
                        backgroundColor = SwypTheme.colors.primary,
                        textColor = Color.White
                    )
                }
            }
        }
    }
}*/
