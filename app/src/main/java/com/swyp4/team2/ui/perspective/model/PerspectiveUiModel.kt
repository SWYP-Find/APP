package com.swyp4.team2.ui.perspective.model

import androidx.compose.ui.res.painterResource
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.PerspectiveBoard
import com.swyp4.team2.domain.model.PerspectiveStance
import java.time.ZonedDateTime
import kotlin.time.Instant

data class PerspectiveUiModel(
    val commentId: String,
    val profileImageRes: Int,
    val nickname: String,
    val stance: PerspectiveStance,
    val content: String,
    val timeAgo: String,
    val replyCount: Int,
    val likeCount: Int,
    val isLiked: Boolean
)

fun PerspectiveBoard.toUiModel(): PerspectiveUiModel {
    return PerspectiveUiModel(
        commentId = this.commentId,
        profileImageRes = R.drawable.ic_profile_xunzi,
        nickname = this.nickname,
        stance = this.stance,
        content = this.content,
        timeAgo = this.createdAt,
        replyCount = this.replyCount,
        likeCount = this.likeCount,
        isLiked = this.isLiked
    )
}

val mockPerspectiveList = listOf(
    PerspectiveUiModel(
        commentId = "1",
        profileImageRes = R.drawable.ic_profile_mengzi,
        nickname = "사유하는 라쿤",
        stance = PerspectiveStance.AGREE,
        content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠.",
        timeAgo = "2분 전",
        replyCount = 23,
        likeCount = 1340,
        isLiked = false
    ),
    PerspectiveUiModel(
        commentId = "2",
        profileImageRes = R.drawable.ic_profile_mengzi,
        nickname = "사유하는 고슴도치",
        stance = PerspectiveStance.AGREE,
        content = "맞습니다. 단순히 권리의 문제가 아니라, 약자에게는 또 다른 폭력이 될 수 있습니다.",
        timeAgo = "10분 전",
        replyCount = 5,
        likeCount = 890,
        isLiked = true
    ),
    PerspectiveUiModel(
        commentId = "3",
        profileImageRes = R.drawable.ic_profile_mengzi,
        nickname = "사유하는 고양이",
        stance = PerspectiveStance.DISAGREE,
        content = "하지만 스스로 결정을 내릴 수 없는 극한의 고통 속에 있는 사람들에게는 마지막 존엄을 지킬 기회를 주어야 하지 않을까요?",
        timeAgo = "1시간 전",
        replyCount = 12,
        likeCount = 450,
        isLiked = false
    ),
    PerspectiveUiModel(
        commentId = "4",
        profileImageRes = R.drawable.ic_profile_mengzi,
        nickname = "철학적인 부엉이",
        stance = PerspectiveStance.AGREE,
        content = "생명의 가치는 함부로 재단할 수 없습니다.",
        timeAgo = "2시간 전",
        replyCount = 0,
        likeCount = 12,
        isLiked = false
    ),
    PerspectiveUiModel(
        commentId = "5",
        profileImageRes = R.drawable.ic_profile_mengzi,
        nickname = "철학적인 부엉이",
        stance = PerspectiveStance.AGREE,
        content = "생명의 가치는 함부로 재단할 수 없습니다.",
        timeAgo = "2시간 전",
        replyCount = 0,
        likeCount = 12,
        isLiked = false
    ),
    PerspectiveUiModel(
        commentId = "6",
        profileImageRes = R.drawable.ic_profile_mengzi,
        nickname = "철학적인 부엉이",
        stance = PerspectiveStance.AGREE,
        content = "생명의 가치는 함부로 재단할 수 없습니다.",
        timeAgo = "2시간 전",
        replyCount = 0,
        likeCount = 12,
        isLiked = false
    )
)