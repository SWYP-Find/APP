package com.picke.domain.usecase.auth

import com.picke.domain.model.AuthBoard
import com.picke.domain.repository.AuthRepository

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