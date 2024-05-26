package com.vk_edu.feed_and_eat.features.profile.domain.repository

import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUserData(userId: String): Flow<Response<UserModel?>>

    fun getUserCollections(userId: String): Flow<Response<List<CollectionDataModel>?>>

    fun saveUserData(userId: String, userData: UserModel): Flow<Response<Void>>

    fun updateUserData(userId: String, userData: HashMap<String, Any?>): Flow<Response<Void>>

    fun addNewUserCollection(userId: String, collection: CollectionDataModel): Flow<Response<Void>>

}