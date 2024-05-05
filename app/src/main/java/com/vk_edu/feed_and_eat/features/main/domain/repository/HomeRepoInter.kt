package com.vk_edu.feed_and_eat.features.main.domain.repository

import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard

interface HomeRepoInter {

    suspend fun getLargeCardData(): RecipeCard

    suspend fun getCardsDataOfRow1(): List<RecipeCard>

    suspend fun getCardsDataOfRow2(): List<RecipeCard>

    suspend fun getCardsDataOfRow3(): List<RecipeCard>
}

