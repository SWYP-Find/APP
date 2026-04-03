package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse // 기존 공통 응답 클래스 임포트 확인!
import com.picke.app.data.model.CommentCreateResponseDto
import com.picke.app.data.model.CommentLikeToggleResponseDto
import com.picke.app.data.model.CommentListResponseDto
import com.picke.app.data.model.CommentRequestDto
import com.picke.app.data.model.CommentUpdateResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApi {

    // 1. 댓글 목록 조회 (페이지네이션)
    //@GET("/api/v1/perspectives/{perspectiveId}/comments")
    @GET("/api/v1/perspectives/{perspectiveId}/comments/labeled")
    suspend fun getComments(
        @Path("perspectiveId") perspectiveId: Long,
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 10
    ): BaseResponse<CommentListResponseDto>

    // 2. 댓글 생성
    @POST("/api/v1/perspectives/{perspectiveId}/comments")
    suspend fun createComment(
        @Path("perspectiveId") perspectiveId: Long,
        @Body request: CommentRequestDto
    ): BaseResponse<CommentCreateResponseDto>

    // 3. 댓글 삭제
    @DELETE("/api/v1/perspectives/{perspectiveId}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("perspectiveId") perspectiveId: Long,
        @Path("commentId") commentId: Long
    ): BaseResponse<String>

    // 4. 댓글 수정
    @PATCH("/api/v1/perspectives/{perspectiveId}/comments/{commentId}")
    suspend fun updateComment(
        @Path("perspectiveId") perspectiveId: Long,
        @Path("commentId") commentId: Long,
        @Body request: CommentRequestDto
    ): BaseResponse<CommentUpdateResponseDto>

    // 5. 댓글 좋아요 등록
    @POST("/api/v1/comments/{commentId}/likes")
    suspend fun likeComment(
        @Path("commentId") commentId: Long
    ): BaseResponse<CommentLikeToggleResponseDto>

    // 6. 댓글 좋아요 취소
    @DELETE("/api/v1/comments/{commentId}/likes")
    suspend fun unlikeComment(
        @Path("commentId") commentId: Long
    ): BaseResponse<CommentLikeToggleResponseDto>

    // 7. 댓글 신고
    @POST("/api/v1/perspectives/{perspectiveId}/comments/{commentId}/reports")
    suspend fun reportComment(
        @Path("perspectiveId") perspectiveId: Long,
        @Path("commentId") commentId: Long
    ): BaseResponse<String>
}