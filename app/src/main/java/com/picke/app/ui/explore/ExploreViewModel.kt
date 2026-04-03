package com.picke.app.ui.explore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.picke.app.domain.repository.ExploreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.paging.map

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val exploreRepository: ExploreRepository
) : ViewModel() {

    private companion object {
        const val TAG = "ExploreFlow"
    }

    // 탭 카테고리 (기본값: 전체)
    private val _selectedCategory = MutableStateFlow("전체")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // 정렬 (기본값: LATEST)
    private val _selectedSort = MutableStateFlow("POPULAR")
    val selectedSort: StateFlow<String> = _selectedSort.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val explorePagingData: Flow<PagingData<ExploreUiModel>> = combine(_selectedCategory, _selectedSort) { cat, sort ->
        Pair(cat, sort)
    }.flatMapLatest { (category, sort) ->
        Pager(PagingConfig(pageSize = 10)) {
            val apiCategory = if (category == "전체") null else category
            ExplorePagingSource(exploreRepository, apiCategory, sort)
        }.flow
    }.map { pagingData ->
        pagingData.map { item ->
            item
        }
    }.cachedIn(viewModelScope)

    fun updateCategory(newCategory: String) {
        if (_selectedCategory.value != newCategory) {
            _selectedCategory.value = newCategory
        }
    }

    fun updateSort(newSort: String) {
        if (_selectedSort.value != newSort) {
            _selectedSort.value = newSort
        }
    }
}