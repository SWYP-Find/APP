package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.ShareApi
import com.picke.app.domain.model.MyRecapBoard
import com.picke.app.domain.model.ShareKey
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

    // 나의 철학자 유형 공유키 가져오기
    override suspend fun getRecapShareKey(): Result<ShareKey> {
        return try {
            Log.d(TAG, "[API_REQ] 리캡 공유 키 발급 요청 시도")
            val response = shareApi.getRecapShareKey()

            if (response.statusCode == 200 && response.data != null) {
                Log.d(TAG, "[API_RES] 리캡 공유 키 발급 성공: ${response.data.shareKey}")

                // DTO -> Domain 모델 변환
                Result.success(response.data.toDomainModel())
            } else {
                Log.e(TAG, "[API_RES] 리캡 공유 키 발급 실패: ${response.statusCode}, 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "공유 키를 불러오는데 실패했습니다."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "[API_ERR] 리캡 공유 키 발급 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 타인의 철학자 유형 정보 가져오기
    override suspend fun getRecapDetail(shareKey: String): Result<MyRecapBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 타인 리캡 상세 정보 요청 시도 - shareKey: $shareKey")
            val response = shareApi.getRecapDetail(shareKey)

            if (response.statusCode == 200 && response.data != null) {
                Log.d(TAG, "[API_RES] 타인 리캡 상세 정보 요청 성공")

                // DTO -> Domain 모델 변환 (기존 MyRecapBoard 매퍼 활용)
                Result.success(response.data.toDomainModel())
            } else {
                Log.e(TAG, "[API_RES] 타인 리캡 상세 정보 요청 실패: ${response.statusCode}, 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "타인의 리캡 정보를 불러오는데 실패했습니다."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "[API_ERR] 타인 리캡 상세 정보 요청 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }
}