package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.MyBattleRecordPage
import com.swyp4.team2.domain.model.MyContentActivityPage
import com.swyp4.team2.domain.model.MyPageInfoBoard
import com.swyp4.team2.domain.model.MyRecapBoard
import com.swyp4.team2.domain.model.NotificationSettingsBoard
import com.swyp4.team2.domain.model.ProfileUpdateBoard

interface MyPageRepository {
    suspend fun getMyBattleRecords(offset: Int?, size: Int, voteSide: String?): Result<MyBattleRecordPage>
    suspend fun getMyContentActivities(offset: Int?, size: Int, activityType: String?): Result<MyContentActivityPage>
    suspend fun getMyPageInfo(): Result<MyPageInfoBoard>
    suspend fun getNotificationSettings(): Result<NotificationSettingsBoard>
    suspend fun updateNotificationSettings(settings: NotificationSettingsBoard): Result<NotificationSettingsBoard>
    suspend fun updateProfile(nickname: String, characterType: String): Result<ProfileUpdateBoard>
    suspend fun getMyRecap(): Result<MyRecapBoard>
}