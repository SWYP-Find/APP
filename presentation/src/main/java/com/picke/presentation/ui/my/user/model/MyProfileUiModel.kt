package com.picke.presentation.ui.my.user.model

import com.picke.domain.feature.mypage.model.MyProfile

data class MyProfileUiModel(
    val userTag: String,
    val nickname: String,
    val characterType: String,
    val characterLabel: String,
    val characterImageUrl: String,
    val mannerTemperature: Double
)

fun MyProfile.toUiModel() = MyProfileUiModel(
    userTag = userTag,
    nickname = nickname,
    characterType = characterType,
    characterLabel = characterLabel,
    characterImageUrl = characterImageUrl,
    mannerTemperature = mannerTemperature
)