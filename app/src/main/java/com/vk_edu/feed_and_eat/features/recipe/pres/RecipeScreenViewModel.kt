package com.vk_edu.feed_and_eat.features.recipe.pres

import androidx.compose.runtime.Composable
import com.vk_edu.feed_and_eat.features.recipe.data.RecipeRepo

@Composable
fun RecipeScreenViewModel(){
    val repo = RecipeRepo()
    val data = repo.getRecipe()

    RecipePres(
        picture = data.picture,
        rating = data.rating,
        cooked = data.cooked,
        description = data.description,
        name = data.name,
        ingredients = data.ingredients,
        steps = data.steps,
        tags = data.tags,
        energyData = data.energyData,
        pictureHeight = data.pictureHeight
    )


}