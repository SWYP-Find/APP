package com.picke.presentation.ui.my.user.model

import com.picke.domain.feature.mypage.model.MyTier

data class MyTierUiModel(
    val tierCode: String,
    val tierLabel: String,
    val currentPoint: Int
)

fun MyTier.toUiModel() = MyTierUiModel(
    tierCode = tierCode,
    tierLabel = tierLabel,
    currentPoint = currentPoint
)