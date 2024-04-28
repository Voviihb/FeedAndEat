package com.vk_edu.feed_and_eat.features.search.pres

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SearchFilters
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel

class SearchPagingSource(
    private val searchRecipes: (SearchFilters, Direction) -> List<CardDataModel>,
    private val searchFilters: SearchFilters
): PagingSource<Int, CardDataModel>() {
    private var currentPage = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CardDataModel> {
        return try {
            val page = params.key ?: 1
            val response = searchRecipes(searchFilters, if (page >= currentPage) Direction.FORWARD else Direction.BACK)
            currentPage = page

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty() || response.size < searchFilters.limit) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CardDataModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val FORWARD = 0
        private const val BACK = 1
    }
}

