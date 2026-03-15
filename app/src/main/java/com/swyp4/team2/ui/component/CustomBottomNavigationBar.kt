package com.swyp4.team2.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.swyp4.team2.ui.main.BottomNavItem
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun CustomBottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Battle,
        BottomNavItem.My
    )

    NavigationBar(
        containerColor = SwypTheme.colors.surface
    ) {
        // 현재 화면의 상태를 추적하는 관찰자
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        // 관찰한 정보에서 현재 화면의 주소
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach{item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = SwypTheme.typography.label
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { //탭을 이동할때마다 스택이 무한히 쌓이지 않도록 시작점을 기준으로 이전 기록을 지움
                            saveState = true // 지워지는 화면의 현재 상태(스크롤 위치 등)을 임시 저장함
                        }
                        launchSingleTop = true // 사용자가 같은 탭을 여러번 눌러도 화면이 영러 겹으로 중복 생성되는 것을 방지함
                        restoreState = true // 예전에 방문했던 탭으로 돌아왔을때 임시 저장해둔 상태(스크롤 위치 등)를 그대로 복원함
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Gray900,
                    selectedTextColor = Gray900,
                    unselectedIconColor = Gray900.copy(alpha = 0.4f),
                    unselectedTextColor = Gray900.copy(alpha = 0.4f),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}