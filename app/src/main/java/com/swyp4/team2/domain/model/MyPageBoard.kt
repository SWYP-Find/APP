package com.swyp4.team2.domain.model

// 1. 내 배틀 기록
data class MyBattleRecordItem(
    val battleId: String,
    val recordId: String,
    val voteSide: String, // "PRO" or "CON"
    val title: String,
    val summary: String,
    val createdAt: String
)

data class MyBattleRecordPage(
    val items: List<MyBattleRecordItem>,
    val nextOffset: Int?,
    val hasNext: Boolean
)

// 2. 내 활동 (댓글, 좋아요)
data class MyContentActivityAuthor(
    val userTag: String,
    val nickname: String,
    val characterType: String
)

data class MyContentActivityItem(
    val activityId: String,
    val activityType: String, // "COMMENT" or "LIKE"
    val perspectiveId: String,
    val battleId: String,
    val battleTitle: String,
    val author: MyContentActivityAuthor,
    val stance: String,
    val content: String,
    val likeCount: Int,
    val createdAt: String
)

data class MyContentActivityPage(
    val items: List<MyContentActivityItem>,
    val nextOffset: Int?,
    val hasNext: Boolean
)

// 3. 마이페이지 메인 정보
data class MyProfile(
    val userTag: String,
    val nickname: String,
    val characterType: String,
    val characterLabel: String,
    val characterImageUrl: String,
    val mannerTemperature: Double
)

data class MyPhilosopher(
    val philosopherType: String,
    val philosopherLabel: String,
    val typeName: String,
    val description: String,
    val imageUrl: String
)

data class MyTier(
    val tierCode: String,
    val tierLabel: String,
    val currentPoint: Int
)

data class MyPageInfoBoard(
    val profile: MyProfile,
    val philosopher: MyPhilosopher,
    val tier: MyTier
)

// 4 & 5. 알림 설정 (조회 및 수정 공통)
data class NotificationSettingsBoard(
    val newBattleEnabled: Boolean,
    val battleResultEnabled: Boolean,
    val commentReplyEnabled: Boolean,
    val newCommentEnabled: Boolean,
    val contentLikeEnabled: Boolean,
    val marketingEventEnabled: Boolean
)

// 6. 프로필 수정 결과
data class ProfileUpdateBoard(
    val userTag: String,
    val nickname: String,
    val characterType: String,
    val updatedAt: String
)

// 7. 내 연말 결산(Recap)
data class FavoriteTopic(
    val rank: Int,
    val tagName: String,
    val participationCount: Int
)

data class RecapScores(
    val principle: Int,
    val reason: Int,
    val individual: Int,
    val change: Int,
    val inner: Int,
    val ideal: Int
)

data class PreferenceReport(
    val totalParticipation: Int,
    val opinionChanges: Int,
    val battleWinRate: Int,
    val favoriteTopics: List<FavoriteTopic>
)

data class MyRecapBoard(
    val myCard: MyPhilosopher,
    val bestMatchCard: MyPhilosopher,
    val worstMatchCard: MyPhilosopher,
    val scores: RecapScores,
    val preferenceReport: PreferenceReport
)