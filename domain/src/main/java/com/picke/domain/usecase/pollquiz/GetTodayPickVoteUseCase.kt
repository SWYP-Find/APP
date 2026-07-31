package com.picke.domain.usecase.pollquiz

import com.picke.domain.model.PollQuizVoteBoard
import com.picke.domain.repository.PollQuizRepository

object ContentType {
    const val QUIZ = "QUIZ"
    const val POLL = "POLL"
}

class GetTodayPickVoteUseCase(
    private val pollQuizRepository: PollQuizRepository
) {
    suspend operator fun invoke(battleId: Long, type: String): Result<PollQuizVoteBoard> {
        return if (type == ContentType.QUIZ) {
            pollQuizRepository.getMyQuizVote(battleId)
        } else {
            pollQuizRepository.getMyPollVote(battleId)
        }
    }
}
