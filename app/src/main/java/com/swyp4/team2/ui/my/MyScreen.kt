package com.swyp4.team2.ui.my

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.swyp4.team2.AppRoute
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Beige700
import com.swyp4.team2.ui.theme.Gray100
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray50
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Secondary600
import com.swyp4.team2.ui.theme.Secondary900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun MyScreen(
    onNavigateToAlarm: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToDiscussion: () -> Unit,
    onNavigateToPhilosopher: () -> Unit,
    onNavigateToContent: () -> Unit,
    onNavigateToNotice: () -> Unit
) {
    // 🌟 가상의 '새 알림' 상태 변수 (나중에는 서버에서 이 값을 받아오게 됩니다!)
    var hasUnreadNotification by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                backgroundColor = SwypTheme.colors.background,
                centerTitle = false,
                actions = {
                    IconButton(
                        onClick = {
                            onNavigateToAlarm()
                            hasUnreadNotification = false // 클릭했으니까 빨간 점 없애기 (선택사항)
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_alarm),
                            contentDescription = stringResource(R.string.alarm),
                            tint = Gray900
                        )
                    }
                    IconButton(
                        onClick = {
                            onNavigateToSetting()
                        }) {
                        Icon(
                            painterResource(R.drawable.ic_setting),
                            contentDescription =  stringResource(R.string.setting),
                            tint = Gray900
                        )
                    }
                }
            )
        },
        containerColor = SwypTheme.colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            ProfileSection(
                nickname = "사색하는 고양이",
                userType = "칸트형",
                userHandle = "@user_code"
            )

            Spacer(modifier = Modifier.height(20.dp))

            CreditCard(credit = 240)

            Spacer(modifier = Modifier.height(24.dp))

            MyPageMenuItem(
                title = stringResource(R.string.my_menu_discussion),
                onClick = { onNavigateToDiscussion() }
            )
            MyPageMenuItem(
                title = stringResource(R.string.my_menu_philosopher),
                onClick = { onNavigateToPhilosopher() }
            )
            MyPageMenuItem(
                title = stringResource(R.string.my_menu_content),
                onClick = { onNavigateToContent() }
            )
            MyPageMenuItem(
                title = stringResource(R.string.my_menu_notice),
                onClick = { onNavigateToNotice() }
            )
        }
    }
}

@Composable
fun ProfileSection(
    nickname: String,
    userType: String,
    userHandle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이름 & 유형 & ID
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = nickname, style = SwypTheme.typography.h4SemiBold)
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .background(Beige500, RoundedCornerShape(12.dp))
                        .border(1.dp, Beige700, RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text(text = userType, style = SwypTheme.typography.labelXSmall, color = SwypTheme.colors.primary)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = userHandle, style = SwypTheme.typography.b4Regular, color = Gray300)
        }
        // 프로필 이미지
        Box(
            modifier = Modifier.size(52.dp)
                .background(Secondary600, CircleShape),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}

@Composable
fun CreditCard(
    credit: Int,
    onChargeClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(SwypTheme.colors.primary)
            .clickable { onChargeClick() }
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [왼쪽] 내 크레딧 정보
        Text(
            text = stringResource(R.string.my_credit_title, credit),
            style = SwypTheme.typography.label,
            color = Gray50
        )

        // [오른쪽] 무료 충전하기 텍스트
        Text(
            text = stringResource(R.string.my_charge_free),
            style = SwypTheme.typography.label,
            color = Gray50
        )
    }
}

@Composable
fun MyPageMenuItem(
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() }
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = SwypTheme.typography.b1Medium
            )
            Icon(
                painterResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Gray900
            )
        }
        HorizontalDivider(color = Gray200, thickness = 1.dp)
    }
}