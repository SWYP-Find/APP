package com.swyp4.team2.ui.vote

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.ui.home.model.BattleProfile
import com.swyp4.team2.ui.theme.Beige100
import com.swyp4.team2.ui.theme.Beige50
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Secondary200
import com.swyp4.team2.ui.theme.SwypTheme
import com.swyp4.team2.ui.vote.model.VoteTopicItem
import com.swyp4.team2.ui.vote.model.VoteType

@Composable
fun VoteScreen(
    voteType: VoteType,
    item: VoteTopicItem,
    onBackClick: () -> Unit,
    onVoteSubmit: () -> Unit
) {
    val isPreVote = voteType == VoteType.PRE

    val bgColor = if (isPreVote) SwypTheme.colors.surface else Color.Black
    val titleColor = if (isPreVote) Color.Black else SwypTheme.colors.surface
    val descText = if (isPreVote) item.preDescription else item.postDescription

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Beige500)
    ){
        // 배경 이미지
        AsyncImage(
            model = item.bgImageRes,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, bgColor),
                                startY = size.height * 0.4f
                            )
                        )
                    }
                },
            contentScale = ContentScale.Crop
        )

        // 전체 콘텐츠 컨테이너
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ){
            // 뒤로가기 & 공유버튼
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = { /* 공유 */ }) {
                    Icon(
                        painterResource(id = R.drawable.ic_share),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 태그, 제목, 설명
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item.tags.forEach { tag ->
                    Surface(
                        color = Beige600,
                        shape = RoundedCornerShape(2.dp)
                    ) {
                        Text(
                            text = "#$tag",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = SwypTheme.typography.label,
                            color = SwypTheme.colors.primary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = item.title,
                style = SwypTheme.typography.h1SemiBold,
                color = titleColor,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = descText,
                style = SwypTheme.typography.label,
                color = Gray500
            )

            Spacer(modifier = Modifier.height(40.dp))

            // [하단] VS 카드 영역
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 왼쪽 카드
                VoteOptionCard(
                    modifier = Modifier.weight(1f),
                    profile = item.leftProfile,
                )

                // VS 뱃지
                Surface(
                    modifier = Modifier.size(28.dp),
                    shape = CircleShape,
                    color = Secondary200
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "VS",
                            style = SwypTheme.typography.labelXSmall,
                            color = Gray900
                        )
                    }
                }

                // 오른쪽 카드
                VoteOptionCard(
                    modifier = Modifier.weight(1f),
                    profile = item.rightProfile
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 투표 버튼
            Button(
                onClick = onVoteSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8C3E26)), // 갈색
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.prevote),
                    style = SwypTheme.typography.b1Medium,
                    color = SwypTheme.colors.surface
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // 하단 여백
        }
    }

}

@Composable
fun VoteOptionCard(
    modifier: Modifier = Modifier,
    profile: BattleProfile,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(Beige500)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 프로필 아이콘
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color = Beige600, shape = CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_profile_xunzi),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = profile.opinion,
            style = SwypTheme.typography.b2Medium,
            color = Gray900
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = profile.name,
            style = SwypTheme.typography.labelXSmall,
            color = Gray500
        )
    }
}