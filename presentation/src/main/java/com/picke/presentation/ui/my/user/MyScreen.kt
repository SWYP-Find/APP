package com.picke.presentation.ui.my.user

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ui.component.AdFitBannerAd
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.my.user.componenet.CreditCard
import com.picke.presentation.ui.my.user.componenet.MyPageMenuItem
import com.picke.presentation.ui.my.user.componenet.MySkeleton
import com.picke.presentation.ui.my.user.model.MyPhilosopherUiModel
import com.picke.presentation.ui.my.user.model.MyUiState
import com.picke.presentation.ui.theme.PickeTheme

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
    val lifecycleOwner = LocalLifecycleOwner.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

    MyScreen(
        uiState = uiState,
        onNavigateToAlarm = onNavigateToAlarm,
        onNavigateToSetting = onNavigateToSetting,
        onNavigateToDiscussion = onNavigateToDiscussion,
        onNavigateToPhilosopher = onNavigateToPhilosopher,
        onNavigateToContent = onNavigateToContent,
        onNavigateToNotice = onNavigateToNotice,
        onNavigateToPoint = onNavigateToPoint,
    )
}

@Composable
fun MyScreen(
    uiState: MyUiState,
    onNavigateToAlarm: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToDiscussion: () -> Unit,
    onNavigateToPhilosopher: () -> Unit,
    onNavigateToContent: () -> Unit,
    onNavigateToNotice: () -> Unit,
    onNavigateToPoint: () -> Unit,
) {

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBrand,
        topBar = {
            CustomTopAppBar(
                backgroundColor = PickeTheme.colors.backgroundBrand,
                centerTitle = false,
                actions = {
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
                        IconButton(onClick = onNavigateToAlarm, modifier = Modifier.size(36.dp)) {
                            BadgedBox(
                                badge = {
                                    if (uiState.hasNewNotice) {
                                        Badge(
                                            containerColor = PickeTheme.colors.primary,
                                            modifier = Modifier.offset(x = 4.dp, y = (-4).dp)
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_alarm),
                                    contentDescription = stringResource(R.string.alarm),
                                    tint = PickeTheme.colors.textPrimary
                                )
                            }
                        }
                        IconButton(
                            onClick = onNavigateToSetting,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_setting),
                                contentDescription = stringResource(R.string.setting),
                                tint = PickeTheme.colors.textPrimary
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
                    onClick = onNavigateToPoint,
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
                PhilosopherTypeSection(
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AdFitBannerAd(adUnitId = BuildConfig.ADFIT_BANNER_320X100)
                }
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
        ProfileImage(
            model = profileImage ?: R.drawable.illust_mengzi,
            modifier = Modifier.size(52.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = nickname, style = PickeTheme.typography.h4SemiBold)

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = userHandle,
                style = PickeTheme.typography.b4Regular,
                color = PickeTheme.colors.textTertiary
            )
        }
    }
}

@Composable
fun PhilosopherTypeSection(
    philosopher: MyPhilosopherUiModel?,
    onClick: () -> Unit
) {
    val isLocked = philosopher == null || philosopher.philosopherType == "UNKNOWN"
    val displayImage = if (isLocked) R.drawable.img_lock else philosopher?.imageUrl
    val displayName = if (isLocked) "??형" else philosopher?.philosopherLabel ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surfaceTertiary)
            .border(1.dp, PickeTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            model = displayImage,
            modifier = Modifier.size(40.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.my_menu_philosopher),
                style = PickeTheme.typography.caption2Medium,
                color = PickeTheme.colors.textTertiary
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isLocked) displayName else "$displayName ",
                style = PickeTheme.typography.b3SemiBold,
                color = PickeTheme.colors.textSecondary
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_a),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = PickeTheme.colors.textPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {
    PickeTheme {
        MyScreen(
            uiState = MyUiState(),
            onNavigateToAlarm = { },
            onNavigateToSetting = { },
            onNavigateToDiscussion = { },
            onNavigateToPhilosopher = { },
            onNavigateToContent = { },
            onNavigateToNotice = { },
            onNavigateToPoint = { }
        )
    }
}