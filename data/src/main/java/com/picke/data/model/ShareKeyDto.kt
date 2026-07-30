package com.picke.data.model

import com.picke.domain.model.ShareKey

data class ShareKeyDto(
    val shareKey: String?
)

fun ShareKeyDto.toDomainModel() = ShareKey(
    shareKey = this.shareKey ?: ""
)
