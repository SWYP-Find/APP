package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.VoteRequestDto
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.model.toResult
import com.picke.app.data.remote.VoteApi
import com.picke.app.domain.model.MyVoteBoard
import com.picke.app.domain.model.VoteStatsBoard
import com.picke.app.domain.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteApi: VoteApi
) : VoteRepository {

    companion object {
        private const val TAG = "VoteRepositoryImpl_Picke"
    }

    override suspend fun submitPreVote(battleId: Long, optionId: Long): Result<Boolean> {
        return try {
            val requestDto = VoteRequestDto(optionId)
            Log.d(TAG, "[API_REQ] 사전 투표(PreVote) 통신 시작 - battleId: $battleId, payload: $requestDto")
            val response = voteApi.submitPreVote(battleId, requestDto)
            when (response.statusCode) {
                200 -> {
                    Log.d(TAG, "[API_RES] 사전 투표 성공!")
                    Result.success(true)
                }
                400 -> {
                    Log.w(TAG, "[API_RES] 사전 투표 실패 - 포인트 부족")
                    Result.failure(Exception("CREDIT_400_INSUFFICIENT: ${response.error?.message}"))
                }
                else -> {
                    Log.e(TAG, "[API_RES] 사전 투표 실패 - 에러: ${response.error?.message}")
                    Result.failure(Exception(response.error?.message ?: "사전 투표에 실패했습니다."))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 사전 투표 예외 발생: ${e.message}")
            if (e.message?.contains("400") == true) {
                Result.failure(Exception("CREDIT_400_INSUFFICIENT"))
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun submitPostVote(battleId: Long, optionId: Long): Result<Boolean> {
        return try {
            val requestDto = VoteRequestDto(optionId)
            Log.d(TAG, "[API_REQ] 최종 투표(PostVote) 통신 시작 - battleId: $battleId, payload: $requestDto")
            val response = voteApi.submitPostVote(battleId, requestDto)
            when (response.statusCode) {
                200 -> {
                    Log.d(TAG, "[API_RES] 최종 투표 성공!")
                    Result.success(true)
                }
                400 -> {
                    Log.w(TAG, "[API_RES] 최종 투표 실패 - 포인트 부족")
                    Result.failure(Exception("CREDIT_400_INSUFFICIENT: ${response.error?.message}"))
                }
                else -> {
                    Log.e(TAG, "[API_RES] 최종 투표 실패 - 에러: ${response.error?.message}")
                    Result.failure(Exception(response.error?.message ?: "최종 투표에 실패했습니다."))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 최종 투표 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getVoteStats(battleId: Long): Result<VoteStatsBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 투표 통계(비율) 단건 조회 시도 - battleId: $battleId")
            voteApi.getVoteStats(battleId)
                .toResult("투표 통계를 불러오지 못했습니다.")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 투표 통계 조회 성공")
                    domainData.options.forEach { option ->
                        Log.d(TAG, "   └ 입장: ${option.label} | 비율: ${option.ratio}%")
                    }
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 투표 통계 조회 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getMyVoteHistory(battleId: Long): Result<MyVoteBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 내 투표 내역(History) 조회 시도 - battleId: $battleId")
            voteApi.getMyVoteHistory(battleId)
                .toResult("내 투표 내역을 불러오지 못했습니다.")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 내 투표 내역 조회 성공")
                    Log.d(TAG, "   └ 내 사전 투표(Pre): ${domainData.preVote} | 최종 투표(Post): ${domainData.postVote}")
                    Log.d(TAG, "   └ 생각 변화 여부(opinionChanged): ${domainData.opinionChanged}")
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 내 투표 내역 조회 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }
}