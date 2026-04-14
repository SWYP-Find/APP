package com.picke.app.domain.repository

import com.picke.app.domain.model.MyRecapBoard
import com.picke.app.domain.model.ShareKey
import com.picke.app.domain.model.ShareUrl

interface ShareRepository {
    // 철학자 유형 공유 링크 가져오기
    suspend fun getReportShareLink(reportId: Int): Result<ShareUrl>

    // 배틀 공유 링크 가져오기
    suspend fun getBattleShareLink(battleId: Int): Result<ShareUrl>

    // 나의 철학자 유형 공유키 가져오기
    suspend fun getRecapShareKey(): Result<ShareKey>

    // 타인의 철학자 유형 정보 가져오기
    suspend fun getRecapDetail(shareKey: String): Result<MyRecapBoard>
}