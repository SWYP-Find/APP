package com.picke.domain.usecase.auth

import com.picke.domain.repository.AuthRepository

class RefreshAccessTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.refreshAccessToken()
    }
}
