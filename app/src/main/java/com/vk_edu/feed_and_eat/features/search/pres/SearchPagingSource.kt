package com.vk_edu.feed_and_eat.features.search.pres

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel

class SearchPagingSource(
    private val searchRecipes: suspend (Int) -> List<CardDataModel>,
    private val limit: Int
): PagingSource<Int, CardDataModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CardDataModel> {
        return try {
            val page = params.key ?: 1
            val response = searchRecipes(page)

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty() || response.size < limit) null else page + 1
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
}

