package com.picke.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.Gray100
import com.picke.presentation.ui.theme.Gray300
import com.picke.presentation.ui.theme.Gray900
import com.picke.presentation.ui.theme.SwypAppTheme
import com.picke.presentation.ui.theme.SwypTheme
import com.picke.presentation.ui.theme.White
import com.picke.presentation.ui.theme.tokens.BrandColorTokens

private const val URL_SERVICE_TERMS = "https://picke.store/terms"
private const val URL_PRIVACY_POLICY = "https://picke.store/privacy-policy"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceBottomSheet(
    onConfirm: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )
    var isServiceTermsAgreed by remember { mutableStateOf(false) }
    var isPrivacyPolicyAgreed by remember { mutableStateOf(false) }

    val isAllAgreed = isServiceTermsAgreed && isPrivacyPolicyAgreed

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .background(Gray100, RoundedCornerShape(2.dp))
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .border(1.dp, BrandColorTokens.neutral50, RoundedCornerShape(50))
                    .background(
                        color = SwypTheme.colors.backgroundBrand,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_picke),
                    contentDescription = "Picke Logo",
                    modifier = Modifier
                        .size(width = 58.dp, height = 58.dp),
                    colorFilter = ColorFilter.tint(BrandColorTokens.primary600)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "픽케 약관 동의서",
                style = SwypTheme.typography.h3SemiBold,
                color = Gray900
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "편리한 서비스 이용을 위해 약관에 동의해 주세요",
                style = SwypTheme.typography.b3Regular,
                color = Gray300,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Terms Items
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TermsItem(
                    text = "(필수) 서비스 이용약관",
                    isAgreed = isServiceTermsAgreed,
                    onToggle = { isServiceTermsAgreed = !isServiceTermsAgreed },
                    onViewDetail = { uriHandler.openUri(URL_SERVICE_TERMS) }
                )
                TermsItem(
                    text = "(필수) 개인정보처리방침",
                    isAgreed = isPrivacyPolicyAgreed,
                    onToggle = { isPrivacyPolicyAgreed = !isPrivacyPolicyAgreed },
                    onViewDetail = { uriHandler.openUri(URL_PRIVACY_POLICY) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Confirm Button
            CustomButton(
                text = "동의",
                onClick = { if (isAllAgreed) onConfirm() },
                backgroundColor = if (isAllAgreed) SwypTheme.colors.buttonPrimaryBackground else SwypTheme.colors.buttonPrimaryBackgroundDisabled,
                textColor = White,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TermsItem(
    text: String,
    isAgreed: Boolean,
    onToggle: () -> Unit,
    onViewDetail: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SwypTheme.colors.surfaceSubtle, RoundedCornerShape(8.dp))
            .border(1.dp, Gray100, RoundedCornerShape(8.dp))
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 커스텀 체크박스
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isAgreed) SwypTheme.colors.buttonPrimaryBackground else SwypTheme.colors.buttonPrimaryBackgroundDisabled)
                .border(
                    1.dp,
                    if (isAgreed) SwypTheme.colors.borderDefault else SwypTheme.colors.borderDisabled,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isAgreed) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = SwypTheme.typography.b3Regular,
            color = Gray900,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(R.drawable.ic_arrow_right_a),
            contentDescription = "View Detail",
            tint = Gray900,
            modifier = Modifier
                .size(20.dp)
                .clickable { onViewDetail() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsOfServiceBottomSheetPreview() {
    SwypAppTheme {
        TermsOfServiceBottomSheet(
            onConfirm = {}
        )
    }
}
