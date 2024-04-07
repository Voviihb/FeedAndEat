package com.vk_edu.feed_and_eat.features.main.domain.repository

import com.vk_edu.feed_and_eat.features.main.domain.models.CardDataModel

interface HomeRepoInter {

    suspend fun getLargeCardData(): CardDataModel

    suspend fun getCardsDataOfRow1(): List<CardDataModel>

    suspend fun getCardsDataOfRow2(): List<CardDataModel>

    suspend fun getCardsDataOfRow3(): List<CardDataModel>
}

