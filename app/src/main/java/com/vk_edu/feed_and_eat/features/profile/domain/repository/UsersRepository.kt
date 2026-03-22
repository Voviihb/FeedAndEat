package com.vk_edu.feed_and_eat.features.profile.domain.repository

import android.net.Uri
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import com.vk_edu.feed_and_eat.features.profile.pres.Profile
import com.vk_edu.feed_and_eat.network.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUserData(userId: String): Flow<Response<UserModel?>>

    fun getUserCollections(): Flow<Response<List<CollectionDataModel>?>>

    fun saveUserData(userId: String, userData: UserModel): Flow<Response<UserDto>>

    fun updateUserData(userData: Profile, imagePath: Uri?): Flow<Response<UserDto>>

    fun addNewUserCollection(collection: CollectionDataModel): Flow<Response<Void>>

}