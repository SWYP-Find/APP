package com.picke.app.ui.alarm

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.app.R
import com.picke.app.domain.model.AlarmItemBoard
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.component.SortFilterChip
import com.picke.app.ui.theme.SwypTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun AlarmScreen(
    onBackClick: () -> Unit,
    onNavigateToTodayBattle: (battleId: String) -> Unit,
    onNavigateToComment: (perspectiveId: String, commentId: String) -> Unit,
    onNavigateToPoint: () -> Unit,
    onNavigateToNotice: (Long) -> Unit,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val tabs = listOf(
        "전체" to "ALL",
        "콘텐츠" to "CONTENT",
        "공지사항" to "NOTICE",
        "이벤트" to "EVENT"
    )

    Scaffold(
        containerColor = SwypTheme.colors.backgroundBrand,
        modifier = Modifier.systemBarsPadding(),
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.alarm),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = SwypTheme.colors.backgroundBrand,
                actions = {
                    Text(
                        text = "모두 읽음",
                        style = SwypTheme.typography.b4Medium,
                        color = SwypTheme.colors.textTertiary,
                        modifier = Modifier
                            .clickable {
                                val hasUnreadAlarms = uiState.alarmList.any { !it.isRead }

                                if (hasUnreadAlarms) {
                                    viewModel.readAllAlarms()
                                } else {
                                    Toast.makeText(context, "이미 모든 공지를 읽었습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
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

            // 로딩중 일때
            if (uiState.isLoading) {
                AlarmListSkeleton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
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
                            painter = painterResource(id = R.drawable.logo_picke),
                            contentDescription = "빈 화면 로고",
                            modifier = Modifier.size(width = 160.dp, height = 120.dp),
                            tint = SwypTheme.colors.borderDefault
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "아직 도착한 알림이 없습니다",
                            style = SwypTheme.typography.b3Regular,
                            color = SwypTheme.colors.beige800
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
                        itemsIndexed(uiState.alarmList) { index, item ->
                            if (index >= uiState.alarmList.size - 2) {
                                viewModel.fetchAlarms()
                            }

                            AlarmCard(
                                item = item,
                                onClick = {
                                    if (!item.isRead) viewModel.readAlarm(item.notificationId)

                                    when (item.detailCode) {
                                        "NEW_BATTLE" ->
                                            onNavigateToTodayBattle(item.referenceId.toString())
                                        "COMMENT_LIKE", "NEW_COMMENT" ->
                                            if (item.perspectiveId != 0L)
                                                onNavigateToComment(item.perspectiveId.toString(), item.referenceId.toString())
                                        "CREDIT_EARNED" ->
                                            onNavigateToPoint()
                                        "POLICY_CHANGE" ->
                                            onNavigateToNotice(item.referenceId)
                                        // PROMOTION, VOTE_RESULT: 이동 없음
                                    }
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
        "CONTENT" -> {
            when (item.detailCode) {
                "NEW_BATTLE" -> R.drawable.ic_alarm_battle
                "COMMENT_LIKE" -> R.drawable.ic_alarm_like
                "NEW_COMMENT" -> R.drawable.ic_alarm_comment
                "CREDIT_EARNED" -> R.drawable.ic_alarm_point
                "VOTE_RESULT" -> R.drawable.ic_alarm_vote
                else -> R.drawable.ic_alarm_vote
            }
        }
        "NOTICE" -> R.drawable.ic_alarm_notice
        "EVENT" -> R.drawable.ic_alarm_calendar
        else -> R.drawable.ic_alarm_point
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
            .border(1.dp, SwypTheme.colors.borderDefault, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 좌측 아이콘
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 우측 텍스트 영역
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // [상단] 제목(카테고리명), 시간, 안읽음 점
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    style = SwypTheme.typography.caption2Medium,
                    color = SwypTheme.colors.textMuted,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.alignByBaseline()
                ) {
                    Text(
                        text = item.createdAt,
                        style = SwypTheme.typography.caption2Medium,
                        color = SwypTheme.colors.neutral200
                    )

                    if (!item.isRead) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(SwypTheme.colors.primary, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // [하단] 본문 내용
            Text(
                text = item.body,
                style = SwypTheme.typography.b3SemiBold,
                color = SwypTheme.colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
