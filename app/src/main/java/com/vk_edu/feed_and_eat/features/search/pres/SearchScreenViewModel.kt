package com.vk_edu.feed_and_eat.features.search.pres

import androidx.compose.runtime.State
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

    private val privateSearchForm = mutableStateOf(SearchForm(""))
    var searchForm: State<SearchForm> = privateSearchForm

    private val privateCardsData = mutableStateOf(mutableListOf<CardDataModel>())
    val cardsData: State<List<CardDataModel>> = privateCardsData

    private val privateLoading = mutableStateOf(false)
    val loading: State<Boolean> = privateLoading

    private val privateErrorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = privateErrorMessage

    fun setRequest() {
        viewModelScope.launch {
            try {
                privateLoading.value = true
                requestBody = privateSearchForm.value.requestBody
                page = 1
                privateCardsData.value.clear()
                privateCardsData.value.addAll(repo.getCardsData(requestBody, sort, filters, page))
            } catch (e: Exception) {
                onError(e)
            }
            privateLoading.value = false
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
                privateLoading.value = true

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
                privateCardsData.value.clear()
                privateCardsData.value.addAll(repo.getCardsData(requestBody, sort, filters, page))
            } catch (e: Exception) {
                onError(e)
            }
            privateLoading.value = false
        }
    }

    fun addCardsData() {
        viewModelScope.launch {
            try {
                privateLoading.value = true
                page += 1
                privateCardsData.value.addAll(repo.getCardsData(requestBody, sort, filters, page))
            } catch (e: Exception) {
                onError(e)
            }
            privateLoading.value = false
        }
    }

    fun requestBodyChanged(value: String) {
        privateSearchForm.value = SearchForm(value)
    }

    private fun onError(message: Exception?) {
        privateErrorMessage.value = message
        privateLoading.value = false
    }

    fun clearError() {
        privateErrorMessage.value = null
    }
}