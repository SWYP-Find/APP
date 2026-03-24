package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.PerspectiveBoard
import com.swyp4.team2.domain.model.PerspectivePage

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
        items = this.items.map { it.toDomainModel() }, // 알맹이 리스트 변환
        nextCursor = this.nextCursor,
        hasNext = this.hasNext
    )
}

fun PerspectiveDto.toDomainModel(): PerspectiveBoard {
    return PerspectiveBoard(
        commentId = this.commentId,
        userTag = this.user.userTag,
        nickname = this.user.nickname,
        content = this.content,
        isMine = this.isMine,
        createdAt = this.createdAt
    )
}