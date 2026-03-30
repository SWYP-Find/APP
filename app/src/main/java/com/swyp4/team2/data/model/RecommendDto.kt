package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.RecommendBoard
import com.swyp4.team2.domain.model.RecommendPageBoard
import com.swyp4.team2.domain.model.RecommendUserBoard

// [응답 DTO] 유저 정보
data class RecommendationUserDto(
    val userTag: String?,
    val nickname: String?,
    val characterType: String?,
    val characterImageUrl: String?
)

// [응답 DTO] 추천 아이템 단건 (현재 명세서상 댓글 형태)
data class RecommendationItemDto(
    val commentId: Long?,
    val user: RecommendationUserDto?,
    val stance: String?,
    val content: String?,
    val likeCount: Int?,
    val isLiked: Boolean?,
    val isMine: Boolean?,
    val createdAt: String?
)

// [응답 DTO] 추천 리스트 (페이지네이션)
data class RecommendationPageResponseDto(
    val items: List<RecommendationItemDto>?,
    val nextCursor: String?,
    val hasNext: Boolean?
)

// DTO -> Domain

fun RecommendationUserDto.toDomainModel() = RecommendUserBoard(
    userTag = this.userTag ?: "",
    nickname = this.nickname ?: "알 수 없음",
    characterType = this.characterType ?: "UNKNOWN",
    characterImageUrl = this.characterImageUrl ?: ""
)

fun RecommendationItemDto.toDomainModel() = RecommendBoard(
    commentId = this.commentId ?: 0L,
    user = this.user?.toDomainModel() ?: RecommendUserBoard("", "알 수 없음", "UNKNOWN", ""),
    stance = this.stance ?: "",
    content = this.content ?: "",
    likeCount = this.likeCount ?: 0,
    isLiked = this.isLiked ?: false,
    isMine = this.isMine ?: false,
    createdAt = this.createdAt ?: ""
)

fun RecommendationPageResponseDto.toDomainModel() = RecommendPageBoard(
    items = this.items?.map { it.toDomainModel() } ?: emptyList(),
    nextCursor = this.nextCursor,
    hasNext = this.hasNext ?: false
)