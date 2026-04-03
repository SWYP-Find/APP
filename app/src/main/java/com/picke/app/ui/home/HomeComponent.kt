package com.picke.app.ui.home

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.picke.app.ui.component.ProfileImage
import com.picke.app.ui.home.model.HomeContentUiModel
import com.picke.app.ui.home.model.PollQuizOptionStatUiModel
import com.picke.app.ui.home.model.TodayPickUiModel
import com.picke.app.ui.theme.*
import com.picke.app.R
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
        Spacer(modifier = Modifier.height(12.dp))
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
        Text(
            text = item.summary,
            style = SwypTheme.typography.label,
            color = Gray400,
            maxLines = 2,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        // VS 영역
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            BattleOpinionBox(
                modifier = Modifier.weight(1f),
                opinion = item.leftOpinion,
                name = item.leftProfileName,
                imageUrl = item.leftProfileImageUrl
            )
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
fun BattleOpinionBox(
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
                text = opinion ?: "의견",
                style = SwypTheme.typography.label,
                color = Gray700,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = name ?: "이름",
                style = SwypTheme.typography.labelXSmall,
                color = Gray300,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// 6. Today Pické
@Composable
fun TodayPickeCard(
    item: TodayPickUiModel,
    onVoteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (item) {
        is TodayPickUiModel.VotePick -> Beige50
        is TodayPickUiModel.QuizPick -> Beige400
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(backgroundColor)
            .border(1.dp, Beige700, RoundedCornerShape(1.dp))
            .padding(16.dp)
    ) {
        // [상단] 뱃지 및 참여자 수
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val typeName = if (item is TodayPickUiModel.VotePick) "투표" else "퀴즈"
            Surface(color = Beige600, shape = RoundedCornerShape(2.dp)) {
                Text(
                    text = "#$typeName",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = SwypTheme.typography.label,
                    color = SwypTheme.colors.primary
                )
            }
            Text(
                text = "${item.participantsCount}명 참여",
                style = SwypTheme.typography.label,
                color = Gray400
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // [본문] 투표(Vote) vs 퀴즈(Quiz) 분기 처리
        when (item) {
            is TodayPickUiModel.VotePick -> VotePickeContent(item, onVoteClick)
            is TodayPickUiModel.QuizPick -> QuizPickeContent(item, onVoteClick)
        }
    }
}

// 투표 (Vote) 전용
@Composable
private fun VotePickeContent(item: TodayPickUiModel.VotePick, onVoteClick: (Long) -> Unit) {
    val isVoted = item.selectedOptionId != null

    // 내가 선택한 옵션의 텍스트를 찾아옵니다.
    val selectedOptionText = item.options.find { it.optionId == item.selectedOptionId }?.title ?: ""

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 1. [수정] 투표 제목 - 문장 사이에 네모박스 구현
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = item.titlePrefix, style = SwypTheme.typography.b3SemiBold, color = Gray900)

            // 네모박스 영역
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = 80.dp)
                    .height(28.dp)
                    .border(
                        width = 1.dp,
                        color = Beige700,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .background(Beige200),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isVoted) selectedOptionText else "",
                    style = SwypTheme.typography.b3SemiBold,
                    color = Primary500
                )
            }

            Text(text = item.titleSuffix, style = SwypTheme.typography.b3SemiBold, color = Gray900)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.summary, style = SwypTheme.typography.label, color = Gray200, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))

        // 2. 2x2 그리드 선택지 버튼 (기존과 동일)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val rows = item.options.chunked(2)
            rows.forEachIndexed { rowIndex, rowOptions ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowOptions.forEachIndexed { colIndex, option ->
                        val indexNum = (rowIndex * 2) + colIndex + 1
                        val isSelected = item.selectedOptionId == option.optionId

                        PickeGridButton(
                            modifier = Modifier.weight(1f),
                            index = indexNum.toString(),
                            text = option.title,
                            isSelected = isSelected,
                            isVoted = isVoted,
                            onClick = { onVoteClick(option.optionId) },
                        )
                    }
                }
            }
        }

        // 3. 투표 완료 시: 하단 통계 (기존과 동일)
        if (isVoted) {
            Spacer(modifier = Modifier.height(24.dp))
            val statRows = item.options.chunked(2)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                statRows.forEach { rowOptions ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        rowOptions.forEach { option ->
                            PollStatBar(modifier = Modifier.weight(1f), option = option)
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun PickeGridButton(modifier: Modifier, index: String, text: String, isVoted: Boolean, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) Secondary500 else Beige600

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(Beige300)
            .border(1.dp, borderColor, RoundedCornerShape(1.dp))
            .clickable(enabled = !isVoted) { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$index. ", style = SwypTheme.typography.label, color = Beige900)
        Text(text = text, style = SwypTheme.typography.chipSmall, color = Gray900)
    }
}

@Composable
private fun PollStatBar(modifier: Modifier, option: PollQuizOptionStatUiModel) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = option.title, style = SwypTheme.typography.labelXSmall, color = Gray400, modifier = Modifier.width(44.dp))

        Box(
            modifier = Modifier.weight(1f)
                .height(4.dp)
                .padding(horizontal = 8.dp)
                .background(Secondary100, CircleShape)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth(option.ratio / 100f)
                    .height(4.dp)
                    .background(Secondary500, CircleShape)
            )
        }

        Text(text = "${option.ratio.toInt()}%", style = SwypTheme.typography.caption2SemiBold, color = Gray900, fontWeight = FontWeight.Bold)
    }
}


