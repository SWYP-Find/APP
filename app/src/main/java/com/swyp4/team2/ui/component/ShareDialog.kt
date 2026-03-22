package com.swyp4.team2.ui.component

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.swyp4.team2.R
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige900
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun ShareDialog(
    onDismiss: () -> Unit,
    onKakaoClick: () -> Unit,
    onInstaClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onCopyLinkClick: () -> Unit
) {
    // 다이얼로그 바깥 배경을 누르거나 뒤로가기를 누르면 꺼지도록 설정
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Beige600, RoundedCornerShape(4.dp))
                .border(1.dp, Beige900, RoundedCornerShape(4.dp))
        ) {
            // [상단 헤더 영역]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // 왼쪽 '공유하기' 텍스트
                Text(
                    text = "공유하기",
                    style = SwypTheme.typography.h4SemiBold,
                    color = Gray900,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                // 오른쪽 닫기(X) 버튼
                Icon(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "닫기",
                    tint = Gray900,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(12.dp)
                        .clickable { onDismiss() }
                )
            }

            // 구분선
            HorizontalDivider(color = Gray900, thickness = 1.dp)

            // [하단 SNS 버튼 영역]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShareItem(iconRes = R.drawable.ic_kakao_share, title = "카카오톡", onClick = onKakaoClick)
                ShareItem(iconRes = R.drawable.ic_instagram, title = "인스타그램", onClick = onInstaClick)
                ShareItem(iconRes = R.drawable.ic_facebook, title = "페이스북", onClick = onFacebookClick)
                ShareItem(iconRes = R.drawable.ic_link, title = "링크 복사", onClick = onCopyLinkClick)
            }
        }
    }
}

@Composable
fun ShareItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(42.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = Primary500,
            style = SwypTheme.typography.b5Medium
        )
    }
}