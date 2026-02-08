package com.vk_edu.feed_and_eat.features.search.pres

import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type

data class PagePointer(
    val type: Type?,
    val number: Int,
    val offset: Int = 0
)