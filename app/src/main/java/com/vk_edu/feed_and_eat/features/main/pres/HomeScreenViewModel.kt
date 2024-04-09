package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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

    private val privateLargeCardData = mutableStateOf(CardDataModel())
    var largeCardData: State<CardDataModel> = privateLargeCardData

    private val privateCardsDataOfRow1 = mutableStateOf(listOf<CardDataModel>())
    var cardsDataOfRow1: State<List<CardDataModel>> = privateCardsDataOfRow1

    private val privateCardsDataOfRow2 = mutableStateOf(listOf<CardDataModel>())
    var cardsDataOfRow2: State<List<CardDataModel>> = privateCardsDataOfRow2

    private val privateCardsDataOfRow3 = mutableStateOf(listOf<CardDataModel>())
    var cardsDataOfRow3: State<List<CardDataModel>> = privateCardsDataOfRow3

    private val privateColumnWidthDp = mutableStateOf(0.dp)
    var columnWidthDp: State<Dp> = privateColumnWidthDp

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun getLargeCardData() {
        viewModelScope.launch {
            try {
                _loading.value = true
                privateLargeCardData.value = repo.getLargeCardData()
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
                privateCardsDataOfRow1.value = repo.getCardsDataOfRow1()
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
                privateCardsDataOfRow2.value = repo.getCardsDataOfRow2()
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
                privateCardsDataOfRow3.value = repo.getCardsDataOfRow3()
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun columnWidthDpChanged(value: Dp) {
        privateColumnWidthDp.value = value
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}