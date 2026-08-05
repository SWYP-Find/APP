package com.picke.domain.feature.auth.usecase

import com.picke.domain.feature.auth.model.AuthBoard
import com.picke.domain.feature.auth.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        redirectUri: String,
        provider: String,
        authCode: String
    ): Result<AuthBoard> {
        return authRepository.login(
            provider = provider,
            authCode = authCode,
            redirectUri = redirectUri
        )
    }
}