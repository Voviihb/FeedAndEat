package com.vk_edu.feed_and_eat.features.search.domain.repository

import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel

interface SearchRepoInter {
    suspend fun getCardsData(
        request: String,
        sort: String,
        filters: HashMap<String, List<String?>>,
        page: Int,
        limit: Int = 20
    ): List<CardDataModel>
}

