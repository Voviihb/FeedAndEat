package com.vk_edu.feed_and_eat.features.profile.data

import com.google.firebase.firestore.FirebaseFirestore
import com.vk_edu.feed_and_eat.features.collection.domain.models.Compilation
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepoImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UsersRepository {
    override fun getUserData(userId: String): Flow<Response<UserModel>> {
        TODO("Not yet implemented")
    }

    override fun getUserCollections(userId: String): Flow<Response<List<Compilation>>> {
        TODO("Not yet implemented")
    }

    override fun saveUserData(userData: HashMap<String, Any>): Flow<Response<Void>> {
        TODO("Not yet implemented")
    }

    override fun saveUserCollection(userId: String, collection: Compilation): Flow<Response<Void>> {
        TODO("Not yet implemented")
    }

    override fun addToUserCollection(userId: String, collectionName: String, recipe: Recipe) {
        TODO("Not yet implemented")
    }
}