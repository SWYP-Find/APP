package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.PerspectiveBoard
import com.swyp4.team2.domain.model.PerspectivePage
import com.swyp4.team2.domain.model.PerspectiveStance

data class PerspectivePageDto(
    val items: List<PerspectiveDto>,
    val nextCursor: String?,
    val hasNext: Boolean
)

data class PerspectiveDto(
    val commentId: Long,
    val user: PerspectiveUserDto,
    val content: String,
    val isMine: Boolean,
    val createdAt: String
)

data class PerspectiveUserDto(
    val userTag: String,
    val nickname: String,
    val characterType: String
)

// Dto->Domain
fun PerspectivePageDto.toDomainModel(): PerspectivePage {
    return PerspectivePage(
        items = this.items.map { it.toDomainModel() },
        nextCursor = this.nextCursor,
        hasNext = this.hasNext
    )
}

fun PerspectiveDto.toDomainModel(): PerspectiveBoard {
    return PerspectiveBoard(
        commentId = this.commentId.toString(),
        userTag = this.user.userTag,
        nickname = this.user.nickname,
        characterType = this.user.characterType,
        content = this.content,
        isMine = this.isMine,
        createdAt = this.createdAt,
        stance = PerspectiveStance.AGREE,
        replyCount = 0,
        likeCount = 0,
        isLiked = false
    )
}