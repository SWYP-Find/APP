package com.picke.domain.repository

import com.picke.domain.model.CreditHistoryPage
import com.picke.domain.model.MyBattleRecordPage
import com.picke.domain.model.MyContentActivityPage
import com.picke.domain.model.MyPageInfoBoard
import com.picke.domain.model.MyRecapBoard
import com.picke.domain.model.NotificationSettingsBoard
import com.picke.domain.model.ProfileUpdateBoard

interface MyPageRepository {
    suspend fun getMyBattleRecords(offset: Int?, size: Int, voteSide: String?): Result<MyBattleRecordPage>
    suspend fun getMyContentActivities(offset: Int?, size: Int, activityType: String?): Result<MyContentActivityPage>
    suspend fun getMyPageInfo(): Result<MyPageInfoBoard>
    suspend fun getNotificationSettings(): Result<NotificationSettingsBoard>
    suspend fun updateNotificationSettings(settings: NotificationSettingsBoard): Result<NotificationSettingsBoard>
    suspend fun updateProfile(nickname: String, characterType: String): Result<ProfileUpdateBoard>
    suspend fun getMyRecap(): Result<MyRecapBoard>
    suspend fun getCreditHistory(offset: Int?, size: Int): Result<CreditHistoryPage>
}