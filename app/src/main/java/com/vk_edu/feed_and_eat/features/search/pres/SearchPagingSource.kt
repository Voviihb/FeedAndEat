package com.vk_edu.feed_and_eat.features.search.pres

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel

class SearchPagingSource(
    private val searchRecipes: suspend (PagePointer) -> CardsAndSnapshots,
    private val limit: Int
): PagingSource<PagePointer, CardDataModel>() {
    override suspend fun load(params: LoadParams<PagePointer>): LoadResult<PagePointer, CardDataModel> {
        return try {
            val page = params.key ?: PagePointer(null, 1, null)
            val response = searchRecipes(page)

            LoadResult.Page(
                data = response.cards,
                prevKey = if (page.number <= 1 || response.firstDocument == null)
                    null
                else
                    PagePointer(Type.EXCLUDED_LAST, page.number - 1, response.firstDocument),
                nextKey = if (response.cards.size < limit || response.lastDocument == null)
                    null
                else
                    PagePointer(Type.EXCLUDED_FIRST, page.number + 1, response.lastDocument)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<PagePointer, CardDataModel>): PagePointer? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.let {
                Log.d("getRefreshKey", it.type.toString())
                PagePointer(Type.INCLUDED_FIRST, it.number + 1, it.documentSnapshot)
            }
            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.let {
                Log.d("getRefreshKey", it.type.toString())
                PagePointer(Type.INCLUDED_LAST, it.number - 1, it.documentSnapshot)
            }
        }
    }
}

