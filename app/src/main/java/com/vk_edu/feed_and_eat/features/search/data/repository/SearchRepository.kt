package com.vk_edu.feed_and_eat.features.search.data.repository

import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.search.domain.repository.SearchRepoInter
import javax.inject.Inject

class SearchRepository @Inject constructor() : SearchRepoInter {
    private val cards = MutableList(20) {
        CardDataModel(
            link = "https://img.spoonacular.com/recipes/641732-556x370.jpg",
            ingredients = 15,
            steps = 10,
            name = "Dulce De Leche Swi Amaretto Frozen Yogurt",
            rating = 3.0,
            cooked = 156
        )
    }

    private var cnt = 0

    override suspend fun getCardsData(
        request: String,
        sort: String,
        filters: HashMap<String, List<String?>>,
        page: Int,
        limit: Int
    ): List<CardDataModel> {
        cards[0].ingredients = cnt
        cnt += 1
        return cards
    }
}