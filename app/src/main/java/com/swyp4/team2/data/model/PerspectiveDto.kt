package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.*

// [공통 DTO]
data class PerspectiveRequestDto(
    val content: String
)

data class PerspectiveUserDto(
    val userTag: String?,
    val nickname: String?,
    val characterType: String?,
    val characterImageUrl: String?
)

data class PerspectiveOptionDto(
    val optionId: Long?,
    val label: String?,
    val title: String?,
    val stance: String?
)

// [API 1: 관점 리스트 조회]
data class PerspectivePageDto(
    val items: List<PerspectiveDto>?,
    val nextCursor: String?,
    val hasNext: Boolean?
)

data class PerspectiveDto(
    val perspectiveId: Long?,
    val user: PerspectiveUserDto?,
    val option: PerspectiveOptionDto?,
    val content: String?,
    val likeCount: Int?,
    val commentCount: Int?,
    val isLiked: Boolean?,
    val isMyPerspective: Boolean?,
    val createdAt: String?
)

// [API 2 & 4 & 5 & 6 & 7 유지보수용 DTO]
data class PerspectiveCreateResponseDto(
    val perspectiveId: Long?,
    val status: String?,
    val createdAt: String?
)

// [API 3: 내 관점 조회 & API 4: 관점 단건 조회 공통 DTO]
data class PerspectiveDetailResponseDto(
    val perspectiveId: Long?,
    val user: PerspectiveUserDto?,
    val option: PerspectiveOptionDto?,
    val content: String?,
    val likeCount: Int?,
    val commentCount: Int?,
    val isLiked: Boolean?,
    val status: String?,
    val isMyPerspective: Boolean?,
    val createdAt: String?
)

// [API 8: 관점 좋아요 수 조회 응답 DTO]
data class PerspectiveLikeCountResponseDto(
    val perspectiveId: Long?,
    val likeCount: Int?
)

// [API 9 & 10: 관점 좋아요 등록 및 취소 응답 DTO]
data class PerspectiveLikeToggleResponseDto(
    val perspectiveId: Long?,
    val likeCount: Int?,
    val isLiked: Boolean?
)

data class PerspectiveUpdateResponseDto(
    val perspectiveId: Long?,
    val content: String?,
    val updatedAt: String?
)

// ==========================================
// DTO -> Domain Mappers
// ==========================================

fun PerspectivePageDto.toDomainModel() = PerspectivePage(
    items = this.items?.map { it.toDomainModel() } ?: emptyList(),
    nextCursor = this.nextCursor,
    hasNext = this.hasNext ?: false
)

fun PerspectiveDto.toDomainModel() = PerspectiveBoard(
    commentId = this.perspectiveId?.toString() ?: "",
    nickname = this.user?.nickname ?: "알 수 없음",
    characterType = this.user?.characterType ?: "",
    characterImageUrl = this.user?.characterImageUrl ?: "",
    content = this.content ?: "",
    isMine = this.isMyPerspective ?: false,
    createdAt = this.createdAt ?: "",
    stance = this.option?.label ?: "A",
    replyCount = this.commentCount ?: 0,
    likeCount = this.likeCount ?: 0,
    isLiked = this.isLiked ?: false
)

fun PerspectiveCreateResponseDto.toDomainModel() = PerspectiveStatusBoard(
    perspectiveId = this.perspectiveId ?: 0L,
    status = this.status ?: "UNKNOWN",
    createdAt = this.createdAt ?: ""
)

// 내 관점 단건, 일반 단건 매퍼 통합
fun PerspectiveDetailResponseDto.toDomainModel() = PerspectiveDetailBoard(
    perspectiveId = this.perspectiveId ?: 0L,
    userTag = this.user?.userTag ?: "",
    nickname = this.user?.nickname ?: "알 수 없음",
    characterImageUrl = this.user?.characterImageUrl ?: "",
    optionLabel = this.option?.label ?: "",
    content = this.content ?: "",
    likeCount = this.likeCount ?: 0,
    commentCount = this.commentCount ?: 0,
    isLiked = this.isLiked ?: false,
    status = this.status ?: "PUBLISHED",
    isMine = this.isMyPerspective ?: true,
    createdAt = this.createdAt ?: ""
)

fun PerspectiveUpdateResponseDto.toDomainModel() = PerspectiveUpdateBoard(
    perspectiveId = this.perspectiveId ?: 0L,
    content = this.content ?: "",
    updatedAt = this.updatedAt ?: ""
)

fun PerspectiveLikeCountResponseDto.toDomainModel() = PerspectiveLikeCountBoard(
    perspectiveId = this.perspectiveId ?: 0L,
    likeCount = this.likeCount ?: 0
)

fun PerspectiveLikeToggleResponseDto.toDomainModel() = PerspectiveLikeToggleBoard(
    perspectiveId = this.perspectiveId ?: 0L,
    likeCount = this.likeCount ?: 0,
    isLiked = this.isLiked ?: false
)