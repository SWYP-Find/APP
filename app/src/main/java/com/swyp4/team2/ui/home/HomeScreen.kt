package com.swyp4.team2.ui.home

import android.graphics.Picture
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.ChattingLoadingAnimation
import com.swyp4.team2.ui.component.CopyLinkButton
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.component.InstagramShareButton
import com.swyp4.team2.ui.component.KakaoShareButton
import com.swyp4.team2.AppRoute
import com.swyp4.team2.ui.home.model.BattleProfile
import com.swyp4.team2.ui.home.model.BestBattleItem
import com.swyp4.team2.ui.home.model.EditorPickItem
import com.swyp4.team2.ui.home.model.NewBattleItem
import com.swyp4.team2.ui.home.model.TodayPickeItem
import com.swyp4.team2.ui.home.model.TrendingBattleItem
import com.swyp4.team2.ui.home.model.dummyBestBattleList
import com.swyp4.team2.ui.home.model.dummyEditorPickList
import com.swyp4.team2.ui.home.model.dummyNewBattleList
import com.swyp4.team2.ui.home.model.dummyPickeList
import com.swyp4.team2.ui.home.model.dummyTrendingList
import com.swyp4.team2.ui.theme.Beige300
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige50
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige700
import com.swyp4.team2.ui.theme.Beige800
import com.swyp4.team2.ui.theme.Beige900
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Secondary200
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun HomeScreen(
    onNavigateToAlarm: ()->Unit,
    onNavigateToVote: (Int) -> Unit,
    onNavigateToTrendingBattle : ()->Unit,
    onNavigateToBestBattle : ()->Unit,
    onNavigateToTodayPicke : ()->Unit,
    onNavigateToNewBattle : ()->Unit,
) {
    var hasUnreadNotification by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()


    Scaffold(
        containerColor = SwypTheme.colors.surface,
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                centerTitle = false,
                backgroundColor = SwypTheme.colors.surface,
                actions = {
                    IconButton(
                        onClick = {
                            onNavigateToAlarm()
                            hasUnreadNotification = false
                        }
                    ) {
                        BadgedBox(
                            badge = {
                                if (hasUnreadNotification) {
                                    Badge(
                                        containerColor = SwypTheme.colors.primary
                                    )
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_alarm),
                                contentDescription = "알림",
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(SwypTheme.colors.surface)
                .verticalScroll(scrollState)
        ){
            // 에디터 픽 섹션
            EditorPickSection(
                items = dummyEditorPickList,
                onItemClick = onNavigateToVote
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 지금 뜨는 배틀
            Column(
                modifier = Modifier.fillMaxWidth()
            ){
                Box(modifier = Modifier.padding(horizontal = 16.dp)){
                    HomeSectionHeader(
                        title = stringResource(R.string.home_section_trending),
                        highlightText = stringResource(R.string.home_highlight_battle),
                        onMoreClick = {
                            onNavigateToTrendingBattle()
                        }
                    )
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    items(dummyTrendingList){item ->
                        TrendingBattleCard(
                            item = item,
                            onClick = { onNavigateToVote(item.id)}
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

            // Best 배틀
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // 전체 섹션 여백
            ) {
                HomeSectionHeader(
                    title = stringResource(R.string.home_section_best),
                    highlightText = stringResource(R.string.home_highlight_battle),
                    onMoreClick = {
                        onNavigateToBestBattle()
                    }
                )

                dummyBestBattleList.take(3).forEach { item ->
                    BestBattleRankItem(
                        item = item,
                        onClick = { onNavigateToVote(item.id)}
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 오늘의 Picke
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                HomeSectionHeader(
                    title = stringResource(R.string.home_section_today_picke),
                    highlightText = stringResource(R.string.home_highlight_picke),
                    onMoreClick = {
                        onNavigateToTodayPicke()
                    }
                )

                dummyPickeList.forEach { item ->
                    TodayPickeCard(item = item)
                    Spacer(modifier = Modifier.height(16.dp)) // 카드와 카드 사이 간격
                }

            }

            Spacer(modifier = Modifier.height(32.dp))

            // 새로운 배틀
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                HomeSectionHeader(
                    title = stringResource(R.string.home_section_new),
                    highlightText = stringResource(R.string.home_highlight_battle),
                    onMoreClick = {
                        onNavigateToNewBattle()
                    }
                )

                // 리스트 형태 (여기선 take(3) 등으로 제한하거나 전체 리스트 출력)
                dummyNewBattleList.forEach { item ->
                    NewBattleCard(
                        item = item,
                        onClick = { onNavigateToVote(item.id)}
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        }
    }
}

@Composable
fun EditorPickSection(
    items: List<EditorPickItem>,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { items.size })

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth()
    ) { page ->
        val item = items[page]
        EditorPickCard(
            item = item,
            currentIndex = page + 1,
            totalCount = items.size,
            modifier = Modifier.clickable { onItemClick(item.id) }
        )
    }
}

@Composable
fun EditorPickCard(
    item: EditorPickItem,
    currentIndex: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        // [상단] EDITOR PICK 라벨 & 페이지네이션
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // EDITOR PICK 뱃지
            Surface(
                color = SwypTheme.colors.primary,
                shape = RoundedCornerShape(2.dp)

            ) {
                Text(
                    text = stringResource(R.string.home_editor_pick),
                    style = SwypTheme.typography.caption2SemiBold,
                    color = Secondary200,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            // 페이지네이션 (1/10)
            Surface(
                color = Gray500.copy(alpha = 0.8f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = buildAnnotatedString {

                        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                            append(currentIndex.toString())
                        }

                        withStyle(style = SpanStyle(color = Beige50.copy(alpha = 0.6f))) {
                            append("/$totalCount")
                        }
                    },
                    style = SwypTheme.typography.labelXSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        // [중앙] 이미지와 VS 텍스트 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.8f)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "Editor Pick Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 이미지 위에 글씨가 잘 보이도록 약간의 어두운 오버레이 추가
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            // VS 텍스트
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 텍스트 그림자 효과 (이미지 위에서 가독성 확보)
                val textStyle = SwypTheme.typography.h4SemiBold.copy(
                    shadow = Shadow(color = Color.Black, offset = Offset(2f, 2f), blurRadius = 4f)
                )

                Text(text = item.leftOpinion, style = textStyle, color = Color.White)

                Icon(
                    painter = painterResource(R.drawable.ic_versus),
                    contentDescription = null,
                    tint = Color.White
                )

                Text(text = item.rightOpinion, style = textStyle, color = Color.White)
            }
        }

        // [하단] 타이틀 및 상세 정보 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = item.title,
                style = SwypTheme.typography.h4SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                style = SwypTheme.typography.label,
                color = Gray400
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 태그
                Text(
                    text = item.tags.joinToString(" ") { "#$it" },
                    style = SwypTheme.typography.label,
                    color = Gray300
                )

                // 조회수
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye),
                        contentDescription = "Views",
                        modifier = Modifier.size(16.dp),
                        tint = Gray400
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.viewCount,
                        style = SwypTheme.typography.label,
                        color = Gray300
                    )
                }
            }
        }
    }
}

@Composable
fun HomeSectionHeader(
    title: String,
    highlightText: String ?= null,
    onMoreClick: ()->Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        val annotatedTitle = buildAnnotatedString{
            if(highlightText != null && title.contains(highlightText)){
                val startIndex = title.indexOf(highlightText)
                val endIndex = startIndex + highlightText.length

                append(title.substring(0, startIndex))

                withStyle(
                    style = SpanStyle(color = SwypTheme.colors.primary)
                ){
                    append(highlightText)
                }

                append(title.substring(endIndex))
            } else{
                append(title)
            }
        }
        Text(
            text = annotatedTitle,
            style = SwypTheme.typography.h3Bold,
            color = Gray900
        )
        Text(
            text = stringResource(R.string.more),
            style = SwypTheme.typography.b4Medium,
            color = Gray500,
            modifier = Modifier.clickable{ onMoreClick() }
        )
    }
}


@Composable
fun TrendingBattleCard(
    item: TrendingBattleItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(220.dp)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .clickable { onClick() }
            .padding(12.dp)
    ){
        // 썸네일
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f),
            shape = RoundedCornerShape(2.dp),
            border = BorderStroke(4.dp, Beige700),
        ){
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "Trending Thumbnail",
                modifier = Modifier.clip(RoundedCornerShape(2.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 카테고리 태그
        Surface(
            color = Beige600,
            shape = RoundedCornerShape(2.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                text = "#${item.category}",
                style = SwypTheme.typography.labelXSmall,
                color = SwypTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 제목
        Text(
            text = item.title,
            style = SwypTheme.typography.h5SemiBold,
            color = Gray900,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        // tts 시간 & 조회수
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Gray400
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = item.timeAgo, style = SwypTheme.typography.label, color = Gray400)

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_eye),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Gray400
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = item.viewCount, style = SwypTheme.typography.label, color = Gray400)
        }
    }
}

@Composable
fun BestBattleRankItem(
    item: BestBattleItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            val rankColor = if (item.rank == 1) SwypTheme.colors.primary else Beige600

            // 순위
            Text(
                text = item.rank.toString(),
                style = SwypTheme.typography.h1SemiBold,
                color = rankColor,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(24.dp)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 우측 콘텐츠 영역
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // VS 뱃지
                Surface(
                    color = Beige600,
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Text(
                        text = item.vsBadge,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = SwypTheme.typography.labelXSmall,
                        color = SwypTheme.colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 제목
                Text(
                    text = item.title,
                    style = SwypTheme.typography.h4SemiBold,
                    color = Gray900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 태그 및 메타 정보
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 해시태그들
                    Text(
                        text = item.tags.joinToString(" ") { "#$it" },
                        style = SwypTheme.typography.label,
                        color = Gray500
                    )

                    // 시간 & 조회수 묶음
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = "시간",
                            modifier = Modifier.size(14.dp),
                            tint = Gray400
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = item.timeAgo,
                            style = SwypTheme.typography.label,
                            color = Gray400
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            painter = painterResource(R.drawable.ic_eye),
                            contentDescription = "조회수",
                            modifier = Modifier.size(14.dp),
                            tint = Gray400
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = item.viewCount,
                            style = SwypTheme.typography.label,
                            color = Gray400
                        )
                    }
                }
            }
        }

        // 2. 아이템 하단 구분선
        HorizontalDivider(
            color = Beige400,
            thickness = 1.dp,
        )
    }
}

