package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.ShareApi
import com.picke.app.domain.model.ShareUrl
import com.picke.app.domain.repository.ShareRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShareRepositoryImpl @Inject constructor(
    private val shareApi: ShareApi
) : ShareRepository {

    companion object {
        private const val TAG = "ShareRepositoryImpl_Picke"
    }

    // 철학자 유형 공유 링크 요청
    override suspend fun getReportShareLink(reportId: Int): Result<ShareUrl> {
        return try {
            Log.d(TAG, "[API_REQ] 리포트 공유 링크 요청 시도 - reportId: $reportId")
            val response = shareApi.getReportShareLink(reportId)

            if (response.statusCode == 200 && response.data != null) {
                Log.d(TAG, "[API_RES] 리포트 공유 링크 요청 성공: ${response.data}")

                Result.success(response.data.toDomainModel())
            } else {
                Log.e(TAG, "[API_RES] 리포트 공유 링크 요청 실패: ${response.statusCode}, 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "리포트 공유 링크를 불러오는데 실패했습니다."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "[API_ERR] 리포트 공유 링크 요청 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 배틀 공유 링크 요청
    override suspend fun getBattleShareLink(battleId: Int): Result<ShareUrl> {
        return try {
            Log.d(TAG, "[API_REQ] 배틀 공유 링크 요청 시도 - battleId: $battleId")
            val response = shareApi.getBattleShareLink(battleId)

            if (response.statusCode == 200 && response.data != null) {
                Log.d(TAG, "[API_RES] 배틀 공유 링크 요청 성공: ${response.data}")

                Result.success(response.data.toDomainModel())
            } else {
                Log.e(TAG, "[API_RES] 배틀 공유 링크 요청 실패: ${response.statusCode}, 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "배틀 공유 링크를 불러오는데 실패했습니다."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "[API_ERR] 배틀 공유 링크 요청 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }
}