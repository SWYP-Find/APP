package com.swyp4.team2.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.swyp4.team2.domain.model.ContentCard
import com.swyp4.team2.domain.repository.ExploreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val exploreRepository: ExploreRepository
) : ViewModel() {
    private val _categoryList = MutableStateFlow(listOf("전체", "철학", "문학", "예술", "과학","사회"))
    val categoryList : StateFlow<List<String>> = _categoryList.asStateFlow()

    private val _selectedCategory = MutableStateFlow("전체")
    val selectedCategory : StateFlow<String> = _selectedCategory.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val explorePagingData: Flow<PagingData<ContentCard>> = _selectedCategory
        .flatMapLatest { category ->
            exploreRepository.getExploreContent(category, "인기순")
        }
        .cachedIn(viewModelScope)

    fun updateCategory(newCategory: String){
        _selectedCategory.value = newCategory
    }
}