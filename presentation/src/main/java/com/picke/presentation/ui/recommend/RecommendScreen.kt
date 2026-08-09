package com.picke.presentation.ui.recommend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ui.component.AdFitBannerAd
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.recommend.component.RecommendItemCard
import com.picke.presentation.ui.recommend.component.RecommendListSkeleton
import com.picke.presentation.ui.theme.PickeTheme

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
        containerColor = PickeTheme.colors.surface,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "더 흥미로운 배틀도 있어요!",
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = PickeTheme.colors.surface,
                    actions = {
                        IconButton(onClick = onCloseClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_x),
                                contentDescription = "닫기",
                                tint = PickeTheme.colors.textPrimary
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            RecommendListSkeleton(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 카카오 애드핏 배너 광고 (리스트 맨 위)
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        AdFitBannerAd(adUnitId = BuildConfig.ADFIT_BANNER_320X100)
                    }
                }

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