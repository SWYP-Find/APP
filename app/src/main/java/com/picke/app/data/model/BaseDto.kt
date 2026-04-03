package com.picke.app.data.model

data class BaseResponse<T>(
    val statusCode: Int,
    val data: T?,
    val error: ErrorResponse?
)

data class ErrorResponse(
    val code: String,
    val message: String
)
