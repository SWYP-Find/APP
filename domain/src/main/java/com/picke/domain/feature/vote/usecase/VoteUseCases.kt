package com.picke.domain.feature.vote.usecase

data class VoteUseCases(
    val getMyVoteHistoryUseCase: GetMyVoteHistoryUseCase,
    val getVoteStatsUseCase: GetVoteStatsUseCase,
    val submitVoteUseCase: SubmitVoteUseCase
)