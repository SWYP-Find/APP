package com.picke.app.ui.explore

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.picke.app.domain.repository.ExploreRepository

class ExplorePagingSource(
    private val repository: ExploreRepository,
    private val category: String?,
    private val sort: String
) : PagingSource<Int, ExploreUiModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExploreUiModel> {
        return try {
            // 처음 로드할 때는 offset 0을 사용합니다.
            val currentOffset = params.key ?: 0

            val response = repository.searchBattles(
                category = category,
                sort = sort,
                offset = currentOffset,
                size = params.loadSize
            )

            response.fold(
                onSuccess = { pageBoard ->
                    val uiModels = pageBoard.items.map { it.toUiModel() }
                    LoadResult.Page(
                        data = uiModels,
                        prevKey = if (currentOffset == 0) null else Math.max(0, currentOffset - params.loadSize),
                        nextKey = pageBoard.nextOffset
                    )
                },
                onFailure = { LoadResult.Error(it) }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ExploreUiModel>): Int? {
        return null
    }
}