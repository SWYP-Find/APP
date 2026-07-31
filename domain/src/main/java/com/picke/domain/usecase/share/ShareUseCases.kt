package com.picke.domain.usecase.share

data class ShareUseCases(
    val getBattleShareLinkUseCase: GetBattleShareLinkUseCase,
    val getRecapShareKeyUseCase: GetRecapShareKeyUseCase,
    val getRecapDetailUseCase: GetRecapDetailUseCase
)