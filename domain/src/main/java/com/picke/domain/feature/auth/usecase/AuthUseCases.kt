package com.picke.domain.feature.auth.usecase

data class AuthUseCases(
    val loginUseCase: LoginUseCase,
    val logoutUseCase: LogoutUseCase,
    val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    val withdrawUseCase: WithdrawUseCase
)