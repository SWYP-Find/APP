package com.picke.app.data.remote

import com.picke.app.data.model.*
import retrofit2.http.*

interface PerspectiveApi {
    // 1. 관점 리스트 조회
    @GET("/api/v1/battles/{battleId}/perspectives")
    suspend fun getPerspectives(
        @Path("battleId") battleId: Long,
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 10,
        @Query("optionLabel") optionLabel: String? = null,
        @Query("sort") sort: String = "latest"
    ): BaseResponse<PerspectivePageDto>

    // 2. 관점 생성 -> 여기서 perspectiveId 저장해놔야함
    @POST("/api/v1/battles/{battleId}/perspectives")
    suspend fun createPerspective(
        @Path("battleId") battleId: Long,
        @Body request: PerspectiveRequestDto
    ): BaseResponse<PerspectiveCreateResponseDto>

    // 3. 내 관점 조회 -> ????
    @GET("/api/v1/battles/{battleId}/perspectives/me")
    suspend fun getMyPerspective(
        @Path("battleId") battleId: Long
    ): BaseResponse<PerspectiveDetailResponseDto>

    // 4. 특정 관점 단건 조회 -> 저장해둔 perspectiveId 꺼내서 새로고침
    @GET("/api/v1/perspectives/{perspectiveId}")
    suspend fun getPerspective(
        @Path("perspectiveId") perspectiveId: Long
    ): BaseResponse<PerspectiveDetailResponseDto>

    // 5. 관점 삭제
    @DELETE("/api/v1/perspectives/{perspectiveId}")
    suspend fun deletePerspective(
        @Path("perspectiveId") perspectiveId: Long
    ): BaseResponse<String>

    // 6. 관점 수정
    @PATCH("/api/v1/perspectives/{perspectiveId}")
    suspend fun updatePerspective(
        @Path("perspectiveId") perspectiveId: Long,
        @Body request: PerspectiveRequestDto
    ): BaseResponse<PerspectiveUpdateResponseDto>

    // 7. 관점 검수 재시도
    @POST("/api/v1/perspectives/{perspectiveId}/moderation/retry")
    suspend fun retryModeration(
        @Path("perspectiveId") perspectiveId: Long
    ): BaseResponse<String>

    // 8. 관점 좋아요 수 조회
    @GET("/api/v1/perspectives/{perspectiveId}/likes")
    suspend fun getPerspectiveLikeCount(
        @Path("perspectiveId") perspectiveId: Long
    ): BaseResponse<PerspectiveLikeCountResponseDto>

    // 9. 관점 좋아요 등록
    @POST("/api/v1/perspectives/{perspectiveId}/likes")
    suspend fun likePerspective(
        @Path("perspectiveId") perspectiveId: Long
    ): BaseResponse<PerspectiveLikeToggleResponseDto>

    // 10. 관점 좋아요 취소
    @DELETE("/api/v1/perspectives/{perspectiveId}/likes")
    suspend fun unlikePerspective(
        @Path("perspectiveId") perspectiveId: Long
    ): BaseResponse<PerspectiveLikeToggleResponseDto>

    // 11. 관점 신고
    @POST("/api/v1/perspectives/{perspectiveId}/reports")
    suspend fun reportPerspective(
        @Path("perspectiveId") perspectiveId: Long
    ): BaseResponse<String>
}