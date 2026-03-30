package com.swyp4.team2.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.ProfileImage
import com.swyp4.team2.ui.home.model.HomeContentUiModel
import com.swyp4.team2.ui.home.model.TodayPickUiModel
import com.swyp4.team2.ui.theme.*

// 1. 공통 헤더
@Composable
fun HomeSectionHeader(
    title: String,
    highlightText: String? = null,
    onMoreClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val annotatedTitle = buildAnnotatedString {
            if (highlightText != null && title.contains(highlightText)) {
                val startIndex = title.indexOf(highlightText)
                val endIndex = startIndex + highlightText.length
                append(title.substring(0, startIndex))
                withStyle(style = SpanStyle(color = SwypTheme.colors.primary)) { append(highlightText) }
                append(title.substring(endIndex))
            } else {
                append(title)
            }
        }
        Text(text = annotatedTitle, style = SwypTheme.typography.h3Bold, color = Gray900)
        /*Text(
            text = stringResource(R.string.more),
            style = SwypTheme.typography.b4Medium,
            color = Gray500,
            modifier = Modifier.clickable { onMoreClick() }
        )*/
    }
}

// 2. Editor Pick
@Composable
fun EditorPickSection(
    items: List<HomeContentUiModel>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    if (items.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { items.size })

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        // [상단] 라벨 & 페이지네이션 (고정 영역)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(color = SwypTheme.colors.primary, shape = RoundedCornerShape(2.dp)) {
                Text(
                    text = "EDITOR PICK",
                    style = SwypTheme.typography.caption2SemiBold,
                    color = Secondary200,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            Surface(color = Gray500.copy(alpha = 0.8f), shape = RoundedCornerShape(12.dp)) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                            append((pagerState.currentPage + 1).toString())
                        }
                        withStyle(style = SpanStyle(color = Beige50.copy(alpha = 0.6f))) {
                            append("/${items.size}")
                        }
                    },
                    style = SwypTheme.typography.labelXSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        // [하단] 이미지 + 텍스트 영역 (페이저 적용 - 한 세트로 같이 넘어감)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val pagerItem = items[page]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(pagerItem.contentId) }
            ) {
                // 1. 중앙 이미지 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.8f)
                ) {
                    SubcomposeAsyncImage(
                        model = pagerItem.thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    )
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
                        val textStyle = SwypTheme.typography.h4SemiBold.copy(
                            shadow = Shadow(color = Color.Black, offset = Offset(2f, 2f), blurRadius = 4f)
                        )
                        Text(text = pagerItem.leftOpinion ?: "A", style = textStyle, color = Color.White)
                        Icon(painter = painterResource(R.drawable.ic_versus), contentDescription = null, tint = Color.White)
                        Text(text = pagerItem.rightOpinion ?: "B", style = textStyle, color = Color.White)
                    }
                }

                // 2. 하단 텍스트 영역
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = pagerItem.title, style = SwypTheme.typography.h4SemiBold, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = pagerItem.summary,
                        style = SwypTheme.typography.label,
                        color = Gray400,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = SwypTheme.typography.label.fontSize * 1.4
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = pagerItem.tags.joinToString(" ") { "#$it" },
                            style = SwypTheme.typography.label,
                            color = Gray300
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painterResource(id = R.drawable.ic_eye), null, Modifier.size(16.dp), tint = Gray400)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = pagerItem.viewCountText, style = SwypTheme.typography.label, color = Gray300)
                        }
                    }
                }
            }
        }
    }
}

// 3. Trending Battle
@Composable
fun TrendingBattleCard(item: HomeContentUiModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(220.dp).border(1.dp, Beige600, RoundedCornerShape(2.dp)).background(SwypTheme.colors.surface).clickable { onClick() }.padding(12.dp)
    ) {
        Surface(modifier = Modifier.fillMaxWidth().aspectRatio(1.2f), shape = RoundedCornerShape(4.dp), border = BorderStroke(4.dp, Beige700)) {
            SubcomposeAsyncImage(
                model = item.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier.clip(RoundedCornerShape(2.dp))
                    .background(Beige200),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Primary900,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Surface(color = Beige600, shape = RoundedCornerShape(2.dp)) {
            Text(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), text = "#${item.tags.firstOrNull() ?: "이슈"}", style = SwypTheme.typography.labelXSmall, color = SwypTheme.colors.primary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.title, style = SwypTheme.typography.h5SemiBold, color = Gray900, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(id = R.drawable.ic_clock), null, Modifier.size(12.dp), tint = Gray400)
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = item.timeInfoText, style = SwypTheme.typography.label, color = Gray400)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(painterResource(id = R.drawable.ic_eye), null, Modifier.size(12.dp), tint = Gray400)
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = item.viewCountText, style = SwypTheme.typography.label, color = Gray400)
        }
    }
}

// 4. Best Battle
@Composable
fun BestBattleRankItem(item: HomeContentUiModel, rank: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier = modifier.fillMaxWidth().clickable { onClick() }) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 16.dp), verticalAlignment = Alignment.Top) {
            val rankColor = if (rank == 1) SwypTheme.colors.primary else if(rank ==2) Secondary500 else Beige700
            Text(text = rank.toString(), style = SwypTheme.typography.h1SemiBold, color = rankColor, modifier = Modifier.fillMaxHeight().width(24.dp).align(Alignment.CenterVertically), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Surface(color = Beige600, shape = RoundedCornerShape(2.dp)) {
                    Text(text = "${item.leftProfileName ?: "A"} VS ${item.rightProfileName ?: "B"}", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = SwypTheme.typography.labelXSmall, color = SwypTheme.colors.primary)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = item.title, style = SwypTheme.typography.h5SemiBold, color = Gray900, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.tags.joinToString(" ") { "#$it" }, style = SwypTheme.typography.label, color = Gray300)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.ic_clock), null, Modifier.size(14.dp), tint = Gray300)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = item.timeInfoText, style = SwypTheme.typography.label, color = Gray400)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(painterResource(R.drawable.ic_eye), null, Modifier.size(14.dp), tint = Gray300)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = item.viewCountText, style = SwypTheme.typography.label, color = Gray400)
                    }
                }
            }
        }
        HorizontalDivider(color = Beige400, thickness = 1.dp)
    }
}

