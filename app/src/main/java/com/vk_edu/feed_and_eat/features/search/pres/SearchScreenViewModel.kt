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

    private var _reloadData = mutableStateOf(false)
    val reloadData: State<Boolean> = _reloadData

    private val _searchForm = mutableStateOf("")
    val searchForm: State<String> = _searchForm

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
        private const val CARBOHYDRATES = "carbohydrates"
        private const val FAT = "fat"
        private const val PROTEIN = "protein"
        private const val SUGAR = "sugar"
    }
}