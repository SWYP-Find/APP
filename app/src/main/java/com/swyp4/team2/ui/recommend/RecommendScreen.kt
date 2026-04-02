package com.swyp4.team2.ui.recommend

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary900
import com.swyp4.team2.ui.theme.Secondary200
import com.swyp4.team2.ui.theme.SwypTheme
import com.swyp4.team2.ui.home.BattleOpinionBox

@Composable
fun RecommendScreen(
    viewModel: RecommendViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recommendList = uiState.recommendList

    Scaffold(
        containerColor = SwypTheme.colors.surface,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "더 흥미로운 배틀도 있어요!",
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = SwypTheme.colors.surface,
                    actions = {
                        IconButton(onClick = onCloseClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_x),
                                contentDescription = "닫기",
                                tint = Gray900
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary900)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(recommendList) { item ->
                    RecommendItemCard(
                        item = item,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onItemClick(item.battleId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendItemCard(
    item: RecommendUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        // 1. 최상단: 태그 및 시간/조회수 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(color = Beige600, shape = RoundedCornerShape(2.dp)) {
                Text(
                    text = "#${item.tags.firstOrNull() ?: "이슈"}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = SwypTheme.typography.label,
                    color = SwypTheme.colors.primary
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.ic_clock), null, Modifier.size(12.dp), tint = Gray400)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${item.audioDuration}분", style = SwypTheme.typography.label, color = Gray400)

                Spacer(modifier = Modifier.width(8.dp))

                Icon(painterResource(R.drawable.ic_eye), null, Modifier.size(12.dp), tint = Gray400)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${item.viewCount}", style = SwypTheme.typography.label, color = Gray400)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. 제목 및 요약
        Text(text = item.title, style = SwypTheme.typography.h5SemiBold, color = Gray900, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.summary,
            style = SwypTheme.typography.label,
            color = Gray400,
            maxLines = 2,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3. VS 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            BattleOpinionBox(
                modifier = Modifier.weight(1f),
                opinion = item.stanceA,
                name = item.representativeA,
                imageUrl = item.imageA
            )
            Surface(
                modifier = Modifier.size(40.dp).padding(6.dp),
                shape = CircleShape,
                color = Secondary200
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "VS", style = SwypTheme.typography.labelXSmall, color = Gray900)
                }
            }
            BattleOpinionBox(
                modifier = Modifier.weight(1f),
                opinion = item.stanceB,
                name = item.representativeB,
                imageUrl = item.imageB
            )
        }
    }
}