@Composable
fun NewBattleCard(
    item: NewBattleItem,
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
    ){
        // 1. 상단 태그 및 메타 정보
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 상단 태그
            Surface(
                color = Beige600,
                shape = RoundedCornerShape(2.dp)
            ) {
                Text(
                    text = "#${item.category}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = SwypTheme.typography.label,
                    color = SwypTheme.colors.primary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.ic_clock), null, Modifier.size(12.dp), tint = Gray400)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.timeAgo, style = SwypTheme.typography.label, color = Gray400)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(painterResource(R.drawable.ic_eye), null, Modifier.size(12.dp), tint = Gray400)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.viewCount, style = SwypTheme.typography.label, color = Gray400)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. 제목 및 설명
        Text(text = item.title, style = SwypTheme.typography.h5SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = item.description, style = SwypTheme.typography.label, color = Gray400)

        Spacer(modifier = Modifier.height(16.dp))

        // 3. VS 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 왼쪽 의견
            BattleOpinionBox(modifier = Modifier.weight(1f), profile = item.leftProfile)

            // 가운데 VS 원형 뱃지
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .padding(6.dp),
                shape = CircleShape,
                color = Secondary200,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "VS", style = SwypTheme.typography.labelXSmall, color = Gray900)
                }
            }

            // 오른쪽 의견
            BattleOpinionBox(modifier = Modifier.weight(1f), profile = item.rightProfile)
        }
    }
}

