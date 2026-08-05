package com.picke.domain.feature.share.repository

import com.picke.domain.feature.mypage.model.MyRecapBoard
import com.picke.domain.feature.share.model.ShareKey
import com.picke.domain.feature.share.model.ShareUrl

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