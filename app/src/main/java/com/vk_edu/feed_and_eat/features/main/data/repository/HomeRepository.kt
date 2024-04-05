package com.vk_edu.feed_and_eat.features.main.data.repository

import com.vk_edu.feed_and_eat.features.main.data.models.CardDataModel
import javax.inject.Inject

class HomeRepository @Inject constructor() {
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
    
    suspend fun getCardTitle1(): CardDataModel {
        return cards[0]
    }

    suspend fun getCardsTitle2(): List<CardDataModel> {
        return cards
    }

    suspend fun getCardsTitle3(): List<CardDataModel> {
        return cards
    }

    suspend fun getCardsTitle4(): List<CardDataModel> {
        return cards
    }
}