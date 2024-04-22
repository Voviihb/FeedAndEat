package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.main.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.main.data.repository.HomeRepository
import com.vk_edu.feed_and_eat.features.main.domain.repository.HomeRepoInter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    private val repo: HomeRepoInter = HomeRepository()

    private val _largeCardData = mutableStateOf(CardDataModel())
    var largeCardData: State<CardDataModel> = _largeCardData

    private val _cardsDataOfRow1 = mutableStateOf(listOf<CardDataModel>())
    var cardsDataOfRow1: State<List<CardDataModel>> = _cardsDataOfRow1

    private val _cardsDataOfRow2 = mutableStateOf(listOf<CardDataModel>())
    var cardsDataOfRow2: State<List<CardDataModel>> = _cardsDataOfRow2

    private val _cardsDataOfRow3 = mutableStateOf(listOf<CardDataModel>())
    var cardsDataOfRow3: State<List<CardDataModel>> = _cardsDataOfRow3

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun getLargeCardData() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _largeCardData.value = repo.getLargeCardData()
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun getCardsDataOfRow1() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _cardsDataOfRow1.value = repo.getCardsDataOfRow1()
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun getCardsDataOfRow2() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _cardsDataOfRow2.value = repo.getCardsDataOfRow2()
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun getCardsDataOfRow3() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _cardsDataOfRow3.value = repo.getCardsDataOfRow3()
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