package com.vk_edu.feed_and_eat.features.search.data.repository

import com.vk_edu.feed_and_eat.features.dishes.domain.models.FiltersDTO
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SortFilter
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.search.domain.repository.SearchRepoInter
import javax.inject.Inject

class SearchRepository @Inject constructor() : SearchRepoInter {
    private val cards = List(20) {
        CardDataModel(
            link = "https://img.spoonacular.com/recipes/641732-556x370.jpg",
            ingredients = 15,
            steps = 10,
            name = "Dulce De Leche Swi Amaretto Frozen Yogurt",
            rating = 3.0,
            cooked = 156
        )
    }
    private val tags = listOf(
        "antipasti",
        "antipasto",
        "appetizer",
        "batter",
        "beverage",
        "bread",
        "breakfast",
        "brunch",
        "condiment",
        "crust",
        "dessert",
        "dinner",
        "dip",
        "drink",
        "fingerfood",
        "frosting",
        "hor d'oeuvre",
        "icing",
        "lunch",
        "main course",
        "main dish",
        "marinade",
        "morning meal",
        "salad",
        "sauce",
        "seasoning",
        "side dish",
        "snack",
        "soup",
        "spread",
        "starter"
    )

    private var currentPage = 1

    override suspend fun getCardsData(
        request: String,
        sort: String,
        filters: HashMap<String, List<String?>>,
        page: Int,
        limit: Int
    ): List<CardDataModel> {
        val parameters = FiltersDTO(
            sort = SortFilter.valueOf(sort),
            limit = limit.toLong(),
            startsWith = request,
            tags = filters[TAGS]?.map { it ?: "" },
            caloriesMin = filters[CALORIES]?.get(0)?.toDouble() ?: 0.0,
            caloriesMax = filters[CALORIES]?.get(1)?.toDouble() ?: 10e9,
            sugarMin = filters[SUGAR]?.get(0)?.toDouble() ?: 0.0,
            sugarMax = filters[SUGAR]?.get(1)?.toDouble() ?: 10e9,
            carbohydratesMin = filters[CARBOHYDRATES]?.get(0)?.toDouble() ?: 0.0,
            carbohydratesMax = filters[CARBOHYDRATES]?.get(1)?.toDouble() ?: 10e9,
            fatMin = filters[FAT]?.get(0)?.toDouble() ?: 0.0,
            fatMax = filters[FAT]?.get(1)?.toDouble() ?: 10e9,
            proteinMin = filters[PROTEIN]?.get(0)?.toDouble() ?: 0.0,
            proteinMax = filters[PROTEIN]?.get(1)?.toDouble() ?: 10e9
        )

        return cards
    }

    override fun getAllTags(): List<String> {
        return tags
    }

    companion object {
        private const val TAGS = "tags"
        private const val CALORIES = "calories"
        private const val SUGAR = "sugar"
        private const val CARBOHYDRATES = "carbohydrates"
        private const val FAT = "fat"
        private const val PROTEIN = "protein"
    }
}