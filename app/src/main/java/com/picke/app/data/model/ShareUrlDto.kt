package com.picke.app.data.model

import com.picke.app.domain.model.ShareUrl

data class ShareUrlDto(
    val shareUrl: String?
)

// DTO -> Domain 매퍼
fun ShareUrlDto.toDomainModel() = ShareUrl(
    shareUrl = this.shareUrl ?: "",
)
