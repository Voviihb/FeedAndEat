package com.vk_edu.feed_and_eat.features.profile.domain.repository

import com.vk_edu.feed_and_eat.features.collection.domain.models.Compilation
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import com.vk_edu.feed_and_eat.features.recipe.data.models.RecipeDataModel
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUserData(userId: String): Flow<Response<UserModel>>

    fun getUserCollections(userId: String): Flow<Response<List<Compilation>>>

    fun saveUserData(userId: String, userData: UserModel): Flow<Response<Void>>

    fun addNewUserCollection(userId: String, collection: Compilation): Flow<Response<Void>>

    fun addToUserCollection(
        userId: String,
        collectionName: String,
        recipe: RecipeDataModel /* TODO replace model type*/
    ): Flow<Response<Void>>
}