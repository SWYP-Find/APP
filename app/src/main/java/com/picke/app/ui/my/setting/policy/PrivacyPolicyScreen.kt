package com.picke.app.ui.my.setting.policy

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
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Gray300
import com.picke.app.ui.theme.Gray600
import com.picke.app.ui.theme.SwypTheme
import com.picke.app.util.PolicyStrings

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

            // 시행 일자
            Text(
                text = "시행일자: ${PolicyStrings.EFFECTIVE_DATE}",
                style = SwypTheme.typography.b5Medium,
                color = Gray300,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = PolicyStrings.PRIVACY_POLICY,
                style = SwypTheme.typography.b4Medium,
                color = Gray600,
                lineHeight = SwypTheme.typography.b4Medium.fontSize * 1.5
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}