// 5. New Battle
@Composable
fun NewBattleCard(
    item: HomeContentUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(2.dp)).background(SwypTheme.colors.surface).border(1.dp, Beige600, RoundedCornerShape(2.dp)).clickable { onClick() }.padding(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Beige600, shape = RoundedCornerShape(2.dp)) {
                Text(text = "#${item.tags.firstOrNull() ?: "이슈"}", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = SwypTheme.typography.label, color = SwypTheme.colors.primary)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.ic_clock), null, Modifier.size(12.dp), tint = Gray400)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.timeInfoText, style = SwypTheme.typography.label, color = Gray400)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(painterResource(R.drawable.ic_eye), null, Modifier.size(12.dp), tint = Gray400)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.viewCountText, style = SwypTheme.typography.label, color = Gray400)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = item.title, style = SwypTheme.typography.h5SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = item.summary, style = SwypTheme.typography.label, color = Gray400)
        Spacer(modifier = Modifier.height(8.dp))

        // VS 영역
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BattleOpinionBox(
                modifier = Modifier.weight(1f),
                opinion = item.leftOpinion,
                name = item.leftProfileName,
                imageUrl = item.leftProfileImageUrl)
            Surface(modifier = Modifier.size(40.dp).padding(6.dp), shape = CircleShape, color = Secondary200) {
                Box(contentAlignment = Alignment.Center) { Text(text = "VS", style = SwypTheme.typography.labelXSmall, color = Gray900) }
            }
            BattleOpinionBox(
                modifier = Modifier.weight(1f),
                opinion = item.rightOpinion,
                name = item.rightProfileName,
                imageUrl = item.rightProfileImageUrl
            )
        }
    }
}

@Composable
private fun BattleOpinionBox(
    modifier: Modifier = Modifier,
    opinion: String?,
    name: String?,
    imageUrl: String?
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
        ProfileImage(model = imageUrl, modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.width(4.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name ?: "이름",
                style = SwypTheme.typography.label,
                color = Gray900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = opinion ?: "의견",
                style = SwypTheme.typography.labelXSmall,
                color = Gray400,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// 6. Today Pické
@Composable
fun TodayPickeCard(item: TodayPickUiModel, modifier: Modifier = Modifier) {
    val backgroundColor = when (item) {
        is TodayPickUiModel.VotePick -> Beige400
        is TodayPickUiModel.QuizPick -> SwypTheme.colors.surface
    }

    Column(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(2.dp)).background(backgroundColor).border(1.dp, Beige700, RoundedCornerShape(1.dp)).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            val typeName = if (item is TodayPickUiModel.VotePick) "투표" else "퀴즈"
            Surface(color = Beige600, shape = RoundedCornerShape(2.dp)) {
                Text(text = "#$typeName", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = SwypTheme.typography.h5SemiBold, color = SwypTheme.colors.primary)
            }
            Text(text = item.participantsText, style = SwypTheme.typography.label, color = Gray400)
        }
        Spacer(modifier = Modifier.height(20.dp))

        when (item) {
            is TodayPickUiModel.VotePick -> AbPickeContent(item)
            is TodayPickUiModel.QuizPick -> SelectionPickeContent(item)
        }
    }
}

@Composable
private fun AbPickeContent(item: TodayPickUiModel.VotePick) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = item.title, style = SwypTheme.typography.b2SemiBold, color = Gray900, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = item.summary, style = SwypTheme.typography.label, color = Gray300, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(28.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PickeOptionButton(modifier = Modifier.weight(1f), title = item.leftOptionText)
            PickeOptionButton(modifier = Modifier.weight(1f), title = item.rightOptionText)
        }
    }
}

@Composable
private fun PickeOptionButton(modifier: Modifier, title: String) {
    Column(modifier = modifier.clip(RoundedCornerShape(2.dp)).background(SwypTheme.colors.surface).border(1.dp, Beige500, RoundedCornerShape(1.dp)).padding(vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = SwypTheme.typography.chipSmall, color = Gray900)
    }
}

@Composable
private fun SelectionPickeContent(item: TodayPickUiModel.QuizPick) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = item.title, style = SwypTheme.typography.b2SemiBold, color = Gray900, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.summary, style = SwypTheme.typography.label, color = Gray300, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val rows = item.options.chunked(2)
            rows.forEachIndexed { rowIndex, rowOptions ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowOptions.forEachIndexed { colIndex, optionText ->
                        val indexNum = (rowIndex * 2) + colIndex + 1
                        PickeGridButton(modifier = Modifier.weight(1f), index = indexNum.toString(), text = optionText)
                    }
                }
            }
        }
    }
}

@Composable
private fun PickeGridButton(modifier: Modifier, index: String, text: String) {
    Row(modifier = modifier.clip(RoundedCornerShape(2.dp)).background(Beige300).border(1.dp, Beige600, RoundedCornerShape(1.dp)).padding(vertical = 16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$index. ", style = SwypTheme.typography.label, color = Beige900)
        Text(text = text, style = SwypTheme.typography.chipSmall, color = Gray900)
    }
}