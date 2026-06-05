package com.picke.app.ui.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picke.app.R
import com.picke.app.ui.component.CustomButton
import com.picke.app.ui.theme.Gray100
import com.picke.app.ui.theme.Gray300
import com.picke.app.ui.theme.Gray900
import com.picke.app.ui.theme.Primary300
import com.picke.app.ui.theme.Primary700
import com.picke.app.ui.theme.SwypTheme
import com.picke.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPermissionBottomSheet(
    onDismiss: () -> Unit,
    onAgree: () -> Unit,
    onDisagree: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
            Box(
                modifier = Modifier
                    .border(1.dp, Gray100, RoundedCornerShape(50))
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = "Picke Logo",
                    modifier = Modifier.size(width = 63.dp, height = 40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "푸시 알림 설정",
                style = SwypTheme.typography.h3SemiBold,
                color = Gray900
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "픽케의 매일 새로운 배틀 소식을 알려드려요",
                style = SwypTheme.typography.b3Regular,
                color = Gray300
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "설정 > 앱 > 픽케에서\n알림설정 변경이 가능합니다.",
                style = SwypTheme.typography.b4Regular,
                color = Gray300,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomButton(
                    text = "동의하지 않음",
                    onClick = onDisagree,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Primary300,
                    textColor = White
                )
                CustomButton(
                    text = "동의함",
                    onClick = onAgree,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Primary700,
                    textColor = White
                )
            }
        }
    }
}
