package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.main.data.models.CardDataModel
import com.vk_edu.feed_and_eat.features.main.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    private val repo = HomeRepository()

    private val privateCardTitle1 = mutableStateOf(CardDataModel())
    var cardTitle1: State<CardDataModel> = privateCardTitle1

    private val privateCardsTitle2 = mutableStateListOf<CardDataModel>()
    var cardsTitle2: List<CardDataModel> = privateCardsTitle2

    private val privateCardsTitle3 = mutableStateListOf<CardDataModel>()
    var cardsTitle3: List<CardDataModel> = privateCardsTitle3

    private val privateCardsTitle4 = mutableStateListOf<CardDataModel>()
    var cardsTitle4: List<CardDataModel> = privateCardsTitle4

    fun getCardTitle1() {
        viewModelScope.launch {
            privateCardTitle1.value = repo.getCardTitle1()
        }
    }

    fun getCardsTitle2() {
        viewModelScope.launch {
            privateCardsTitle2.clear()
            repo.getCardsTitle2().forEach { card ->
                privateCardsTitle2.add(card)
            }
        }
    }

    fun getCardsTitle3() {
        viewModelScope.launch {
            privateCardsTitle3.clear()
            repo.getCardsTitle3().forEach { card ->
                privateCardsTitle3.add(card)
            }
        }
    }

    fun getCardsTitle4() {
        viewModelScope.launch {
            privateCardsTitle4.clear()
            repo.getCardsTitle4().forEach { card ->
                privateCardsTitle4.add(card)
            }
        }
    }
}