package com.swyp4.team2.ui.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.swyp4.team2.ui.component.CategoryTabBar

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categoryList by viewModel.categoryList.collectAsState()
    val pagingItems = viewModel.explorePagingData.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CategoryTabBar(
            modifier = Modifier.statusBarsPadding()
                .padding(horizontal = 16.dp, vertical=12.dp),
            categories = categoryList,
            selectedCategory = selectedCategory,
            onCategorySelected = { newCategory ->
                viewModel.updateCategory(newCategory)
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id }
            ){ index->
                val card = pagingItems[index]
                if(card != null){
                    Text(
                        text = "${card.id}번 글 | 태그: ${card.hashtags.joinToString(" ")}\n${card.title}"
                    )
                }
            }
        }
    }
}