// 퀴즈 (Quiz) 전용
@Composable
private fun QuizPickeContent(item: TodayPickUiModel.QuizPick, onVoteClick: (Long) -> Unit) {
    val isVoted = item.selectedOptionId != null

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 1. 퀴즈 제목 및 설명
        Text(text = item.title, style = SwypTheme.typography.b3SemiBold, color = Gray900, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.summary, style = SwypTheme.typography.label, color = Gray200, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))

        // 2. 퀴즈 선택지
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item.options.forEach { option ->
                QuizOptionCard(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    option = option,
                    isVoted = isVoted,
                    selectedOptionId = item.selectedOptionId,
                    onClick = { onVoteClick(option.optionId) }
                )
            }
        }
    }
}

@Composable
private fun QuizOptionCard(modifier: Modifier, option: PollQuizOptionStatUiModel, selectedOptionId: Long?, isVoted: Boolean, onClick: () -> Unit) {
    //al borderColor = if (option.isCorrect) Secondary500 else Beige500
    val isMySelection = isVoted && option.optionId == selectedOptionId

    val borderColor = when {
        // 1. 투표 전: 기본 베이지색
        !isVoted -> Beige500
        // 2. 투표 후, 내가 선택한 카드일 때
        isMySelection -> if (option.isCorrect) Secondary500 else Primary500 // 정답이면 초록, 오답이면 빨강
        // 3. 투표 후, 내가 선택하지 않은 카드일 때 (정답/오답 상관없이 연하게 처리하거나 기본색)
        else -> Beige500
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
            .border(1.dp, borderColor, RoundedCornerShape(1.dp))
            .clickable(enabled = !isVoted) { onClick() }
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isVoted) {
            val resultColor = if (option.isCorrect) Secondary500 else Primary500
            val resultText = if (option.isCorrect) "O 정답" else "X 오답"

            Text(text = resultText, style = SwypTheme.typography.label, color = resultColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = option.title, style = SwypTheme.typography.chipSmall, color = Gray900, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            // Text(text = option.description ?: "", style = SwypTheme.typography.labelXSmall, color = Gray400, textAlign = TextAlign.Center)
        } else {
            // 참여 전 상태: 본문만 심플하게
            Text(text = option.title, style = SwypTheme.typography.chipSmall, color = Gray900, textAlign = TextAlign.Center)
        }
    }
}