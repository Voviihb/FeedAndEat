package com.vk_edu.feed_and_eat.features.new_recipe.data

import android.content.Context
import android.net.Uri
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Nutrients
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Servings
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.new_recipe.repository.NewRecipeRepository
import com.vk_edu.feed_and_eat.features.network.dto.CreateRecipeDto
import com.vk_edu.feed_and_eat.network.api.RecipesApi
import com.vk_edu.feed_and_eat.network.dto.IngredientDto
import com.vk_edu.feed_and_eat.network.dto.InstructionDto
import com.vk_edu.feed_and_eat.network.dto.NutrientsDto
import com.vk_edu.feed_and_eat.network.dto.ServingsDto
import com.vk_edu.feed_and_eat.network.dto.TimerDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewRecipeRepoBackendImpl @Inject constructor(
    private val recipesApi: RecipesApi,
    private val context: Context
) : NewRecipeRepository {

    override fun addNewRecipe(
        user: String,
        name: String,
        imagePath: Uri?,
        instructions: List<Instruction>,
        tags: List<String>?,
        nutrients: Nutrients?,
        servings: Servings?
    ): Flow<Response<String?>> = repoTryCatchBlock {

        val instructionsDto = instructions.map { instruction ->
            InstructionDto(
                paragraph = instruction.paragraph,
                timers = instruction.timers?.map { timer ->
                    TimerDto(
                        type = timer.type,
                        lowerLimit = timer.lowerLimit,
                        upperLimit = timer.upperLimit,
                        number = timer.number,
                        id = timer.id
                    )
                }
            )
        }
        
        val nutrientsDto = nutrients?.let {
            NutrientsDto(
                calories = it.calories,
                sugar = it.sugar,
                protein = it.protein,
                fat = it.fat,
                carbohydrates = it.carbohydrates
            )
        }
        
        val servingsDto = servings?.let {
            ServingsDto(
                amount = it.amount,
                weight = it.weight
            )
        }
        
        val createRecipeDto = CreateRecipeDto(
            name = name,
            image_url = null,
            instructions = instructionsDto,
            servings = servingsDto,
            ingredients = null,
            tags = tags,
            nutrients = nutrientsDto
        )

        val createdRecipe = recipesApi.createRecipe(createRecipeDto)
        val recipeId = createdRecipe.id

        if (imagePath != null) {
            try {
                val file = uriToFile(imagePath)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                recipesApi.uploadRecipeImage(recipeId, body)

                file.delete()
            } catch (e: Exception) {
                android.util.Log.w("NewRecipeRepo", "Failed to upload image: ${e.message}")
            }
        }
        
        recipeId
    }.flowOn(Dispatchers.IO)
    
    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream for URI: $uri")
        
        val file = File(context.cacheDir, "temp_recipe_image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        
        return file
    }
}