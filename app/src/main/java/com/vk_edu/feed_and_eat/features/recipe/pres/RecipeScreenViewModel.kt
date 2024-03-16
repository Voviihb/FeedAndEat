package com.vk_edu.feed_and_eat.features.recipe.pres

import androidx.compose.runtime.Composable
import com.vk_edu.feed_and_eat.features.recipe.data.RecipeRepo
import com.vk_edu.feed_and_eat.features.recipe.pres.RecipePres

@Composable
fun RecipeScreenViewModel(){
    val repo = RecipeRepo()
    val data = repo.getRecipe()

    RecipePres(
        Picture = data.picture,
        Rating = data.rating,
        Cooked = data.cooked,
        Description = data.description,
        InFavor = data.inFavor,
        Name = data.name,
        Ingredients = data.ingredients,
        Steps = data.steps,
        Tags = data.tags,
        EnergyData = data.energyData,
        PictureHeight = data.pictureHeight
    )


}