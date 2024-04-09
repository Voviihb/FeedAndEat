package com.vk_edu.feed_and_eat.features.main.data.repository

import com.vk_edu.feed_and_eat.features.main.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.main.domain.repository.HomeRepoInter
import javax.inject.Inject

class HomeRepository @Inject constructor() : HomeRepoInter {
    private val cards = List(8) {
        CardDataModel(
            link = "https://img.spoonacular.com/recipes/641732-556x370.jpg",
            ingredients = 15,
            steps = 10,
            name = "Dulce De Leche Swi Amaretto Frozen Yogurt",
            rating = 3.0,
            cooked = 156
        )
    }

    override suspend fun getLargeCardData(): CardDataModel {
        return cards[0]
    }

    override suspend fun getCardsDataOfRow1(): List<CardDataModel> {
        return cards
    }

    override suspend fun getCardsDataOfRow2(): List<CardDataModel> {
        return cards
    }

    override suspend fun getCardsDataOfRow3(): List<CardDataModel> {
        return cards
    }
}