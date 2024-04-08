package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
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

    private val privateLargeCardData = mutableStateOf(CardDataModel())
    var largeCardData: State<CardDataModel> = privateLargeCardData

    private val privateCardsDataOfRow1 = mutableStateListOf<CardDataModel>()
    var cardsDataOfRow1: List<CardDataModel> = privateCardsDataOfRow1

    private val privateCardsDataOfRow2 = mutableStateListOf<CardDataModel>()
    var cardsDataOfRow2: List<CardDataModel> = privateCardsDataOfRow2

    private val privateCardsDataOfRow3 = mutableStateListOf<CardDataModel>()
    var cardsDataOfRow3: List<CardDataModel> = privateCardsDataOfRow3

    fun getCardTitle1() {
        viewModelScope.launch {
            privateLargeCardData.value = repo.getLargeCardData()
        }
    }

    fun getCardsTitle2() {
        viewModelScope.launch {
            privateCardsDataOfRow1.clear()
            privateCardsDataOfRow1.addAll(repo.getCardsDataOfRow1())
        }
    }

    fun getCardsTitle3() {
        viewModelScope.launch {
            privateCardsDataOfRow2.clear()
            privateCardsDataOfRow2.addAll(repo.getCardsDataOfRow2())
        }
    }

    fun getCardsTitle4() {
        viewModelScope.launch {
            privateCardsDataOfRow3.clear()
            privateCardsDataOfRow3.addAll(repo.getCardsDataOfRow3())
        }
    }
}