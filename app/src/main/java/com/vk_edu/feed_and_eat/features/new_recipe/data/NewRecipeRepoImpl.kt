package com.vk_edu.feed_and_eat.features.new_recipe.data

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Ingredient
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Nutrients
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Servings
import com.vk_edu.feed_and_eat.features.dishes.domain.models.serializeNutrients
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.new_recipe.repository.NewRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.sql.Timestamp
import java.util.Date
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
        tags: List<String>?,
        nutrients: Nutrients?
    ): Flow<Response<String?>> = repoTryCatchBlock {
        val document = db.collection(RECIPES_COLLECTION).document()
        val docId = document.id
        var imageUrl: String? = null
        if (imagePath != null) {
            val imgRef = storage.getReference(RECIPES_IMAGES).child(docId)
            val uploadTask = imgRef.putFile(imagePath).await()
            if (uploadTask.task.isSuccessful) {
                imageUrl = imgRef.downloadUrl.await().toString()
            }
        }

        val nutrientsData = serializeNutrients(nutrients ?: Nutrients(10e4, 10e4, 10e4, 10e4, 10e4))
        val recipe = hashMapOf(
            "id" to null,
            "name" to name,
            "image" to imageUrl,
            "instructions" to instructions,
            "tags" to tags,
            "user" to user,
            "created" to Date(Timestamp(System.currentTimeMillis()).time),
            "nutrients" to nutrientsData,
            "servings" to Servings(amount = 1, weight = 0),
            "ingredients" to listOf<Ingredient>(),
            "author" to 0,
            "reviews" to listOf<Review>(),
            "rating" to 0.0,
            "cooked" to 0,
        )
        db.collection(RECIPES_COLLECTION).document(docId).set(recipe).await()
        return@repoTryCatchBlock docId

    }.flowOn(Dispatchers.IO)

    companion object {
        private const val RECIPES_IMAGES = "recipes_images"
        private const val RECIPES_COLLECTION = "recipes"
    }
}