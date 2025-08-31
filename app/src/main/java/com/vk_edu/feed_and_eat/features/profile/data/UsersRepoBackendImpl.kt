package com.vk_edu.feed_and_eat.features.profile.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import com.vk_edu.feed_and_eat.features.profile.pres.Profile
import com.vk_edu.feed_and_eat.network.api.UsersApi
import com.vk_edu.feed_and_eat.network.dto.ProfileUpdateDto
import com.vk_edu.feed_and_eat.network.dto.UserDto
import com.vk_edu.feed_and_eat.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepoBackendImpl @Inject constructor(
    private val usersApi: UsersApi,
    private val context: Context
) : UsersRepository {

    private fun makeFullUrl(relativeUrl: String?): String? {
        if (relativeUrl == null) return null
        return if (relativeUrl.startsWith("http")) {
            relativeUrl
        } else {
            BuildConfig.API_BASE_URL.trimEnd('/') + relativeUrl
        }
    }

    override fun getUserData(userId: String): Flow<Response<UserModel?>> = repoTryCatchBlock {
        val userDto = if (userId.isEmpty()) {
            usersApi.getMyProfile()
        } else {
            usersApi.getUserProfile(userId)
        }

        UserModel(
            userId = userDto.id,
            email = userDto.email,
            username = userDto.username,
            avatarUrl = makeFullUrl(userDto.avatarUrl),
            aboutMeData = userDto.aboutMe,
            collectionsIdList = emptyList(), // TODO: получать коллекции отдельно
            isProfilePrivate = userDto.isProfilePrivate,
            themeSettings = userDto.themeSettings
        )
    }.flowOn(Dispatchers.IO)

    override fun getUserCollections(userId: String): Flow<Response<List<CollectionDataModel>?>> = repoTryCatchBlock {
        // TODO: реализовать после миграции коллекций
        emptyList<CollectionDataModel>()
    }.flowOn(Dispatchers.IO)

    override fun saveUserData(userId: String, userData: UserModel): Flow<Response<UserDto>> = repoTryCatchBlock {
        val updateDto = ProfileUpdateDto(
            avatarUrl = userData.avatarUrl,
            aboutMe = userData.aboutMeData,
            isProfilePrivate = userData.isProfilePrivate,
            themeSettings = userData.themeSettings
        )
        usersApi.updateMyProfile(updateDto)
    }.flowOn(Dispatchers.IO)

    override fun updateUserData(userId: String, userData: Profile, imagePath: Uri?): Flow<Response<UserDto>> = repoTryCatchBlock {
        var resultUserDto: UserDto
        
        // Сначала загружаем аватар, если есть
        if (imagePath != null) {
            val inputStream = context.contentResolver.openInputStream(imagePath)
                ?: throw IllegalArgumentException("Cannot open input stream for URI")

            val tempFile = File.createTempFile("avatar", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            inputStream.close()
            
            val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("file", tempFile.name, requestBody)

            usersApi.uploadAvatar(multipartBody)

            tempFile.delete()
        }

        val updateDto = ProfileUpdateDto(
            aboutMe = userData.aboutMe,
            isProfilePrivate = userData.isPrivate,
            themeSettings = userData.theme
        )
        resultUserDto = usersApi.updateMyProfile(updateDto)

        return@repoTryCatchBlock resultUserDto
    }.flowOn(Dispatchers.IO)

    @Suppress("UNCHECKED_CAST")
    override fun addNewUserCollection(userId: String, collection: CollectionDataModel): Flow<Response<Void>> = repoTryCatchBlock {
        // TODO: реализовать после миграции коллекций
        Unit
    }.flowOn(Dispatchers.IO) as Flow<Response<Void>>
}
