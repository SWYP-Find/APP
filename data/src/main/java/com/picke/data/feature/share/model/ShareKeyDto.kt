package com.picke.data.feature.share.model

import com.picke.domain.feature.share.model.ShareKey

data class ShareKeyDto(
    val shareKey: String?
)

fun ShareKeyDto.toDomainModel() = ShareKey(
    shareKey = this.shareKey ?: ""
)
