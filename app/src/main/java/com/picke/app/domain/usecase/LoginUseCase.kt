package com.picke.app.domain.usecase

import com.picke.app.BuildConfig
import com.picke.app.domain.model.AuthBoard
import com.picke.app.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: String, authCode: String): Result<AuthBoard> {
        val redirectUri = when (provider) {
            "kakao" -> "kakao${BuildConfig.KAKAO_DEBUG_APPKEY}://oauth"
            "google" -> "https://picke.store/oauth/google"
            else -> return Result.failure(IllegalArgumentException("지원하지 않는 로그인 방식입니다: $provider"))
        }

        return authRepository.login(
            provider = provider,
            authCode = authCode,
            redirectUri = redirectUri
        )
    }
}