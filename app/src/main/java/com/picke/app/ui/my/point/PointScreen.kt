package com.picke.app.ui.my.point

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.app.R
import com.picke.app.ui.component.CustomConfirmDialog
import com.picke.app.ui.component.CustomReverseConfirmDialog
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Beige600
import com.picke.app.ui.theme.Beige800
import com.picke.app.ui.theme.Gray300
import com.picke.app.ui.theme.Gray400
import com.picke.app.ui.theme.Gray500
import com.picke.app.ui.theme.Gray900
import com.picke.app.ui.theme.Primary500
import com.picke.app.ui.theme.Primary800
import com.picke.app.ui.theme.Primary900
import com.picke.app.ui.theme.SwypTheme

data class PointHistoryUiModel(
    val title: String,
    val date: String,
    val point: Int,
    val type: String
)

@Composable
fun PointScreen(
    modifier: Modifier = Modifier,
    onBackClick : ()->Unit,
    onNavigateToMakeBattle : ()->Unit,
    viewModel: PointViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showChargeDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Beige200,
        contentWindowInsets = WindowInsets(0.dp),
        topBar={
            CustomTopAppBar(
                title = "포인트 내역",
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = Beige200,
                actions = {
                    IconButton(
                        onClick = {
                            showChargeDialog = true
                        }) {
                        Icon(
                            painterResource(R.drawable.ic_point),
                            contentDescription = stringResource(R.string.setting),
                            tint = Primary500
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        // 1. 로딩 상태 처리
        if (uiState.isLoading && uiState.pointList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary900)
            }
        }
        // 2. 빈 내역
        else if (uiState.pointList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "빈 화면 로고",
                    modifier = Modifier.size(width = 160.dp, height = 120.dp),
                    tint = Beige600
                )
                Text(
                    text = "아직 포인트 내역이 없습니다",
                    style = SwypTheme.typography.b3Regular,
                    color = Beige800
                )
            }
        }
        // 3. 정상적으로 데이터가 있을 때 리스트 노출
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement =  Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ){
                itemsIndexed(uiState.pointList) { index, pointItem ->
                    if (index >= uiState.pointList.size - 3) {
                        viewModel.loadPointHistory()
                    }

                    PointHistoryItem(item = pointItem)
                }

                // 포인트 내역 로딩중
                if (uiState.isLoading && uiState.pointList.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Primary900)
                        }
                    }
                }
            }
        }
    }

    if (showChargeDialog) {
        CustomReverseConfirmDialog(
            title = "나만의 배틀을 제안해주세요",
            message = "-30P를 사용해 원하는 주제를 제안하고,\n" +
                    "채택되면 +100P를 돌려받아요.",
            confirmText = "제안하기",
            dismissText = "뒤로가기",
            onConfirm = {
                showChargeDialog = false
                onNavigateToMakeBattle()
            },
            onDismiss = {
                showChargeDialog = false
            }
        )
    }
}


@Composable
fun PointHistoryItem(
    modifier: Modifier = Modifier,
    item: PointHistoryUiModel
) {
    val isEarned = item.point > 0
    val pointColor = if (isEarned) Primary800 else Gray500
    val pointText = if (isEarned) "+ ${item.point}P" else "${item.point}P"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // [왼쪽] 내용 & 날짜
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = item.title,
                    color = Gray900,
                    style = SwypTheme.typography.b3SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.date,
                    color = Gray300,
                    style = SwypTheme.typography.caption2Medium
                )
            }

            // [오른쪽] 포인트 & 적립/사용 상태
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = pointText,
                    color = pointColor,
                    style = SwypTheme.typography.b3SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.type,
                    color = Gray300,
                    style = SwypTheme.typography.caption2Medium
                )
            }
        }
    }

}