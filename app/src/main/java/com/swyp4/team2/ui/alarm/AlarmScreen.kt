package com.swyp4.team2.ui.alarm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.AlarmItemBoard
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.component.SortFilterChip
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige800
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.Primary900
import com.swyp4.team2.ui.theme.SwypTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun AlarmScreen(
    onBackClick:()->Unit,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabs = listOf(
        "전체" to "ALL",
        "콘텐츠" to "CONTENT",
        "공지사항" to "NOTICE",
        "이벤트" to "EVENT"
    )

    Scaffold(
        containerColor = Beige200,
        modifier = Modifier.systemBarsPadding(),
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.alarm),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = Beige200,
                actions = {
                    Text(
                        text = "모두 읽음",
                        style = SwypTheme.typography.b4Medium,
                        color = Gray500,
                        modifier = Modifier
                            .clickable { viewModel.readAllAlarms() }
                            .padding(end = 4.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { (tabName, categoryCode) ->
                    SortFilterChip(
                        text = tabName,
                        isSelected = uiState.selectedCategory == categoryCode,
                        onClick = { viewModel.setCategory(categoryCode) }
                    )
                }
            }

            // 💡 2. 로딩 상태에 따라 하단(리스트 영역)만 갈아끼웁니다
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary900)
                }
            } else {
                if (uiState.alarmList.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logo),
                            contentDescription = "빈 화면 로고",
                            modifier = Modifier.size(width = 160.dp, height = 120.dp),
                            tint = Beige600
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "아직 도착한 알림이 없습니다",
                            style = SwypTheme.typography.b3Regular,
                            color = Beige800
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.alarmList) { item ->
                            AlarmCard(
                                item = item,
                                onClick = {
                                    if (!item.isRead) viewModel.readAlarm(item.notificationId)
                                    // TODO: 알림 클릭 시 해당 화면으로 이동하는 로직 추가
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlarmCard(
    item: AlarmItemBoard,
    onClick: () -> Unit
) {
    val iconRes = when (item.category) {
        "CONTENT" -> R.drawable.ic_alarm_vote
        "NOTICE" -> R.drawable.ic_alarm_notice
        "EVENT" -> R.drawable.ic_alarm_calendar
        else -> R.drawable.ic_alarm_point
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
            .border(1.dp, Beige600, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 좌측 아이콘
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(24.dp)
                .padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 우측 텍스트 영역
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // [상단] 제목(카테고리명), 시간, 안읽음 점
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = SwypTheme.typography.caption2Medium,
                    color = Gray300
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatTimeAgo(item.createdAt),
                        style = SwypTheme.typography.caption2Medium,
                        color = Gray200
                    )

                    if (!item.isRead) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Primary500, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // [하단] 본문 내용
            Text(
                text = item.body,
                style = SwypTheme.typography.b3SemiBold,
                color = Gray900,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

fun formatTimeAgo(isoTimeString: String): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val past = format.parse(isoTimeString) ?: return ""

        val now = Date()
        val seconds = (now.time - past.time) / 1000

        when {
            seconds < 60 -> "방금 전"
            seconds < 3600 -> "${seconds / 60}분 전"
            seconds < 86400 -> "${seconds / 3600}시간 전"
            else -> "${seconds / 86400}일 전"
        }
    } catch (e: Exception) {
        ""
    }
}