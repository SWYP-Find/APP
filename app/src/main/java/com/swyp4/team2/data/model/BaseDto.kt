package com.swyp4.team2.data.model

data class BaseResponse<T>(
    val statusCode: Int,
    val data: T?,
    val error: ErrorResponse?
)

data class ErrorResponse(
    val code: String,
    val message: String
)
