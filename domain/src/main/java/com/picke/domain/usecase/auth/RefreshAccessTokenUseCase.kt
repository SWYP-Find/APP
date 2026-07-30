package com.picke.domain.usecase.auth

import com.picke.domain.repository.AuthRepository

class RefreshAccessTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(refreshToken: String): Result<Unit> {
        return authRepository.refreshAccessToken(refreshToken)
    }
}
