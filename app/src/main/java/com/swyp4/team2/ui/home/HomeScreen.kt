package com.swyp4.team2.ui.home

import android.graphics.Picture
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.itemKey
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.ChattingLoadingAnimation
import com.swyp4.team2.ui.component.CopyLinkButton
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.component.InstagramShareButton
import com.swyp4.team2.ui.component.KakaoShareButton
import com.swyp4.team2.AppRoute
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun HomeScreen(
    onNavigateToAlarm: ()->Unit,
) {
    // 🌟 가상의 '새 알림' 상태 변수 (나중에는 서버에서 이 값을 받아오게 됩니다!)
    var hasUnreadNotification by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                backgroundColor = SwypTheme.colors.background,
                actions = {
                    IconButton(
                        onClick = {
                            onNavigateToAlarm()
                            hasUnreadNotification = false // 클릭했으니까 빨간 점 없애기 (선택사항)
                        }
                    ) {
                        BadgedBox(
                            badge = {
                                if (hasUnreadNotification) {
                                    Badge(
                                        containerColor = SwypTheme.colors.primary
                                    )
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_alarm),
                                contentDescription = "알림",
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){

        }
    }
}