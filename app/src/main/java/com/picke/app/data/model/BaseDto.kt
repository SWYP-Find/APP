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

fun <T> BaseResponse<T>.toResult(fallbackMessage: String = "알 수 없는 오류가 발생했습니다."): Result<T> {
    val body = data
    return if (statusCode == 200 && body != null) {
        Result.success(body)
    } else {
        Result.failure(Exception(error?.message ?: fallbackMessage))
    }
}
