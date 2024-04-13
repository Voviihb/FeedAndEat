package com.vk_edu.feed_and_eat.features.search.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.search.data.repository.SearchRepository
import com.vk_edu.feed_and_eat.features.search.domain.models.NutrientRangeModel
import com.vk_edu.feed_and_eat.features.search.domain.repository.SearchRepoInter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchScreenViewModel @Inject constructor() : ViewModel() {
    private val repo: SearchRepoInter = SearchRepository()

    private var requestBody: String = ""
    private var sort: String = ""
    private var filters: HashMap<String, List<String?>> = hashMapOf(
        "includedIngredients" to listOf(),
        "excludedIngredients" to listOf(),
        "tags" to listOf(),
        "calories" to listOf(null, null),
        "sugar" to listOf(null, null),
        "protein" to listOf(null, null),
        "fat" to listOf(null, null),
        "carbohydrates" to listOf(null, null)
    )
    private var page: Int = 1

    private val _searchForm = mutableStateOf(SearchForm(""))
    val searchForm: State<SearchForm> = _searchForm

    private val _cardsData = mutableStateListOf<CardDataModel>()
    val cardsData: List<CardDataModel> = _cardsData

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun setRequest() {
        viewModelScope.launch {
            try {
                _loading.value = true
                requestBody = _searchForm.value.requestBody
                page = 1
                _cardsData.clear()
                _cardsData.addAll(repo.getCardsData(requestBody, sort, filters, page))
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    private fun getRangeList(range: NutrientRangeModel): List<String?> {
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

                page = 1
                _cardsData.clear()
                _cardsData.addAll(repo.getCardsData(requestBody, sort, filters, page))
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun addCardsData() {
        viewModelScope.launch {
            try {
                _loading.value = true
                page += 1
                _cardsData.addAll(repo.getCardsData(requestBody, sort, filters, page))
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun requestBodyChanged(value: String) {
        _searchForm.value = SearchForm(value)
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}