package com.vk_edu.feed_and_eat.features.search.pres

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SearchFilters
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val _recipesRepo: RecipesRepoImpl
) : ViewModel() {
    val cardsDataPager: Flow<PagingData<CardDataModel>> = Pager(PagingConfig(pageSize = LIMIT)) {
        SearchPagingSource(::searchRecipes, LIMIT)
    }.flow.cachedIn(viewModelScope)

    private var currentPage = 1

    private var searchFilters = SearchFilters(limit = LIMIT)

    private val _searchForm = mutableStateOf("")
    val searchForm: State<String> = _searchForm

    private val _sortingForm = mutableStateOf(0)
    val sortingForm: State<Int> = _sortingForm

    private val _filtersForm = mutableStateOf(FiltersForm())
    val filtersForm: State<FiltersForm> = _filtersForm

    private var _reloadData = mutableStateOf(false)
    val reloadData: State<Boolean> = _reloadData

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    init {
        viewModelScope.launch {
            try {
                _recipesRepo.loadTags().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            _filtersForm.value = _filtersForm.value.copy(
                                tags = response.data.map { TagChecking(it, false) }
                            )
                        }

                        is Response.Failure -> {
                            onError(response.e)
                        }
                    }
                }
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun setRequest() {
        viewModelScope.launch {
            try {
                _loading.value = true

                searchFilters = searchFilters.copy(
                    startsWith = _searchForm.value
                )

                _reloadData.value = true
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun requestChanged(value: String) {
        _searchForm.value = value
    }

    fun setSortingAndFilters() {
        viewModelScope.launch {
            try {
                _loading.value = true

                searchFilters = searchFilters.copy(
                    sort = _sortingForm.value,
                    tags = _filtersForm.value.tags.filter { it.ckecked }.map { it.name },
                    caloriesMin = _filtersForm.value.nutrients[CALORIES].min.ifEmpty { null }?.toDouble() ?: 0.0,
                    caloriesMax = _filtersForm.value.nutrients[CALORIES].max.ifEmpty { null }?.toDouble() ?: 10e9,
                    sugarMin = _filtersForm.value.nutrients[SUGAR].min.ifEmpty { null }?.toDouble() ?: 0.0,
                    sugarMax = _filtersForm.value.nutrients[SUGAR].max.ifEmpty { null }?.toDouble() ?: 10e9,
                    carbohydratesMin = _filtersForm.value.nutrients[CARBOHYDRATES].min.ifEmpty { null }?.toDouble() ?: 0.0,
                    carbohydratesMax = _filtersForm.value.nutrients[CARBOHYDRATES].max.ifEmpty { null }?.toDouble() ?: 10e9,
                    fatMin = _filtersForm.value.nutrients[FAT].min.ifEmpty { null }?.toDouble() ?: 0.0,
                    fatMax = _filtersForm.value.nutrients[FAT].max.ifEmpty { null }?.toDouble() ?: 10e9,
                    proteinMin = _filtersForm.value.nutrients[PROTEIN].min.ifEmpty { null }?.toDouble() ?: 0.0,
                    proteinMax = _filtersForm.value.nutrients[PROTEIN].max.ifEmpty { null }?.toDouble() ?: 10e9
                )

                _reloadData.value = true
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun sortingChanged(sorting: Int) {
        _sortingForm.value = sorting
    }

    fun tagCheckingChanged(tag: Int) {
        val tags = _filtersForm.value.tags.toMutableList()
        tags[tag] = TagChecking(tags[tag].name, !tags[tag].ckecked)
        _filtersForm.value = _filtersForm.value.copy(tags = tags)
    }

    fun nutrientMinChanged(nutrient: Int, text: String) {
        val nutrients = _filtersForm.value.nutrients.toMutableList()
        nutrients[nutrient] = NutrientRange(text, nutrients[nutrient].max)
        _filtersForm.value = _filtersForm.value.copy(nutrients = nutrients)
    }

    fun nutrientMaxChanged(nutrient: Int, text: String) {
        val nutrients = _filtersForm.value.nutrients.toMutableList()
        nutrients[nutrient] = NutrientRange(nutrients[nutrient].min, text)
        _filtersForm.value = _filtersForm.value.copy(nutrients = nutrients)
    }

    fun reloadDataFinished() {
        _reloadData.value = false
    }

    suspend fun searchRecipes(pagePointer: PagePointer): CardsAndSnapshots {
        val result = viewModelScope.async {
            var result = CardsAndSnapshots(listOf(), null, null)
            try {
                _recipesRepo.loadSearchRecipes(searchFilters, pagePointer.type, pagePointer.documentSnapshot).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            result = CardsAndSnapshots(
                                response.data.recipes.map { fullRecipe ->
                                    CardDataModel(
                                        link = fullRecipe.image ?: "",
                                        ingredients = fullRecipe.ingredients.size,
                                        steps = fullRecipe.instructions.size,
                                        name = fullRecipe.name,
                                        rating = fullRecipe.rating,
                                        cooked = fullRecipe.cooked
                                    )
                                },
                                response.data.startDocument,
                                response.data.endDocument
                            )
                        }

                        is Response.Failure -> {
                            onError(response.e)
                        }
                    }
                }
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
            return@async result
        }
        return result.await()
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    companion object {
        private const val LIMIT = 20

        private const val CALORIES = 0
        private const val SUGAR = 1
        private const val CARBOHYDRATES = 2
        private const val FAT = 3
        private const val PROTEIN = 4
    }
}