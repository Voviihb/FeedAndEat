package com.vk_edu.feed_and_eat.features.search.data.repository

import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.search.domain.repository.SearchRepoInter
import javax.inject.Inject

class SearchTestRepository @Inject constructor() : SearchRepoInter {
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

    private var cnt = 0

    override suspend fun getCardsData(
        request: String,
        sort: String,
        filters: HashMap<String, List<String?>>,
        page: Int,
        limit: Int
    ): List<CardDataModel> {
        val arr = mutableListOf<CardDataModel>()
        for (elem in cards) {
            val newElem = elem.copy(
                ingredients = cnt,
                name = request + elem.name,
                steps = page
            )
            arr.add(newElem)
        }
        cnt += 1
        return arr
    }

    override fun getAllTags(): List<String> {
        return tags
    }
}