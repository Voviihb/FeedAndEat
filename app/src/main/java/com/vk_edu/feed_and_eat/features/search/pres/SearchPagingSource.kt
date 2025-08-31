package com.vk_edu.feed_and_eat.features.search.pres

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type

class SearchPagingSource(
    private val searchRecipes: suspend (PagePointer) -> CardsAndSnapshots,
    private val limit: Int
): PagingSource<PagePointer, RecipeCard>() {
    override suspend fun load(params: LoadParams<PagePointer>): LoadResult<PagePointer, RecipeCard> {
        return try {
            val page = params.key ?: PagePointer(null, 1, null, 0)
            val response = searchRecipes(page)

            LoadResult.Page(
                data = response.cards,
                prevKey = if (page.number <= 1)
                    null
                else
                    PagePointer(Type.EXCLUDED_LAST, page.number - 1, null, maxOf(0, response.currentOffset - limit)),
                nextKey = if (!response.hasMore)
                    null
                else
                    PagePointer(Type.EXCLUDED_FIRST, page.number + 1, null, response.currentOffset + limit)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<PagePointer, RecipeCard>): PagePointer? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.let {
                PagePointer(Type.INCLUDED_FIRST, it.number + 1, null, it.offset)
            }
            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.let {
                PagePointer(Type.INCLUDED_LAST, it.number - 1, null, it.offset)
            }
        }
    }
}

