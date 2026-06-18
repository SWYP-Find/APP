package com.picke.app.ui.my.discussion

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.app.R
import com.picke.app.domain.model.MyBattleRecordItem
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.theme.SwypTheme

@Composable
fun DiscussionHistoryScreen(
    onBackClick: () -> Unit,
    // onNavigateToComment: (Long) -> Unit,
    onNavigateToDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: DiscussionHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val combinedList = uiState.agreeList + uiState.disagreeList

    Scaffold(
        containerColor = SwypTheme.colors.backgroundBrand,
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.my_menu_discussion),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = {onBackClick()},
                backgroundColor = SwypTheme.colors.backgroundBrand
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ){
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SwypTheme.colors.primaryDarkest)
                }
            } else {
                DiscussionHistoryList(
                    list = combinedList,
                    emptyMessage = "아직 참여한 배틀이 없습니다",
                    onItemClick = onNavigateToDetail,
                    onLoadMore = {
                        viewModel.loadMore("PRO")
                        viewModel.loadMore("CON")
                    }
                )
            }
        }
    }
}

@Composable
fun DiscussionHistoryList(
    list: List<MyBattleRecordItem>,
    emptyMessage: String,
    onItemClick: (String) -> Unit,
    onLoadMore: () -> Unit
) {
    if (list.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_picke),
                contentDescription = "빈 화면 로고",
                modifier = Modifier.size(width = 160.dp, height = 120.dp),
                tint = SwypTheme.colors.borderDefault
            )
            Text(
                text = emptyMessage,
                style = SwypTheme.typography.b3Regular,
                color = SwypTheme.colors.beige800
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(list) { index, item ->
                // 리스트 끝에서 2번째 아이템에 도달하면 다음 페이지 데이터 호출
                if (index >= list.size - 2) {
                    onLoadMore()
                }

                DiscussionHistoryCard(
                    item = item,
                    onClick = { onItemClick(item.battleId) }
                )
            }
        }
    }
}

@Composable
fun DiscussionHistoryCard(
    item: MyBattleRecordItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, SwypTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // [상단] 토론 주제
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = SwypTheme.colors.borderDefault,
                shape = RoundedCornerShape(2.dp)
            ) {
                Text(
                    text = "#${item.category ?: "이슈"}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = SwypTheme.typography.label,
                    color = SwypTheme.colors.primary
                )
            }
            Text(
                text = item.title,
                style = SwypTheme.typography.labelMedium,
                color = SwypTheme.colors.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // [중단] 내가 남긴 요약 내용
        Text(
            text = item.summary,
            style = SwypTheme.typography.b4Regular,
            color = SwypTheme.colors.neutral400,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        // [하단] 작성 날짜
        Text(
            text = item.createdAt,
            style = SwypTheme.typography.label,
            color = SwypTheme.colors.neutral200
        )
    }
}