package com.swyp4.team2.ui.my

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.swyp4.team2.AppRoute
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.MyPhilosopher
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.component.ProfileImage
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige50
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige700
import com.swyp4.team2.ui.theme.Gray100
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray50
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary800
import com.swyp4.team2.ui.theme.Primary900
import com.swyp4.team2.ui.theme.Secondary200
import com.swyp4.team2.ui.theme.Secondary300
import com.swyp4.team2.ui.theme.Secondary600
import com.swyp4.team2.ui.theme.Secondary700
import com.swyp4.team2.ui.theme.Secondary900
import com.swyp4.team2.ui.theme.SwypTheme
import com.swyp4.team2.util.AdMobManager

@Composable
fun MyScreen(
    onNavigateToAlarm: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToDiscussion: () -> Unit,
    onNavigateToPhilosopher: () -> Unit,
    onNavigateToContent: () -> Unit,
    onNavigateToNotice: () -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity

    val adMobManager = remember { AdMobManager(context) }
    LaunchedEffect(uiState.profile?.userTag) {
        uiState.profile?.userTag?.let {
            adMobManager.loadAd(userId = it)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchMyInfo()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        containerColor = Beige200,
        topBar = {
            CustomTopAppBar(
                backgroundColor = Beige200,
                centerTitle = false,
                actions = {
                    /*IconButton(
                        onClick = {
                            onNavigateToAlarm()
                            viewModel.readNotice()
                        }
                    ) {
                        Box {
                            Icon(
                                painterResource(R.drawable.ic_alarm),
                                contentDescription = stringResource(R.string.alarm),
                                tint = Gray900
                            )

                            if (uiState.hasNewNotice) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(x = 2.dp, y = (-2).dp)
                                        .background(Color(0xFFFF5454), CircleShape)
                                )
                            }
                        }
                    }*/
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
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary900)
            }
        }
        else {
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                ProfileSection(
                    nickname = uiState.profile?.nickname ?: "사용자",
                    userHandle = uiState.profile?.userTag?.let { "@$it" } ?: "",
                    profileImage = uiState.profile?.characterImageUrl
                )

                Spacer(modifier = Modifier.height(20.dp))

                CreditCard(
                    credit = uiState.tier?.currentPoint ?: 0,
                    onChargeClick = {
                        activity?.let {
                        val isAdReady = adMobManager.showAd(
                            activity = it,
                            onRewardEarned = {
                                viewModel.refreshPointsAfterAd()
                                uiState.profile?.userTag?.let { tag -> adMobManager.loadAd(userId = tag) }
                            }
                        )

                        if (!isAdReady) {
                            Toast.makeText(
                                context,
                                "아직 광고가 준비되지 않았습니다.\n잠시 후 다시 시도해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } ?: run {
                        Toast.makeText(context, "광고를 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PhilosopherTypeCard(
                    philosopher = uiState.philosopher,
                    onClick = { onNavigateToPhilosopher() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                MyPageMenuItem(
                    title = stringResource(R.string.my_menu_discussion),
                    onClick = { onNavigateToDiscussion() }
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
}

@Composable
fun ProfileSection(
    nickname: String,
    userHandle: String,
    profileImage: Any?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 프로필 이미지
        ProfileImage(
            model = profileImage ?: R.drawable.ic_profile_mengzi,
            modifier = Modifier.size(52.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        // 이름 & 유형 & ID
        Column {
            Text(text = nickname, style = SwypTheme.typography.h4SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = userHandle, style = SwypTheme.typography.b4Regular, color = Gray500)
        }
    }
}

@Composable
fun PhilosopherTypeCard(
    philosopher: MyPhilosopher?,
    onClick: () -> Unit
) {
    val isLocked = philosopher == null || philosopher.philosopherType == "UNKNOWN"
    val displayImage = if (isLocked) R.drawable.img_lock else philosopher?.imageUrl
    val displayName = if (isLocked) "??형" else philosopher?.philosopherLabel ?: ""
    val displayDesc = if (isLocked) "나만의 철학자를 찾아보세요" else philosopher?.description ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(Beige400)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [왼쪽] 철학자 아이콘
        ProfileImage(
            model = displayImage,
            modifier = Modifier.size(40.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        // [가운데] 텍스트 영역
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.my_menu_philosopher),
                style = SwypTheme.typography.caption2Medium,
                color = Gray500
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isLocked) displayName else "$displayName - $displayDesc",
                style = SwypTheme.typography.b3SemiBold,
                color = Gray700
            )
        }

        // [오른쪽] 화살표
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_a),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = Gray900
        )
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
            .clip(RoundedCornerShape(2.dp))
            .background(Primary800)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // C 아이콘 + 내 크레딧 정보
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. 'C' 동그라미 아이콘
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color = Secondary300, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "P",
                    style = SwypTheme.typography.b5Medium,
                    color = Gray700
                )
            }

            // 텍스트 영역
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.my_point),
                    style = SwypTheme.typography.b3Regular,
                    color = Beige50
                )
                Text(
                    text = credit.toString(),
                    style = SwypTheme.typography.b3Regular,
                    color = Secondary700
                )
            }
        }

        // [오른쪽] 무료 충전 버튼
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Secondary300)
                .clickable { onChargeClick() }
                .padding(horizontal = 6.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.my_charge_free),
                style = SwypTheme.typography.label,
                color = Gray900
            )
        }
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
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = SwypTheme.typography.b3SemiBold,
                color = Gray700
            )
            Icon(
                painterResource(R.drawable.ic_arrow_right_a),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Gray900
            )
        }
        HorizontalDivider(color = Beige600, thickness = 1.dp)
    }
}