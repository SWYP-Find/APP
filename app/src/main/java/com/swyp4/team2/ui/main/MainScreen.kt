package com.swyp4.team2.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.swyp4.team2.AppRoute
import com.swyp4.team2.ui.component.CustomBottomNavigationBar
import com.swyp4.team2.ui.curation.CurationScreen
import com.swyp4.team2.ui.explore.ExploreScreen
import com.swyp4.team2.ui.home.HomeScreen
import com.swyp4.team2.ui.my.MyScreen
import com.swyp4.team2.ui.my.content.ContentActivityScreen
import com.swyp4.team2.ui.my.discussion.DiscussionHistoryScreen
import com.swyp4.team2.ui.my.notice.NoticeEventScreen
import com.swyp4.team2.ui.my.philosopher.PhilosopherTypeScreen
import com.swyp4.team2.ui.my.setting.SettingScreen
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme
import com.swyp4.team2.ui.todaybattle.TodayBattleScreen

@Composable
fun MainScreen(
    rootNavController : NavController
){
    val mainNavController = rememberNavController()

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != BottomNavItem.TodayBattle.route

    val rootBackStackEntry by rootNavController.currentBackStackEntryAsState()
    val nextRoute = rootBackStackEntry?.savedStateHandle?.get<String>("next_route")

    LaunchedEffect(nextRoute) {
        if (nextRoute != null) {
            // 메모에 적힌 목적지(Curation)로 이동!
            mainNavController.navigate(nextRoute)

            // 🚨 이동했으면 메모를 찢어버려야 합니다! (안 지우면 다른 탭 갔다가 돌아올 때 또 무한 이동됨)
            rootBackStackEntry?.savedStateHandle?.remove<String>("next_route")
        }
    }

    Scaffold(
        containerColor = if (showBottomBar) SwypTheme.colors.surface else Gray900,
        bottomBar = {
            if (showBottomBar) {
                CustomBottomNavigationBar(mainNavController)
            }
        }
    ){ innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = if (showBottomBar) innerPadding.calculateTopPadding() else 0.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
                .background(if (showBottomBar) SwypTheme.colors.surface else Gray900)
        ){
            composable(BottomNavItem.Home.route){
                HomeScreen(
                    onNavigateToAlarm = {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    },
                    onNavigateToVote = { id ->
                        // 🔥 여기서 사전투표(PreVote) 혹은 투표 화면으로 이동!
                        rootNavController.navigate(AppRoute.PreVote.route)
                        // 나중에 API 연결 시 라우트 예시:
                        // rootNavController.navigate("pre_vote_route/$id")
                    },
                    onNavigateToTrendingBattle = {

                    },
                    onNavigateToNewBattle = {

                    },
                    onNavigateToBestBattle = {

                    },
                    onNavigateToTodayPicke = {

                    }
                )
            }
            composable(BottomNavItem.Explore.route){
                ExploreScreen(
                    onNavigateToAlarm = {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    },
                    onNavigateToVote = { id ->
                        // 🔥 여기서 VoteScreen으로 이동!
                        // 지금은 더미데이터라 단순히 화면만 넘기지만,
                        // 나중에는 API 통신을 위해 라우트에 id를 붙여서 넘길 수 있습니다.
                        rootNavController.navigate(AppRoute.PreVote.route)

                        // 💡 나중에 API 연결 시 라우트 예시:
                        // rootNavController.navigate("pre_vote_route/$id")
                    }
                )
            }
            composable(BottomNavItem.TodayBattle.route){
                TodayBattleScreen(
                    onBackClick = {
                        mainNavController.popBackStack()
                    },
                    onEnterBattle = {
                        rootNavController.navigate(AppRoute.PreVote.route)
                    }
                )
            }
            composable(BottomNavItem.My.route){
                MyScreen(
                    onNavigateToAlarm= {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    },
                    onNavigateToSetting = {
                        mainNavController.navigate(AppRoute.Setting.route)
                    },
                    onNavigateToDiscussion= {
                        mainNavController.navigate(AppRoute.DiscussionHistory.route)
                    },
                    onNavigateToPhilosopher= {
                        mainNavController.navigate(AppRoute.PhilosopherType.route)
                    },
                    onNavigateToContent= {
                        mainNavController.navigate(AppRoute.ContentActivity.route)
                    },
                    onNavigateToNotice={
                        mainNavController.navigate(AppRoute.NoticeEvent.route)
                    },
                )
            }


            composable(AppRoute.DiscussionHistory.route) {
                DiscussionHistoryScreen(
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
            composable(AppRoute.ContentActivity.route) {
                ContentActivityScreen(
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
            composable(AppRoute.PhilosopherType.route) {
                PhilosopherTypeScreen(
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
            composable(AppRoute.NoticeEvent.route) {
                NoticeEventScreen(
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
            composable(AppRoute.Setting.route){
                SettingScreen(
                    onBackClick = {
                        mainNavController.popBackStack()
                    },
                    onNavigateToSettingProfile = {
                        rootNavController.navigate(AppRoute.SettingProfile.route)
                    },
                    onNavigateToSettingAlarm = {
                        rootNavController.navigate(AppRoute.SettingAlarm.route)
                    },
                    onOpenWebLink = {
                        // 웹 링크 열기
                    }
                )
            }

            composable(AppRoute.Curation.route){
                CurationScreen(
                    onCloseClick = {
                        mainNavController.popBackStack()
                    },
                    onBackClick = {
                        mainNavController.popBackStack()
                    },
                    onItemClick = {
                        // 🔥 여기서 사전투표(PreVote) 혹은 투표 화면으로 이동!
                        rootNavController.navigate(AppRoute.PreVote.route)
                        // 나중에 API 연결 시 라우트 예시:
                        // rootNavController.navigate("pre_vote_route/$id")
                    }
                )
            }
        }
    }
}