@Composable
private fun BattleOpinionBox(
    modifier: Modifier = Modifier,
    profile: BattleProfile
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, Beige500, RoundedCornerShape(2.dp))
            .background(Beige300)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 프로필 이미지
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Beige600),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = profile.profileImg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column {
            Text(text = profile.opinion, style = SwypTheme.typography.h5SemiBold, color = Gray900)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = profile.name, style = SwypTheme.typography.labelXSmall, color = Gray400)
        }
    }
}


@Composable
fun TodayPickeCard(
    item: TodayPickeItem,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (item) {
        is TodayPickeItem.AbType -> Beige400
        is TodayPickeItem.SelectionType -> SwypTheme.colors.surface
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(backgroundColor)
            .border(1.dp, Beige700, RoundedCornerShape(1.dp))
            .padding(16.dp)
    ) {
        // [공통 상단] 뱃지 & 참여자 수
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Beige600,
                shape = RoundedCornerShape(2.dp)
            ) {
                Text(
                    text = "#${item.typeName}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = SwypTheme.typography.h5SemiBold,
                    color = SwypTheme.colors.primary
                )
            }
            Text(
                text = item.participants,
                style = SwypTheme.typography.label,
                color = Gray400
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (item) {
            is TodayPickeItem.AbType -> AbPickeContent(item)
            is TodayPickeItem.SelectionType -> SelectionPickeContent(item)
        }
    }

}

