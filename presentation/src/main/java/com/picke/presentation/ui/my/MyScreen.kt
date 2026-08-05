package com.picke.presentation.ui.my

import android.app.Activity
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.domain.feature.mypage.model.MyPhilosopher
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ui.component.AdFitBannerAd
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.SwypTheme

@Composable
fun MyScreen(
    onNavigateToAlarm: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToDiscussion: () -> Unit,
    onNavigateToPhilosopher: () -> Unit,
    onNavigateToContent: () -> Unit,
    onNavigateToNotice: () -> Unit,
    onNavigateToPoint: () -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchMyInfo()
                viewModel.fetchUnreadAlarmStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        containerColor = SwypTheme.colors.backgroundBrand,
        topBar = {
            CustomTopAppBar(
                backgroundColor = SwypTheme.colors.backgroundBrand,
                centerTitle = false,
                actions = {
                    // 벨 배지(미읽음 여부)는 API 응답 후에야 확정되므로,
                    // 그 전까지는 아이콘 자리도 스켈레톤과 동일하게 shimmer로 보여준다.
                    if (uiState.isLoading || uiState.isAlarmStatusLoading) {
                        repeat(2) {
                            Box(
                                modifier = Modifier.size(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .shimmer()
                                )
                            }
                        }
                    } else {
                        // 배지는 서버의 미읽음 여부 응답으로만 갱신한다.
                        // (여기서 임의로 숨기면 알림함에서 돌아올 때 배지가 다시 나타나는 깜빡임이 생긴다)
                        IconButton(onClick = onNavigateToAlarm, modifier = Modifier.size(36.dp)) {
                            BadgedBox(
                                badge = {
                                    if (uiState.hasNewNotice) {
                                        Badge(
                                            containerColor = SwypTheme.colors.primary,
                                            modifier = Modifier.offset(x = 4.dp, y = (-4).dp)
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_alarm),
                                    contentDescription = stringResource(R.string.alarm),
                                    tint = SwypTheme.colors.textPrimary
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                onNavigateToSetting()
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_setting),
                                contentDescription = stringResource(R.string.setting),
                                tint = SwypTheme.colors.textPrimary
                            )
                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        if (uiState.isLoading) {
            MySkeleton(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            )
        } else {
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
                    onClick = {
                        onNavigateToPoint()
                    },
                    onChargeClick = {
                        // AdMob 광고 로직 비활성화 (추후 재사용 예정)
                        // activity?.let {
                        // val isAdReady = viewModel.adMobManager.showAd(
                        //     activity = it,
                        //     placement = "mypage_charge",
                        //     onRewardEarned = {
                        //         viewModel.refreshPointsAfterAd()
                        //         uiState.profile?.userTag?.let { tag -> viewModel.adMobManager.loadAd(userId = tag) }
                        //         Toast.makeText(context, "20포인트가 지급되었습니다.", Toast.LENGTH_SHORT).show()
                        //     }
                        // )
                        //
                        // if (!isAdReady) {
                        //     Toast.makeText(
                        //         context,
                        //         "아직 광고가 준비되지 않았습니다.\n잠시 후 다시 시도해주세요.",
                        //         Toast.LENGTH_SHORT
                        //     ).show()
                        // }
                        // } ?: run {
                        //     Toast.makeText(context, "광고를 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        // }
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

                Spacer(modifier = Modifier.height(24.dp))

                // 카카오 애드핏 배너 광고 (마이 탭 최하단)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    AdFitBannerAd(adUnitId = BuildConfig.ADFIT_BANNER_320X100)
                }

                Spacer(modifier = Modifier.height(24.dp))
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
            model = profileImage ?: R.drawable.illust_mengzi,
            modifier = Modifier.size(52.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        // 이름 & 유형 & ID
        Column {
            Text(text = nickname, style = SwypTheme.typography.h4SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = userHandle,
                style = SwypTheme.typography.b4Regular,
                color = SwypTheme.colors.textTertiary
            )
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surfaceTertiary)
            .border(1.dp, SwypTheme.colors.borderDefault, RoundedCornerShape(2.dp))
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
                color = SwypTheme.colors.textTertiary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isLocked) displayName else "$displayName ",
                style = SwypTheme.typography.b3SemiBold,
                color = SwypTheme.colors.textSecondary
            )
        }

        // [오른쪽] 화살표
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_a),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = SwypTheme.colors.textPrimary
        )
    }
}

@Composable
fun CreditCard(
    credit: Int,
    onClick: () -> Unit,
    onChargeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.primaryDark)
            .clickable { onClick() }
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
                    .background(color = SwypTheme.colors.secondary300, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "P",
                    style = SwypTheme.typography.b5Medium,
                    color = SwypTheme.colors.textSecondary
                )
            }

            // 텍스트 영역
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.my_point),
                    style = SwypTheme.typography.b3Regular,
                    color = SwypTheme.colors.surfaceDefault
                )
                Text(
                    text = credit.toString(),
                    style = SwypTheme.typography.b3Regular,
                    color = SwypTheme.colors.secondary700
                )
            }
        }

        // [오른쪽] 무료 충전 버튼 (UI 비활성화)
        // Box(
        //     modifier = Modifier
        //         .clip(RoundedCornerShape(4.dp))
        //         .background(SwypTheme.colors.secondary300)
        //         .clickable { onChargeClick() }
        //         .padding(horizontal = 6.dp, vertical = 4.dp),
        //     contentAlignment = Alignment.Center
        // ) {
        //     Text(
        //         text = stringResource(R.string.my_charge_free),
        //         style = SwypTheme.typography.label,
        //         color = SwypTheme.colors.textPrimary
        //     )
        // }
    }
}

@Composable
fun MyPageMenuItem(
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = SwypTheme.typography.b3SemiBold,
                color = SwypTheme.colors.textSecondary
            )
            Icon(
                painterResource(R.drawable.ic_arrow_right_a),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = SwypTheme.colors.textPrimary
            )
        }
        HorizontalDivider(color = SwypTheme.colors.borderDefault, thickness = 1.dp)
    }
}