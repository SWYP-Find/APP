package com.picke.app.domain.repository

import com.picke.app.domain.model.PollQuizVoteBoard
import kotlinx.coroutines.flow.Flow

interface VoteStreamRepository {
    // 실시간 투표 통계 스트림 연결
    fun getVoteStatsStream(battleId: Long): Flow<PollQuizVoteBoard>
}