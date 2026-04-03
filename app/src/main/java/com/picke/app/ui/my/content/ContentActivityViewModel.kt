package com.picke.app.ui.my.content

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.MyContentActivityItem
import com.picke.app.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContentActivityUiState(
    val commentList: List<MyContentActivityItem> = emptyList(),
    val commentOffset: Int? = 0,
    val hasMoreComments: Boolean = true,

    val likeList: List<MyContentActivityItem> = emptyList(),
    val likeOffset: Int? = 0,
    val hasMoreLikes: Boolean = true,

    val isLoading: Boolean = false,
    val isPagingLoading: Boolean = false
)

@HiltViewModel
class ContentActivityViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContentActivityUiState())
    val uiState: StateFlow<ContentActivityUiState> = _uiState.asStateFlow()

    private val TAG = "ContentActivityFlow"

    init {
        fetchContentActivity()
    }
    private fun fetchContentActivity() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val commentsDeferred = async {
                myPageRepository.getMyContentActivities(offset = 0, size = 20, activityType = "COMMENT")
            }
            val likesDeferred = async {
                myPageRepository.getMyContentActivities(offset = 0, size = 20, activityType = "LIKE")
            }

            val commentsResult = commentsDeferred.await()
            val likesResult = likesDeferred.await()

            commentsResult.onSuccess { data ->
                Log.d(TAG, "댓글 불러오기 성공! 아이템 개수: ${data.items.size}")
                Log.d(TAG, "댓글 데이터 상세: $data")
            }.onFailure { exception ->
                Log.e(TAG, "댓글 불러오기 실패: ${exception.message}")
            }

            likesResult.onSuccess { data ->
                Log.d(TAG, "좋아요 불러오기 성공! 아이템 개수: ${data.items.size}")
                Log.d(TAG, "좋아요 데이터 상세: $data")
            }.onFailure { exception ->
                Log.e(TAG, "좋아요 불러오기 실패: ${exception.message}")
            }

            val comments = commentsResult.getOrNull()?.items ?: emptyList()
            val likes = likesResult.getOrNull()?.items ?: emptyList()

            _uiState.update { state ->
                state.copy(
                    commentList = commentsResult.getOrNull()?.items ?: emptyList(),
                    commentOffset = commentsResult.getOrNull()?.nextOffset,
                    hasMoreComments = commentsResult.getOrNull()?.hasNext ?: false,

                    likeList = likesResult.getOrNull()?.items ?: emptyList(),
                    likeOffset = likesResult.getOrNull()?.nextOffset,
                    hasMoreLikes = likesResult.getOrNull()?.hasNext ?: false,

                    isLoading = false
                )
            }
        }
    }

    fun loadMore(activityType: String) {
        val currentState = _uiState.value

        if (currentState.isPagingLoading) return

        val (currentOffset, hasNext) = if (activityType == "COMMENT") {
            currentState.commentOffset to currentState.hasMoreComments
        } else {
            currentState.likeOffset to currentState.hasMoreLikes
        }

        if (!hasNext || currentOffset == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPagingLoading = true) }
            Log.d(TAG, "페이징 시작: 타입=$activityType, offset=$currentOffset")

            val result = myPageRepository.getMyContentActivities(
                offset = currentOffset,
                size = 20,
                activityType = activityType
            )

            result.onSuccess { pageData ->
                Log.d(
                    TAG,
                    "페이징 성공: 타입=$activityType, 추가된 개수=${pageData.items.size}, 다음 offset=${pageData.nextOffset}"
                )
                _uiState.update { state ->
                    if (activityType == "COMMENT") {
                        state.copy(
                            commentList = state.commentList + pageData.items,
                            commentOffset = pageData.nextOffset,
                            hasMoreComments = pageData.hasNext,
                            isPagingLoading = false
                        )
                    } else {
                        state.copy(
                            likeList = state.likeList + pageData.items,
                            likeOffset = pageData.nextOffset,
                            hasMoreLikes = pageData.hasNext,
                            isPagingLoading = false
                        )
                    }
                }
            }.onFailure { exception ->
                Log.e(TAG, "페이징 실패: 타입=$activityType, 에러=${exception.message}")
                _uiState.update { it.copy(isPagingLoading = false) }
            }
        }
    }
}

