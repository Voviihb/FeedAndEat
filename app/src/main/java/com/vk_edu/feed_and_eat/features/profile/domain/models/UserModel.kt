package com.vk_edu.feed_and_eat.features.profile.domain.models

import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel

data class UserModel(
    val userId: String = "",
    val avatarUrl: String? = null,
    val aboutMeData: String? = null,
    val collectionsIdList: List<CollectionDataModel> = listOf(),
    @field:JvmField
    val isProfilePrivate: Boolean = false,
    val themeSettings: String = "light"
)
