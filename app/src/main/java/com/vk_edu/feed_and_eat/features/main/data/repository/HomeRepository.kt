package com.vk_edu.feed_and_eat.features.main.data.repository

import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.main.domain.repository.HomeRepoInter
import javax.inject.Inject

class HomeRepository @Inject constructor() : HomeRepoInter {
    private val cards = List(8) {
        RecipeCard(
            recipeId = "qN4RbtLwAi6WmfFwHvfq",
            image = "https://img.spoonacular.com/recipes/641732-556x370.jpg",
            ingredients = 15,
            steps = 10,
            name = "Dulce De Leche Swi Amaretto Frozen Yogurt",
            rating = 3.0,
            cooked = 156
        )
    }

    override suspend fun getLargeCardData(): RecipeCard {
        return cards[0]
    }

    override suspend fun getCardsDataOfRow1(): List<RecipeCard> {
        return cards
    }

    override suspend fun getCardsDataOfRow2(): List<RecipeCard> {
        return cards
    }

    override suspend fun getCardsDataOfRow3(): List<RecipeCard> {
        return cards
    }
}