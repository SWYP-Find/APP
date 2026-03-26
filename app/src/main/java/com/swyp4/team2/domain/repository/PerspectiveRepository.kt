package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.PerspectivePage

interface PerspectiveRepository {
    suspend fun getPerspectives(
        battleId: Long,
        cursor: String? = null,
        size: Int = 10,
        optionLabel: String? = null
    ): Result<PerspectivePage>
}