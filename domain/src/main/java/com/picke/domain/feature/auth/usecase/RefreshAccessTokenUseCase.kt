package com.picke.domain.feature.auth.usecase

import com.picke.domain.feature.auth.repository.AuthRepository

class RefreshAccessTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.refreshAccessToken()
    }
}
