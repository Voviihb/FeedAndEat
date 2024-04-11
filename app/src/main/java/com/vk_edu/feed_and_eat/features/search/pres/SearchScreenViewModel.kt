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

    private var request: String = ""
    private var sort: String = ""
    private var filters: HashMap<String, List<String?>> = hashMapOf()

    private val privateCardsData = mutableStateOf(mutableListOf<CardDataModel>())
    var cardsData: State<List<CardDataModel>> = privateCardsData

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun setRequest(newRequest: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                request = newRequest
                privateCardsData.value.clear()
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    private fun getRangeList(range: NutrientRangeModel): List<String?> {
        return listOf(
            if (range.start == "") null else range.start,
            if (range.end == "") null else range.end
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

                privateCardsData.value.clear()
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
                privateCardsData.value.addAll(repo.getCardsData(request, sort, filters))
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}