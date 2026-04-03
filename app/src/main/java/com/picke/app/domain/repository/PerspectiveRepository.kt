package com.picke.app.domain.repository

import com.picke.app.domain.model.PerspectiveDetailBoard
import com.picke.app.domain.model.PerspectiveLikeCountBoard
import com.picke.app.domain.model.PerspectiveLikeToggleBoard
import com.picke.app.domain.model.PerspectivePage
import com.picke.app.domain.model.PerspectiveStatusBoard
import com.picke.app.domain.model.PerspectiveUpdateBoard

interface PerspectiveRepository {
    // 1. 관점 목록 조회
    suspend fun getPerspectives(
        battleId: Long,
        cursor: String? = null,
        size: Int = 10,
        optionLabel: String? = null,
        sort: String = "latest"
    ): Result<PerspectivePage>

    // 2. 관점 생성
    suspend fun createPerspective(battleId: Long, content: String): Result<PerspectiveStatusBoard>

    // 3. 내 관점 조회
    suspend fun getMyPerspective(battleId: Long): Result<PerspectiveDetailBoard>

    // 4. 관점 단건 조회
    suspend fun getPerspective(perspectiveId: Long): Result<PerspectiveDetailBoard>

    // 5. 관점 삭제
    suspend fun deletePerspective(perspectiveId: Long): Result<String>

    // 6. 관점 수정
    suspend fun updatePerspective(perspectiveId: Long, content: String): Result<PerspectiveUpdateBoard>

    // 7. 관점 검수 재시도
    suspend fun retryModeration(perspectiveId: Long): Result<String>

    // 8. 관점 좋아요 수 조회
    suspend fun getPerspectiveLikeCount(perspectiveId: Long): Result<PerspectiveLikeCountBoard>

    // 9. 관점 좋아요 등록
    suspend fun likePerspective(perspectiveId: Long): Result<PerspectiveLikeToggleBoard>

    // 10. 관점 좋아요 취소
    suspend fun unlikePerspective(perspectiveId: Long): Result<PerspectiveLikeToggleBoard>
    // 11. 관점 신고
    suspend fun reportPerspective(perspectiveId: Long): Result<String>
}