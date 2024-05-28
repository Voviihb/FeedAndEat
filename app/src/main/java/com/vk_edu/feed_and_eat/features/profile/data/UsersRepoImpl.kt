package com.vk_edu.feed_and_eat.features.profile.data

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import com.vk_edu.feed_and_eat.features.profile.pres.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepoImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UsersRepository {
    /**
     * Loads all additional user data
     * */
    override fun getUserData(userId: String): Flow<Response<UserModel?>> = repoTryCatchBlock {
        val document = db.collection(USERS_COLLECTION).document(userId).get().await()
        return@repoTryCatchBlock document.toObject<UserModel>()
    }.flowOn(Dispatchers.IO)

    /**
     * Loads all user collections
     * */
    override fun getUserCollections(userId: String): Flow<Response<List<CollectionDataModel>?>> =
        repoTryCatchBlock {
            val document = db.collection(USERS_COLLECTION).document(userId).get().await()
            val user = document.toObject<UserModel>()
            return@repoTryCatchBlock user?.collectionsIdList
        }.flowOn(Dispatchers.IO)

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
        userData: Profile,
        imagePath: Uri?
    ): Flow<Response<Void>> = repoTryCatchBlock {
        var imageUrl: String? = userData.avatar
        if (imagePath != null) {
            val imgRef = storage.getReference(USERS_AVATARS).child(userId)
            val uploadTask = imgRef.putFile(imagePath).await()
            if (uploadTask.task.isSuccessful) {
                imageUrl = imgRef.downloadUrl.await().toString()
            }
        }
        val data: HashMap<String, Any?> = hashMapOf(
            AVATAR_URL_VALUE to imageUrl,
            ABOUT_ME_VALUE to userData.aboutMe,
        )
        db.collection(USERS_COLLECTION).document(userId).update(data).await()
    }.flowOn(Dispatchers.IO)

    /**
     * Is used to create new collection for user
     * */
    override fun addNewUserCollection(
        userId: String,
        collection: CollectionDataModel
    ): Flow<Response<Void>> =
        repoTryCatchBlock {
            db.collection(USERS_COLLECTION).document(userId)
                .update(COLLECTIONS_ID_LIST_FIELD, FieldValue.arrayUnion(collection)).await()
        }.flowOn(Dispatchers.IO)


    companion object {
        private const val USERS_COLLECTION = "users"
        private const val COLLECTIONS_ID_LIST_FIELD = "collectionsIdList"
        private const val USERS_AVATARS = "users_avatars"

        private const val AVATAR_URL_VALUE = "avatarUrl"
        private const val ABOUT_ME_VALUE = "aboutMeData"
    }
}