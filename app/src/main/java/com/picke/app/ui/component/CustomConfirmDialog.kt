package com.picke.app.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.picke.app.ui.theme.Beige500
import com.picke.app.ui.theme.Primary500
import com.picke.app.ui.theme.SwypTheme

@Composable
fun CustomConfirmDialog(
    message: String,
    confirmText: String = "네, 확인합니다",
    dismissText: String = "뒤로가기",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val modalBackgroundColor = Beige500
    val pointColor = Primary500

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(2.dp),
            color = modalBackgroundColor,
            border = BorderStroke(1.dp, pointColor),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column {
                // 1. 텍스트 영역 (상단)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message,
                        style = SwypTheme.typography.b3SemiBold,
                        color = pointColor,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }

                // 2. 가로 구분선
                HorizontalDivider(thickness = 1.dp, color = pointColor)

                // 3. 버튼 영역 (하단)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onConfirm() }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmText,
                            style = SwypTheme.typography.b3SemiBold,
                            color = pointColor
                        )
                    }

                    // 수직 구분선
                    VerticalDivider(thickness = 1.dp, color = pointColor)

                    // 오른쪽 버튼 (뒤로가기)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(pointColor)
                            .clickable { onDismiss() }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dismissText,
                            style = SwypTheme.typography.b3SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}