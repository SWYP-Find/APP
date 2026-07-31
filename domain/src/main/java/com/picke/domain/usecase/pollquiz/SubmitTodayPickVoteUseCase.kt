package com.picke.domain.usecase.pollquiz

import com.picke.domain.model.PollQuizVoteBoard
import com.picke.domain.repository.PollQuizRepository

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
