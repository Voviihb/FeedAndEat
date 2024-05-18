package com.vk_edu.feed_and_eat.features.new_recipe.data

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.new_recipe.repository.NewRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewRecipeRepoImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : NewRecipeRepository {
    override fun addNewRecipe(
        user: String,
        name: String,
        imagePath: Uri?,
        instructions: List<Instruction>,
        tags: List<String>?
    ): Flow<Response<String?>> = repoTryCatchBlock {
        var imageUrl: String? = null
        if (imagePath != null) {
            val uploadTask = storage.getReference(RECIPES_IMAGES).child(name).putFile(imagePath).await()
            imageUrl = uploadTask.task.result.toString()
        }

        val recipe = Recipe(
            name = name,
            image = imageUrl,
            instructions = instructions,
            tags = tags,
            user = user
        )
        val document = db.collection(RECIPES_COLLECTION).document()
        val docId = document.id
        db.collection(RECIPES_COLLECTION).document(docId).set(recipe).await()
        return@repoTryCatchBlock docId

    }.flowOn(Dispatchers.IO)

    companion object {
        private const val RECIPES_IMAGES = "recipes_images"
        private const val RECIPES_COLLECTION = "recipes"
    }
}