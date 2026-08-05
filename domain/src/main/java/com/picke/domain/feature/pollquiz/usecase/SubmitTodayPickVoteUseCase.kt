package com.picke.domain.feature.pollquiz.usecase

import com.picke.domain.feature.pollquiz.model.PollQuizVoteBoard
import com.picke.domain.feature.pollquiz.repository.PollQuizRepository

class SubmitTodayPickVoteUseCase(
    private val pollQuizRepository: PollQuizRepository
) {
    suspend operator fun invoke(
        battleId: Long,
        optionId: Long,
        type: String
    ): Result<PollQuizVoteBoard> {
        return if (type == ContentType.QUIZ) {
            pollQuizRepository.submitQuizVote(battleId, optionId)
        } else {
            pollQuizRepository.submitPollVote(battleId, optionId)
        }
    }
}
