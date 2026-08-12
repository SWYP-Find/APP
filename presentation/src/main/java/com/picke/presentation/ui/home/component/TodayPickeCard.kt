package com.picke.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.home.model.PollQuizOptionStatUiModel
import com.picke.presentation.ui.home.model.TodayPickUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData

@Composable
fun TodayPickeCard(
    item: TodayPickUiModel,
    onVoteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (item) {
        is TodayPickUiModel.VotePick -> PickeTheme.colors.surfaceDefault
        is TodayPickUiModel.QuizPick -> PickeTheme.colors.surfaceTertiary
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(backgroundColor)
            .border(1.dp, PickeTheme.colors.borderSubtle, RoundedCornerShape(1.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val typeName = if (item is TodayPickUiModel.VotePick) "투표" else "퀴즈"

            Surface(color = PickeTheme.colors.borderDefault, shape = RoundedCornerShape(2.dp)) {
                Text(
                    text = "#$typeName",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = PickeTheme.typography.label,
                    color = PickeTheme.colors.primary
                )
            }
            Text(
                text = "${item.participantsCount}명 참여",
                style = PickeTheme.typography.label,
                color = PickeTheme.colors.neutral400
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (item) {
            is TodayPickUiModel.VotePick -> VotePickeContent(item, onVoteClick)
            is TodayPickUiModel.QuizPick -> QuizPickeContent(item, onVoteClick)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VotePickeContent(item: TodayPickUiModel.VotePick, onVoteClick: (Long) -> Unit) {
    val isVoted = item.selectedOptionId != null
    val selectedOptionText = item.options.find { it.optionId == item.selectedOptionId }?.title ?: ""

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.titlePrefix,
                style = PickeTheme.typography.b3SemiBold,
                color = PickeTheme.colors.textPrimary,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = 80.dp)
                    .height(28.dp)
                    .border(
                        width = 1.dp,
                        color = PickeTheme.colors.borderSubtle,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .background(PickeTheme.colors.backgroundBrand)
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isVoted) selectedOptionText else "",
                    style = PickeTheme.typography.b3SemiBold,
                    color = PickeTheme.colors.primary,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = item.titleSuffix,
                style = PickeTheme.typography.b3SemiBold,
                color = PickeTheme.colors.textPrimary,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.summary,
            style = PickeTheme.typography.label,
            color = PickeTheme.colors.neutral200,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val rows = item.options.chunked(2)
            rows.forEachIndexed { rowIndex, rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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

        if (isVoted) {
            Spacer(modifier = Modifier.height(24.dp))
            val statRows = item.options.chunked(2)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                statRows.forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
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
private fun QuizPickeContent(item: TodayPickUiModel.QuizPick, onVoteClick: (Long) -> Unit) {
    val isVoted = item.selectedOptionId != null

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = item.title,
            style = PickeTheme.typography.b3SemiBold,
            color = PickeTheme.colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.summary,
            style = PickeTheme.typography.label,
            color = PickeTheme.colors.neutral200,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item.options.forEach { option ->
                QuizOptionCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
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
private fun PickeGridButton(
    modifier: Modifier,
    index: String,
    text: String,
    isVoted: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor =
        if (isSelected) PickeTheme.colors.secondary else PickeTheme.colors.borderDefault

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surfaceSubtle)
            .border(1.dp, borderColor, RoundedCornerShape(1.dp))
            .clickable(enabled = !isVoted) { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$index. ",
            style = PickeTheme.typography.label,
            color = PickeTheme.colors.beige900
        )
        Text(
            text = text,
            style = PickeTheme.typography.chipSmall,
            color = PickeTheme.colors.textPrimary
        )
    }
}

@Composable
private fun PollStatBar(modifier: Modifier, option: PollQuizOptionStatUiModel) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = option.title,
            style = PickeTheme.typography.labelXSmall,
            color = PickeTheme.colors.neutral400,
            modifier = Modifier.width(44.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .padding(horizontal = 4.dp)
                .background(PickeTheme.colors.secondary100, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(option.ratio / 100f)
                    .height(4.dp)
                    .background(PickeTheme.colors.secondary, CircleShape)
            )
        }

        Text(
            text = "${option.ratio.toInt()}%",
            style = PickeTheme.typography.caption2SemiBold,
            color = PickeTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.width(36.dp)
        )
    }
}

@Composable
private fun QuizOptionCard(
    modifier: Modifier,
    option: PollQuizOptionStatUiModel,
    selectedOptionId: Long?,
    isVoted: Boolean,
    onClick: () -> Unit
) {
    val isMySelection = isVoted && option.optionId == selectedOptionId
    val borderColor = when {
        !isVoted -> PickeTheme.colors.borderDisabled
        isMySelection -> if (option.isCorrect) PickeTheme.colors.secondary else PickeTheme.colors.primary // 정답이면 초록, 오답이면 빨강
        else -> PickeTheme.colors.borderDisabled
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
            val resultColor =
                if (option.isCorrect) PickeTheme.colors.secondary else PickeTheme.colors.primary
            val resultText = if (option.isCorrect) "O 정답" else "X 오답"

            Text(text = resultText, style = PickeTheme.typography.label, color = resultColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = option.title,
                style = PickeTheme.typography.chipSmall,
                color = PickeTheme.colors.textPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
        } else {
            Text(
                text = option.title,
                style = PickeTheme.typography.chipSmall,
                color = PickeTheme.colors.textPrimary,
                textAlign = TextAlign.Center
            )
            if (option.stance.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = option.stance,
                    style = PickeTheme.typography.labelXSmall,
                    color = PickeTheme.colors.neutral400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodayPickeCardPreview() {
    PickeTheme {
        TodayPickeCard(
            item = DummyData.dummyVotePick,
            onVoteClick = {}
        )
    }
}