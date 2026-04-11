package com.picke.app.domain.repository

import com.picke.app.domain.model.ShareUrl

interface ShareRepository {
    // 철학자 유형 공유 링크 가져오기
    suspend fun getReportShareLink(reportId: Int): Result<ShareUrl>

    // 배틀 공유 링크 가져오기
    suspend fun getBattleShareLink(battleId: Int): Result<ShareUrl>
}