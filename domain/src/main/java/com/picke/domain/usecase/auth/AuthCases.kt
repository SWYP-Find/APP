package com.picke.domain.usecase.auth

data class AuthUseCases(
    val loginUseCase: LoginUseCase,
    val logoutUseCase: LogoutUseCase,
    val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    val withdrawUseCase: WithdrawUseCase
)