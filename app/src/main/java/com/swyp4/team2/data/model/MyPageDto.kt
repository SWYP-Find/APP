package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.FavoriteTopic
import com.swyp4.team2.domain.model.MyBattleRecordItem
import com.swyp4.team2.domain.model.MyBattleRecordPage
import com.swyp4.team2.domain.model.MyContentActivityAuthor
import com.swyp4.team2.domain.model.MyContentActivityItem
import com.swyp4.team2.domain.model.MyContentActivityPage
import com.swyp4.team2.domain.model.MyPageInfoBoard
import com.swyp4.team2.domain.model.MyPhilosopher
import com.swyp4.team2.domain.model.MyProfile
import com.swyp4.team2.domain.model.MyRecapBoard
import com.swyp4.team2.domain.model.MyTier
import com.swyp4.team2.domain.model.NotificationSettingsBoard
import com.swyp4.team2.domain.model.PreferenceReport
import com.swyp4.team2.domain.model.ProfileUpdateBoard
import com.swyp4.team2.domain.model.RecapScores

// --- [Request DTOs] ---
data class NotificationSettingsDto(
    val newBattleEnabled: Boolean?,
    val battleResultEnabled: Boolean?,
    val commentReplyEnabled: Boolean?,
    val newCommentEnabled: Boolean?,
    val contentLikeEnabled: Boolean?,
    val marketingEventEnabled: Boolean?
)

data class ProfileUpdateRequestDto(
    val nickname: String,
    val characterType: String
)

// --- [Response DTOs] ---
data class MyBattleRecordItemDto(
    val battleId: String?,
    val recordId: String?,
    val voteSide: String?,
    val title: String?,
    val summary: String?,
    val createdAt: String?
)

data class MyBattleRecordPageDto(
    val items: List<MyBattleRecordItemDto>?,
    val nextOffset: Int?,
    val hasNext: Boolean?
)

data class MyContentActivityAuthorDto(
    val userTag: String?,
    val nickname: String?,
    val characterType: String?
)

data class MyContentActivityItemDto(
    val activityId: String?,
    val activityType: String?,
    val perspectiveId: String?,
    val battleId: String?,
    val battleTitle: String?,
    val author: MyContentActivityAuthorDto?,
    val stance: String?,
    val content: String?,
    val likeCount: Int?,
    val createdAt: String?
)

data class MyContentActivityPageDto(
    val items: List<MyContentActivityItemDto>?,
    val nextOffset: Int?,
    val hasNext: Boolean?
)

data class MyProfileDto(
    val userTag: String?,
    val nickname: String?,
    val characterType: String?,
    val characterLabel: String?,
    val characterImageUrl: String?,
    val mannerTemperature: Double?
)

data class MyPhilosopherDto(
    val philosopherType: String?,
    val philosopherLabel: String?,
    val typeName: String?,
    val description: String?,
    val imageUrl: String?
)

data class MyTierDto(
    val tierCode: String?,
    val tierLabel: String?,
    val currentPoint: Int?
)

data class MyPageInfoDto(
    val profile: MyProfileDto?,
    val philosopher: MyPhilosopherDto?,
    val tier: MyTierDto?
)

data class ProfileUpdateResponseDto(
    val userTag: String?,
    val nickname: String?,
    val characterType: String?,
    val updatedAt: String?
)

data class FavoriteTopicDto(
    val rank: Int?,
    val tagName: String?,
    val participationCount: Int?
)

data class RecapScoresDto(
    val principle: Int?,
    val reason: Int?,
    val individual: Int?,
    val change: Int?,
    val inner: Int?,
    val ideal: Int?
)

data class PreferenceReportDto(
    val totalParticipation: Int?,
    val opinionChanges: Int?,
    val battleWinRate: Int?,
    val favoriteTopics: List<FavoriteTopicDto>?
)

data class MyRecapDto(
    val myCard: MyPhilosopherDto?,
    val bestMatchCard: MyPhilosopherDto?,
    val worstMatchCard: MyPhilosopherDto?,
    val scores: RecapScoresDto?,
    val preferenceReport: PreferenceReportDto?
)

// Mappers DTO -> Domain
fun MyBattleRecordItemDto.toDomainModel() = MyBattleRecordItem(
    battleId = this.battleId ?: "",
    recordId = this.recordId ?: "",
    voteSide = this.voteSide ?: "PRO",
    title = this.title ?: "",
    summary = this.summary ?: "",
    createdAt = this.createdAt ?: ""
)

