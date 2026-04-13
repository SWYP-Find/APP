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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.picke.app.R
import com.picke.app.ui.component.CustomConfirmDialog
import com.picke.app.ui.component.CustomReverseConfirmDialog
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Beige600
import com.picke.app.ui.theme.Gray300
import com.picke.app.ui.theme.Gray400
import com.picke.app.ui.theme.Gray500
import com.picke.app.ui.theme.Gray900
import com.picke.app.ui.theme.Primary500
import com.picke.app.ui.theme.Primary800
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
) {
    val mockData = listOf(
        PointHistoryUiModel("무료 충전", "2026.4.10", 10, "적립"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("무료 충전", "2026.4.10", 20, "적립"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
        PointHistoryUiModel("배틀 참여", "2026.4.10", -5, "사용"),
    )
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement =  Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ){
            items(mockData) { pointItem ->
                PointHistoryItem(item = pointItem)
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