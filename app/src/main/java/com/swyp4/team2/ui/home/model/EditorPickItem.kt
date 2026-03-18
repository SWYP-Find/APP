package com.swyp4.team2.ui.home.model

import androidx.compose.ui.text.LinkAnnotation
import com.swyp4.team2.R

data class EditorPickItem(
    val id: Int,
    val imageUrl: String,
    val leftOpinion: String,
    val rightOpinion: String,
    val title: String,
    val description: String,
    val tags: List<String>,
    val viewCount: String
)

val dummyEditorPickList = listOf(
    EditorPickItem(
        id = 1,
        imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1000&auto=format&fit=crop",
        leftOpinion = "예술이다",
        rightOpinion = "쓰레기다",
        title = "뒤샹의 변기, 예술인가 도발인가",
        description = "뒤샹의 변기 <샘>은 “무엇이 예술인가”를 묻는 작품이다.",
        tags = listOf("예술", "현대미술"),
        viewCount = "847"
    ),
    EditorPickItem(
        id = 2,
        imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1000&auto=format&fit=crop",
        leftOpinion = "창의적이다",
        rightOpinion = "난해하다",
        title = "현대 미술의 경계",
        description = "선 하나만 그어놓고 수십억? 현대 미술의 가치에 대하여.",
        tags = listOf("이슈", "토론"),
        viewCount = "1,204"
    )
)