fun MyBattleRecordPageDto.toDomainModel() = MyBattleRecordPage(
    items = this.items?.map { it.toDomainModel() } ?: emptyList(),
    nextOffset = this.nextOffset,
    hasNext = this.hasNext ?: false
)

fun MyContentActivityItemDto.toDomainModel() = MyContentActivityItem(
    activityId = this.activityId ?: "",
    activityType = this.activityType ?: "COMMENT",
    perspectiveId = this.perspectiveId ?: "",
    battleId = this.battleId ?: "",
    battleTitle = this.battleTitle ?: "",
    author = MyContentActivityAuthor(
        userTag = this.author?.userTag ?: "",
        nickname = this.author?.nickname ?: "알 수 없음",
        characterType = this.author?.characterType ?: "UNKNOWN"
    ),
    stance = this.stance ?: "",
    content = this.content ?: "",
    likeCount = this.likeCount ?: 0,
    createdAt = this.createdAt ?: ""
)

fun MyContentActivityPageDto.toDomainModel() = MyContentActivityPage(
    items = this.items?.map { it.toDomainModel() } ?: emptyList(),
    nextOffset = this.nextOffset,
    hasNext = this.hasNext ?: false
)

fun MyPageInfoDto.toDomainModel() = MyPageInfoBoard(
    profile = MyProfile(
        userTag = this.profile?.userTag ?: "",
        nickname = this.profile?.nickname ?: "알 수 없음",
        characterType = this.profile?.characterType ?: "UNKNOWN",
        characterLabel = this.profile?.characterLabel ?: "",
        characterImageUrl = this.profile?.characterImageUrl ?: "",
        mannerTemperature = this.profile?.mannerTemperature ?: 0.0
    ),
    philosopher = this.philosopher.toDomainModel(),
    tier = MyTier(
        tierCode = this.tier?.tierCode ?: "UNKNOWN",
        tierLabel = this.tier?.tierLabel ?: "",
        currentPoint = this.tier?.currentPoint ?: 0
    )
)

fun MyPhilosopherDto?.toDomainModel() = MyPhilosopher(
    philosopherType = this?.philosopherType ?: "UNKNOWN",
    philosopherLabel = this?.philosopherLabel ?: "",
    typeName = this?.typeName ?: "",
    description = this?.description ?: "",
    imageUrl = this?.imageUrl ?: ""
)

fun NotificationSettingsDto.toDomainModel() = NotificationSettingsBoard(
    newBattleEnabled = this.newBattleEnabled ?: true,
    battleResultEnabled = this.battleResultEnabled ?: true,
    commentReplyEnabled = this.commentReplyEnabled ?: true,
    newCommentEnabled = this.newCommentEnabled ?: true,
    contentLikeEnabled = this.contentLikeEnabled ?: true,
    marketingEventEnabled = this.marketingEventEnabled ?: false
)

// Request DTO 변환 (Domain -> DTO)
fun NotificationSettingsBoard.toDto() = NotificationSettingsDto(
    newBattleEnabled = this.newBattleEnabled,
    battleResultEnabled = this.battleResultEnabled,
    commentReplyEnabled = this.commentReplyEnabled,
    newCommentEnabled = this.newCommentEnabled,
    contentLikeEnabled = this.contentLikeEnabled,
    marketingEventEnabled = this.marketingEventEnabled
)

fun ProfileUpdateResponseDto.toDomainModel() = ProfileUpdateBoard(
    userTag = this.userTag ?: "",
    nickname = this.nickname ?: "",
    characterType = this.characterType ?: "",
    updatedAt = this.updatedAt ?: ""
)

fun MyRecapDto.toDomainModel() = MyRecapBoard(
    myCard = this.myCard.toDomainModel(),
    bestMatchCard = this.bestMatchCard.toDomainModel(),
    worstMatchCard = this.worstMatchCard.toDomainModel(),
    scores = RecapScores(
        principle = this.scores?.principle ?: 0,
        reason = this.scores?.reason ?: 0,
        individual = this.scores?.individual ?: 0,
        change = this.scores?.change ?: 0,
        inner = this.scores?.inner ?: 0,
        ideal = this.scores?.ideal ?: 0
    ),
    preferenceReport = PreferenceReport(
        totalParticipation = this.preferenceReport?.totalParticipation ?: 0,
        opinionChanges = this.preferenceReport?.opinionChanges ?: 0,
        battleWinRate = this.preferenceReport?.battleWinRate ?: 0,
        favoriteTopics = this.preferenceReport?.favoriteTopics?.map {
            FavoriteTopic(it.rank ?: 0, it.tagName ?: "", it.participationCount ?: 0)
        } ?: emptyList()
    )
)