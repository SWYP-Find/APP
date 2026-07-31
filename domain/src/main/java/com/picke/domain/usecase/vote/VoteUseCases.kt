package com.picke.domain.usecase.vote

data class VoteUseCases(
    val getMyVoteHistoryUseCase: GetMyVoteHistoryUseCase,
    val getVoteStatsUseCase: GetVoteStatsUseCase,
    val submitVoteUseCase: SubmitVoteUseCase
)