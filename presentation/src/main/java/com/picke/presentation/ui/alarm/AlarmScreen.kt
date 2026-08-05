package com.picke.presentation.ui.alarm

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.domain.feature.alarm.model.AlarmItemBoard
import com.picke.presentation.R
import com.picke.presentation.ui.alarm.component.AlarmCard
import com.picke.presentation.ui.alarm.component.AlarmListSkeleton
import com.picke.presentation.ui.alarm.model.AlarmUiEvent
import com.picke.presentation.ui.alarm.model.AlarmUiState
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.SortFilterChip
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AlarmScreen(
    onBackClick: () -> Unit,
    onNavigateToPreVote: (battleId: String) -> Unit,
    onNavigateToComment: (perspectiveId: String, commentId: String) -> Unit,
    onNavigateToPoint: () -> Unit,
    onNavigateToNotice: (Long) -> Unit,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AlarmUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AlarmScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onReadAllClick = {
            val hasUnreadAlarms = uiState.alarmList.any { !it.isRead }
            if (hasUnreadAlarms) {
                viewModel.readAllAlarms()
            } else {
                Toast.makeText(context, "이미 모든 공지를 읽었습니다.", Toast.LENGTH_SHORT).show()
            }
        },
        onCategoryClick = { viewModel.setCategory(it) },
        onFetchMore = { viewModel.fetchAlarms() },
        onAlarmClick = { item ->
            if (!item.isRead) viewModel.readAlarm(item.notificationId)
            when (item.detailCode) {
                "NEW_BATTLE" -> onNavigateToPreVote(item.referenceId.toString())
                "COMMENT_LIKE", "NEW_COMMENT" -> if (item.perspectiveId != 0L) onNavigateToComment(
                    item.perspectiveId.toString(),
                    item.referenceId.toString()
                )

                "CREDIT_EARNED" -> onNavigateToPoint()
                "POLICY_CHANGE" -> onNavigateToNotice(item.referenceId)
            }
        }
    )
}

@Composable
fun AlarmScreen(
    uiState: AlarmUiState,
    onBackClick: () -> Unit,
    onReadAllClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onFetchMore: () -> Unit,
    onAlarmClick: (AlarmItemBoard) -> Unit
) {
    val tabs = listOf(
        "전체" to "ALL",
        "콘텐츠" to "CONTENT",
        "공지사항" to "NOTICE",
        "이벤트" to "EVENT"
    )

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBrand,
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.alarm),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = onBackClick,
                backgroundColor = PickeTheme.colors.backgroundBrand,
                actions = {
                    Text(
                        text = "모두 읽음",
                        style = PickeTheme.typography.b4Medium,
                        color = PickeTheme.colors.textTertiary,
                        modifier = Modifier
                            .clickable { onReadAllClick() }
                            .padding(end = 4.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
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
                        onClick = { onCategoryClick(categoryCode) }
                    )
                }
            }

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
                            tint = PickeTheme.colors.borderDefault
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "아직 도착한 알림이 없습니다",
                            style = PickeTheme.typography.b3Regular,
                            color = PickeTheme.colors.beige800
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
                                onFetchMore()
                            }

                            AlarmCard(
                                item = item,
                                onClick = { onAlarmClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmScreenPreview() {
    PickeTheme {
        AlarmScreen(
            uiState = AlarmUiState(alarmList = DummyData.dummyAlarmList),
            onBackClick = {},
            onReadAllClick = {},
            onCategoryClick = {},
            onFetchMore = {},
            onAlarmClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmScreenPreview2() {
    PickeTheme {
        AlarmScreen(
            uiState = AlarmUiState(),
            onBackClick = {},
            onReadAllClick = {},
            onCategoryClick = {},
            onFetchMore = {},
            onAlarmClick = {}
        )
    }
}