package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.ProfileUpdateRequestDto
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.model.toDto
import com.picke.app.data.remote.MyPageApi
import com.picke.app.domain.model.CreditHistoryPage
import com.picke.app.domain.model.MyBattleRecordPage
import com.picke.app.domain.model.MyContentActivityPage
import com.picke.app.domain.model.MyPageInfoBoard
import com.picke.app.domain.model.MyRecapBoard
import com.picke.app.domain.model.NotificationSettingsBoard
import com.picke.app.domain.model.ProfileUpdateBoard
import com.picke.app.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageApi: MyPageApi
): MyPageRepository {

    companion object {
        private const val TAG = "MyPageRepo_Picke"
    }

    override suspend fun getMyBattleRecords(offset: Int?, size: Int, voteSide: String?): Result<MyBattleRecordPage> {
        return try {
            val response = myPageApi.getMyBattleRecords(offset, size, voteSide)
            val data = response.data ?: throw Exception(response.error?.message ?: "기록을 불러오지 못했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyContentActivities(offset: Int?, size: Int, activityType: String?): Result<MyContentActivityPage> {
        return try {
            val response = myPageApi.getMyContentActivities(offset, size, activityType)
            val data = response.data ?: throw Exception(response.error?.message ?: "활동 기록을 불러오지 못했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyPageInfo(): Result<MyPageInfoBoard> {
        return try {
            val response = myPageApi.getMyPageInfo()
            val data = response.data ?: throw Exception(response.error?.message ?: "마이페이지 정보를 불러오지 못했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNotificationSettings(): Result<NotificationSettingsBoard> {
        return try {
            val response = myPageApi.getNotificationSettings()
            val data = response.data ?: throw Exception(response.error?.message ?: "알림 설정을 불러오지 못했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettingsBoard): Result<NotificationSettingsBoard> {
        return try {
            val response = myPageApi.updateNotificationSettings(settings.toDto())
            val data = response.data ?: throw Exception(response.error?.message ?: "알림 설정 업데이트에 실패했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(nickname: String, characterType: String): Result<ProfileUpdateBoard> {
        return try {
            val response = myPageApi.updateProfile(ProfileUpdateRequestDto(nickname, characterType))
            val data = response.data ?: throw Exception(response.error?.message ?: "프로필 수정에 실패했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyRecap(): Result<MyRecapBoard> {
        return try {
            val response = myPageApi.getMyRecap()
            val data = response.data ?: throw Exception(response.error?.message ?: "연말 결산 정보를 불러오지 못했습니다.")
            val domainModel = data.toDomainModel()
            Log.d(TAG, "[API_RES] 철학자 유형 응답 성공! 전체 데이터: $domainModel")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCreditHistory(
        offset: Int?,
        size: Int
    ): Result<CreditHistoryPage> {
        return try {
            val response = myPageApi.getCreditHistory(offset, size)
            val data = response.data ?: throw Exception(response.error?.message ?: "크레딧 내역을 불러오지 못했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}