package com.picke.app.data.model

import com.picke.app.domain.model.ShareKey

data class ShareKeyDto(
    val shareKey: String?
)

fun ShareKeyDto.toDomainModel() = ShareKey(
    shareKey = this.shareKey ?: ""
)
