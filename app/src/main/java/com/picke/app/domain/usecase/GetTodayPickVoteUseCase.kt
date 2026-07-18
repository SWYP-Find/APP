package com.picke.app.domain.usecase

import com.picke.app.domain.model.PollQuizVoteBoard
import com.picke.app.domain.repository.PollQuizRepository
import com.picke.app.util.ContentType
import javax.inject.Inject

class GetTodayPickVoteUseCase @Inject constructor(
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
