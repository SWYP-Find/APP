package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.VoteRequestDto
import com.picke.app.data.model.toDomainModel
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

    // 1. 사전 투표
    override suspend fun submitPreVote(
        battleId: Long,
        optionId: Long
    ): Result<Boolean> {
        return try {
            val requestDto = VoteRequestDto(optionId)
            Log.d(TAG, "=========================================")
            Log.d(TAG, "[API_REQ] 사전 투표(PreVote) 통신 시작")
            Log.d(TAG, "   - 대상 배틀 ID: $battleId")
            Log.d(TAG, "   - 📤 서버로 보내는 데이터(Payload): $requestDto") // optionId 전송
            Log.d(TAG, "=========================================")

            val response = voteApi.submitPreVote(battleId, requestDto)

            if (response.statusCode == 200 || response.statusCode == 0) {
                Log.d(TAG, "[API_RES] 사전 투표 성공! (응답 코드: 200)")
                Result.success(true)
            } else {
                val errorCode = response.error?.code ?: ""
                val errorMessage = response.error?.message ?: "사전 투표에 실패했습니다."
                Log.e(TAG, "[API_RES] 사전 투표 실패 - 코드: $errorCode, 에러: $errorMessage")

                // (1) 예외가 터지지 않고 정상 응답으로 왔을 때의 400 에러 처리
                if (response.statusCode == 400 ) {
                    Log.w(TAG, "   👉 [에러 분석] 포인트 부족 에러 감지됨!")
                    Result.failure(Exception("CREDIT_400_INSUFFICIENT: $errorMessage"))
                } else {
                    Result.failure(Exception(errorMessage))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 사전 투표 예외 발생: ${e.message}")

            // ✨ (2) 핵심: Retrofit이 400 에러를 예외(Exception)로 던져서 이쪽으로 바로 넘어왔을 때의 처리!
            if (e.message?.contains("400") == true) {
                Log.w(TAG, "   👉 [예외 분석] HTTP 400 감지됨 (포인트 부족) -> 시그널 변환")
                Result.failure(Exception("CREDIT_400_INSUFFICIENT"))
            } else {
                Result.failure(e)
            }
        }
    }

    // 2. 최종(사후) 투표
    override suspend fun submitPostVote(
        battleId: Long,
        optionId: Long
    ): Result<Boolean> {
        return try {
            val requestDto = VoteRequestDto(optionId)
            Log.d(TAG, "=========================================")
            Log.d(TAG, "[API_REQ] 최종 투표(PostVote) 통신 시작")
            Log.d(TAG, "   - 대상 배틀 ID: $battleId")
            Log.d(TAG, "   - 📤 서버로 보내는 데이터(Payload): $requestDto") // optionId 전송
            Log.d(TAG, "=========================================")

            val response = voteApi.submitPostVote(battleId, requestDto)

            if (response.statusCode == 200 || response.statusCode == 0) {
                Log.d(TAG, "[API_RES] 최종 투표 성공! (응답 코드: 200)")
                Result.success(true)
            } else {
                val errorCode = response.error?.code ?: ""
                val errorMessage = response.error?.message ?: "최종 투표에 실패했습니다."
                Log.e(TAG, "[API_RES] 최종 투표 실패 - 코드: $errorCode, 에러: $errorMessage")

                // ✨ 포인트 부족(400) 에러 명시적 처리
                if (response.statusCode == 400 && (errorCode == "CREDIT_400_INSUFFICIENT" || errorMessage.contains("포인트") || errorMessage.contains("CREDIT"))) {
                    Log.w(TAG, "   👉 [에러 분석] 포인트 부족 에러 감지됨!")
                    Result.failure(Exception("CREDIT_400_INSUFFICIENT: $errorMessage"))
                } else {
                    Result.failure(Exception(errorMessage))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 최종 투표 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 3. 투표 통계(비율) 조회
    override suspend fun getVoteStats(battleId: Long): Result<VoteStatsBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 투표 통계(비율) 단건 조회 시도 - battleId: $battleId")
            val response = voteApi.getVoteStats(battleId)

            if (response.data != null) {
                val domainData = response.data.toDomainModel()
                Log.d(TAG, "[API_RES] 투표 통계 조회 성공")
                domainData.options.forEach { option ->
                    Log.d(TAG, "   └ 입장: ${option.label} | 비율: ${option.ratio}%")
                }
                Result.success(domainData)
            } else {
                Log.e(TAG, "[API_RES] 투표 통계 조회 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "투표 통계를 불러오지 못했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 투표 통계 조회 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 4. 내 투표 내역 (생각 변화 등) 조회
    override suspend fun getMyVoteHistory(battleId: Long): Result<MyVoteBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 내 투표 내역(History) 조회 시도 - battleId: $battleId")
            val response = voteApi.getMyVoteHistory(battleId)

            if (response.data != null) {
                val domainData = response.data.toDomainModel()
                Log.d(TAG, "[API_RES] 내 투표 내역 조회 성공")
                Log.d(TAG, "   └ 내 사전 투표(Pre): ${domainData.preVote} | 최종 투표(Post): ${domainData.postVote}")
                Log.d(TAG, "   └ 생각 변화 여부(opinionChanged): ${domainData.opinionChanged}")
                Result.success(domainData)
            } else {
                Log.w(TAG, "[API_RES] 내 투표 내역이 없습니다. (null 응답, 투표 안 한 상태)")
                Result.failure(Exception(response.error?.message ?: "내 투표 내역이 없습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 내 투표 내역 조회 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }
}