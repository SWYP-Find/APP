package com.picke.app.domain.model

enum class PerspectiveStance(val label: String) {
    AGREE("찬성"),
    DISAGREE("반대")
}

data class PerspectivePage(
    val items: List<PerspectiveBoard>,
    val nextCursor: String?,
    val hasNext: Boolean
)

data class PerspectiveBoard(
    val commentId: String,
    val nickname: String,
    val characterType: String,
    val characterImageUrl: String,
    val content: String,
    val isMine: Boolean,
    val createdAt: String,
    val stance: String,
    val replyCount: Int,
    val likeCount: Int,
    val isLiked: Boolean
)

// '내 관점' 또는 '단일 관점' 조회를 위한 상세 모델
data class PerspectiveDetailBoard(
    val perspectiveId: Long,
    val userTag: String,
    val nickname: String,
    val characterImageUrl: String,
    val optionLabel: String,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val status: String,
    val isMine: Boolean,
    val createdAt: String
)

data class PerspectiveStatusBoard(
    val perspectiveId: Long,
    val status: String,
    val createdAt: String
)

data class PerspectiveUpdateBoard(
    val perspectiveId: Long,
    val content: String,
    val updatedAt: String
)

// 좋아요 수 조회 모델
data class PerspectiveLikeCountBoard(
    val perspectiveId: Long,
    val likeCount: Int
)

// 좋아요 등록/취소 결과 모델
data class PerspectiveLikeToggleBoard(
    val perspectiveId: Long,
    val likeCount: Int,
    val isLiked: Boolean
)