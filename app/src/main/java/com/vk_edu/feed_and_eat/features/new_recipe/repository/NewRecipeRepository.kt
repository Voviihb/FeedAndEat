package com.vk_edu.feed_and_eat.features.new_recipe.repository

import android.net.Uri
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Nutrients
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface NewRecipeRepository {
    fun addNewRecipe(
        user: String,
        name: String,
        imagePath: Uri?,
        instructions: List<Instruction>,
        tags: List<String>?,
        nutrients: Nutrients?
    ): Flow<Response<String?>>
}