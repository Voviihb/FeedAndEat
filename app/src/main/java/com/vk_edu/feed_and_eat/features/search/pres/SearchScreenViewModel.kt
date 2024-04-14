package com.vk_edu.feed_and_eat.features.search.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.search.data.repository.SearchRepository
import com.vk_edu.feed_and_eat.features.search.domain.repository.SearchRepoInter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchScreenViewModel @Inject constructor() : ViewModel() {
    private val repo: SearchRepoInter = SearchRepository()

    val cardsDataPager: Flow<PagingData<CardDataModel>> = Pager(PagingConfig(pageSize = 20)) {
        CardsDataPagingSource()
    }.flow.cachedIn(viewModelScope)

    private var requestBody: String = ""
    private var sort: String = ""
    private var filters: HashMap<String, List<String?>> = hashMapOf(
        "includedIngredients" to listOf(),  // lists of strings are stored here
        "excludedIngredients" to listOf(),
        "tags" to listOf(),
        "calories" to listOf(null, null),  // min-max limits are stored here in the form of numbers or nulls
        "sugar" to listOf(null, null),
        "protein" to listOf(null, null),
        "fat" to listOf(null, null),
        "carbohydrates" to listOf(null, null)
    )

    private var _reloadData = mutableStateOf(false)
    val reloadData: State<Boolean> = _reloadData

    private val _searchForm = mutableStateOf(SearchForm(""))
    val searchForm: State<SearchForm> = _searchForm

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun setRequest() {
        viewModelScope.launch {
            try {
                _loading.value = true
                requestBody = _searchForm.value.requestBody
                _reloadData.value = true
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    /* TODO develop FiltersForm and replace arguments with it */
    /*private fun getRangeList(range: NutrientRangeModel): List<String?> {
        return listOf(
            if (range.min == "") null else range.min,
            if (range.max == "") null else range.max
        )
    }

    fun setSortAndFilters(
        sortBy: Int,
        includedIngredients: List<String>,
        excludedIngredients: List<String>,
        tags: List<String>,
        calories: NutrientRangeModel,
        sugar: NutrientRangeModel,
        protein: NutrientRangeModel,
        fat: NutrientRangeModel,
        carbohydrates: NutrientRangeModel
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true

                sort = listOf("newness", "rating", "popularity")[sortBy]
                filters["includedIngredients"] = includedIngredients
                filters["excludedIngredients"] = excludedIngredients
                filters["tags"] = tags
                filters["calories"] = getRangeList(calories)
                filters["sugar"] = getRangeList(sugar)
                filters["protein"] = getRangeList(protein)
                filters["fat"] = getRangeList(fat)
                filters["carbohydrates"] = getRangeList(carbohydrates)

                _reloadData.value = true
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }*/

    fun requestBodyChanged(value: String) {
        _searchForm.value = SearchForm(value)
    }

    fun reloadDataFinished() {
        _reloadData.value = false
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    inner class CardsDataPagingSource: PagingSource<Int, CardDataModel>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CardDataModel> {
            _loading.value = true
            return try {
                val page = params.key ?: 1
                val limit = params.loadSize
                val response = repo.getCardsData(requestBody, sort, filters, page, limit)
                _loading.value = false

                LoadResult.Page(
                    data = response,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (response.isEmpty() || response.size < limit) page + 1 else page + 1
                )
            } catch (e: Exception) {
                onError(e)
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
}