package com.picke.app.domain.usecase.auth

import com.picke.app.domain.repository.AuthRepository
import javax.inject.Inject

class RefreshAccessTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(refreshToken: String): Result<Unit> {
        return authRepository.refreshAccessToken(refreshToken)
    }
}
