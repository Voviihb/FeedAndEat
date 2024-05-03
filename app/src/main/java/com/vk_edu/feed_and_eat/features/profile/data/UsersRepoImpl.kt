package com.vk_edu.feed_and_eat.features.profile.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.collection.domain.models.Compilation
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepoImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UsersRepository {
    /**
     * Loads all additional user data
     * */
    override fun getUserData(userId: String): Flow<Response<UserModel?>> = repoTryCatchBlock {
        val document = db.collection(USERS_COLLECTION).document(userId).get().await()
        return@repoTryCatchBlock document.toObject<UserModel>()
    }

    /**
     * Loads all user collections
     * */
    override fun getUserCollections(userId: String): Flow<Response<List<Compilation>?>> =
        repoTryCatchBlock {
            val document = db.collection(USERS_COLLECTION).document(userId).get().await()
            val user = document.toObject<UserModel>()
            return@repoTryCatchBlock user?.collectionsIdList
        }

    /**
     * Is used after registration to create new document for user in DB
     * */
    override fun saveUserData(
        userId: String,
        userData: UserModel
    ): Flow<Response<Void>> = repoTryCatchBlock {
        db.collection(USERS_COLLECTION).document(userId).set(userData).await()
    }.flowOn(Dispatchers.IO)

    /**
     * Updates user fields passed
     * @param userData pass here fields to be updated
     * */
    override fun updateUserData(
        userId: String,
        userData: HashMap<String, Any?>
    ): Flow<Response<Void>> = repoTryCatchBlock {
        db.collection(USERS_COLLECTION).document(userId).update(userData).await()
    }

    /**
     * Is used to create new collection for user
     * */
    override fun addNewUserCollection(
        userId: String,
        collection: Compilation
    ): Flow<Response<Void>> =
        repoTryCatchBlock {
            db.collection(USERS_COLLECTION).document(userId)
                .update(COLLECTIONS_ID_LIST_FIELD, FieldValue.arrayUnion(collection)).await()
        }.flowOn(Dispatchers.IO)


    companion object {
        private const val USERS_COLLECTION = "users"
        private const val COLLECTIONS_ID_LIST_FIELD = "collectionsIdList"
    }
}