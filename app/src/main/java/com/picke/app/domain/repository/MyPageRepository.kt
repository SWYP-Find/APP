package com.picke.app.domain.repository

import com.picke.app.domain.model.MyBattleRecordPage
import com.picke.app.domain.model.MyContentActivityPage
import com.picke.app.domain.model.MyPageInfoBoard
import com.picke.app.domain.model.MyRecapBoard
import com.picke.app.domain.model.NotificationSettingsBoard
import com.picke.app.domain.model.ProfileUpdateBoard

interface MyPageRepository {
    suspend fun getMyBattleRecords(offset: Int?, size: Int, voteSide: String?): Result<MyBattleRecordPage>
    suspend fun getMyContentActivities(offset: Int?, size: Int, activityType: String?): Result<MyContentActivityPage>
    suspend fun getMyPageInfo(): Result<MyPageInfoBoard>
    suspend fun getNotificationSettings(): Result<NotificationSettingsBoard>
    suspend fun updateNotificationSettings(settings: NotificationSettingsBoard): Result<NotificationSettingsBoard>
    suspend fun updateProfile(nickname: String, characterType: String): Result<ProfileUpdateBoard>
    suspend fun getMyRecap(): Result<MyRecapBoard>
}