package com.picke.presentation.ui.my.user.model

import com.picke.domain.feature.mypage.model.MyPhilosopher

data class MyPhilosopherUiModel(
    val philosopherType: String,
    val philosopherLabel: String,
    val typeName: String,
    val description: String,
    val keywordTags: List<String>,
    val imageUrl: String
)

fun MyPhilosopher.toUiModel() = MyPhilosopherUiModel(
    philosopherType = philosopherType,
    philosopherLabel = philosopherLabel,
    typeName = typeName,
    description = description,
    keywordTags = keywordTags,
    imageUrl = imageUrl
)