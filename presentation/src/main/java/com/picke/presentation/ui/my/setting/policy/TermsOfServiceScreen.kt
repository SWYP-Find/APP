package com.picke.presentation.ui.my.setting.policy

import androidx.compose.foundation.background
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
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.PolicyStrings

@Composable
fun TermsOfServiceScreen(
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "서비스 이용약관",
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = PickeTheme.colors.backgroundBrand
                )
            }
        },
        containerColor = PickeTheme.colors.backgroundBrand
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 시행 일자
            Text(
                text = "시행일자: ${PolicyStrings.EFFECTIVE_DATE}",
                style = PickeTheme.typography.b5Medium,
                color = PickeTheme.colors.textMuted,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = PolicyStrings.TERMS_OF_SERVICE,
                style = PickeTheme.typography.b4Medium,
                color = PickeTheme.colors.neutral600,
                lineHeight = PickeTheme.typography.b4Medium.fontSize * 1.5
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}