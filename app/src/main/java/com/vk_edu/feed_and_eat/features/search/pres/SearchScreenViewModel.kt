package com.vk_edu.feed_and_eat.features.search.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.search.data.repository.SearchTestRepository
import com.vk_edu.feed_and_eat.features.search.domain.repository.SearchRepoInter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchScreenViewModel @Inject constructor() : ViewModel() {
    private val repo: SearchRepoInter = SearchTestRepository()

    val cardsDataPager: Flow<PagingData<CardDataModel>> = Pager(PagingConfig(pageSize = LIMIT)) {
        SearchPagingSource(repo, requestBody, sort, filters, LIMIT)
    }.flow.cachedIn(viewModelScope)

    private var requestBody: String = ""
    private var sort: String = ""
    private var filters: HashMap<String, List<String?>> = hashMapOf(
        TAGS to listOf(),  // tags are stored here as strings
        CALORIES to listOf(null, null),  // min-max limits are stored here in the form of numbers or nulls
        SUGAR to listOf(null, null),
        PROTEIN to listOf(null, null),
        FAT to listOf(null, null),
        CARBOHYDRATES to listOf(null, null)
    )

    private val _searchForm = mutableStateOf("")
    val searchForm: State<String> = _searchForm

    private val _filtersForm = mutableStateOf(FiltersForm(tags = repo.getAllTags().map {
        TagChecking(it, false)
    }))
    val filtersForm: State<FiltersForm> = _filtersForm

    private var _reloadData = mutableStateOf(false)
    val reloadData: State<Boolean> = _reloadData

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun setRequest() {
        viewModelScope.launch {
            try {
                _loading.value = true
                requestBody = _searchForm.value
                _reloadData.value = true
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun requestBodyChanged(value: String) {
        _searchForm.value = value
    }

    private fun getRealNutrientRange(nutrient: Int): List<String?> {
        return listOf(
            _filtersForm.value.nutrients[nutrient].min.ifEmpty { null },
            _filtersForm.value.nutrients[nutrient].max.ifEmpty { null }
        )
    }

    fun setFilters() {
        viewModelScope.launch {
            try {
                _loading.value = true

                filters[TAGS] = _filtersForm.value.tags.filter { it.ckecked }.map { it.name }
                filters[CALORIES] = getRealNutrientRange(CALORIES_INT)
                filters[SUGAR] = getRealNutrientRange(SUGAR_INT)
                filters[CARBOHYDRATES] = getRealNutrientRange(CARBOHYDRATES_INT)
                filters[FAT] = getRealNutrientRange(FAT_INT)
                filters[PROTEIN] = getRealNutrientRange(PROTEIN_INT)

                _reloadData.value = true
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
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

    fun tagCheckingChanged(tag: Int, checked: Boolean) {
        val tags = _filtersForm.value.tags.toMutableList()
        tags[tag] = TagChecking(tags[tag].name, checked)
        _filtersForm.value = _filtersForm.value.copy(tags = tags)
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

    companion object {
        private const val LIMIT = 20

        private const val TAGS = "tags"
        private const val CALORIES = "calories"
        private const val SUGAR = "sugar"
        private const val CARBOHYDRATES = "carbohydrates"
        private const val FAT = "fat"
        private const val PROTEIN = "protein"

        private const val CALORIES_INT = 0
        private const val SUGAR_INT = 1
        private const val CARBOHYDRATES_INT = 2
        private const val FAT_INT = 3
        private const val PROTEIN_INT = 4
    }
}