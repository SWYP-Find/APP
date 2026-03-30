package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.CommentBoard
import com.swyp4.team2.domain.model.CommentCreateBoard
import com.swyp4.team2.domain.model.CommentLikeToggleBoard
import com.swyp4.team2.domain.model.CommentPageBoard
import com.swyp4.team2.domain.model.CommentUpdateBoard
import com.swyp4.team2.domain.model.CommentUserBoard

// [요청 DTO] 댓글 생성 및 수정 공통
data class CommentRequestDto(
    val content: String?
)

// [응답 DTO] 댓글 유저 정보 공통
data class CommentUserDto(
    val userTag: String?,
    val nickname: String?,
    val characterType: String?,
    val characterImageUrl: String?
)

// [응답 DTO] 단일 댓글 정보
data class CommentDto(
    val commentId: Long?,
    val user: CommentUserDto?,
    val stance: String?,
    val content: String?,
    val likeCount: Int?,
    val isLiked: Boolean?,
    val isMine: Boolean?,
    val createdAt: String?
)

// [응답 DTO] 댓글 목록 (페이지네이션)
data class CommentListResponseDto(
    val items: List<CommentDto>?,
    val nextCursor: String?,
    val hasNext: Boolean?
)

// [응답 DTO] 댓글 생성 응답
data class CommentCreateResponseDto(
    val commentId: Long?,
    val user: CommentUserDto?,
    val stance: String?,
    val content: String?,
    val likeCount: Int?,
    val isLiked: Boolean?,
    val isMine: Boolean?,
    val createdAt: String?
)

// [응답 DTO] 댓글 수정 응답
data class CommentUpdateResponseDto(
    val commentId: Long?,
    val content: String?,
    val updatedAt: String?
)

// [응답 DTO] 댓글 좋아요 등록 및 취소 응답
data class CommentLikeToggleResponseDto(
    val perspectiveId: Long?, // 명세서상 응답에 perspectiveId로 내려오고 있습니다.
    val likeCount: Int?,
    val isLiked: Boolean?
)

// 좋아요 Mapper
fun CommentLikeToggleResponseDto.toDomainModel() = CommentLikeToggleBoard(
    perspectiveId = this.perspectiveId ?: 0L,
    likeCount = this.likeCount ?: 0,
    isLiked = this.isLiked ?: false
)

// DTO -> Domain
fun CommentUserDto.toDomainModel() = CommentUserBoard(
    userTag = this.userTag ?: "",
    nickname = this.nickname ?: "알 수 없음",
    characterType = this.characterType ?: "UNKNOWN",
    characterImageUrl = this.characterImageUrl ?: ""
)

fun CommentDto.toDomainModel() = CommentBoard(
    commentId = this.commentId ?: 0L,
    user = this.user?.toDomainModel() ?: CommentUserBoard("", "알 수 없음", "UNKNOWN", ""),
    stance = this.stance ?: "",
    content = this.content ?: "",
    likeCount = this.likeCount ?: 0,
    isLiked = this.isLiked ?: false,
    isMine = this.isMine ?: false,
    createdAt = this.createdAt ?: ""
)

fun CommentListResponseDto.toDomainModel() = CommentPageBoard(
    items = this.items?.map { it.toDomainModel() } ?: emptyList(),
    nextCursor = this.nextCursor,
    hasNext = this.hasNext ?: false
)

fun CommentCreateResponseDto.toDomainModel() = CommentCreateBoard(
    commentId = this.commentId ?: 0L,
    user = this.user?.toDomainModel() ?: CommentUserBoard("", "알 수 없음", "UNKNOWN", ""),
    stance = this.stance ?: "",
    content = this.content ?: "",
    likeCount = this.likeCount ?: 0,
    isLiked = this.isLiked ?: false,
    isMine = this.isMine ?: false,
    createdAt = this.createdAt ?: ""
)

fun CommentUpdateResponseDto.toDomainModel() = CommentUpdateBoard(
    commentId = this.commentId ?: 0L,
    content = this.content ?: "",
    updatedAt = this.updatedAt ?: ""
)