// 1. 퀴즈
@Composable
private fun AbPickeContent(item: TodayPickeItem.AbType) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 제목
        Text(
            text = item.title,
            style = SwypTheme.typography.b2SemiBold,
            color = Gray900,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))

        // 설명
        Text(
            text = item.description,
            style = SwypTheme.typography.label,
            color = Gray300,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 양자택일 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PickeOptionButton(
                modifier = Modifier.weight(1f),
                title = item.leftOption,
                sub = item.leftSub
            )
            PickeOptionButton(
                modifier = Modifier.weight(1f),
                title = item.rightOption,
                sub = item.rightSub
            )
        }
    }
}

@Composable
private fun PickeOptionButton(modifier: Modifier, title: String, sub: String) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige500, RoundedCornerShape(1.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = SwypTheme.typography.chipSmall, color = Gray900)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = sub, style = SwypTheme.typography.labelXSmall, color = Gray400)
    }
}

// 2. 투표
@Composable
private fun SelectionPickeContent(item: TodayPickeItem.SelectionType) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 제목
            Text(
                text = item.titlePart1,
                style = SwypTheme.typography.b2SemiBold,
                color = Gray900
            )

            // 빈칸
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .width(64.dp)
                    .height(32.dp)
                    .border(1.dp, Beige600, RoundedCornerShape(2.dp))
                    .background(SwypTheme.colors.surface)
            )

            Text(
                text = item.titlePart2,
                style = SwypTheme.typography.b2SemiBold,
                color = Gray900
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.description,
            style = SwypTheme.typography.label,
            color = Gray300,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 2x2 그리드 옵션 버튼
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val rows = item.options.chunked(2) // 4개를 2개씩 묶음
            rows.forEachIndexed { rowIndex, rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowOptions.forEachIndexed { colIndex, optionText ->
                        val indexNum = (rowIndex * 2) + colIndex + 1

                        PickeGridButton(
                            modifier = Modifier.weight(1f),
                            index = indexNum.toString(),
                            text = optionText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PickeGridButton(modifier: Modifier, index: String, text: String) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(Beige300)
            .border(1.dp, Beige600, RoundedCornerShape(1.dp))
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$index. ",
            style = SwypTheme.typography.label,
            color = Beige900
        )
        Text(
            text = text,
            style = SwypTheme.typography.chipSmall,
            color = Gray900
        )
    }
}