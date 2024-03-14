package com.vk_edu.feed_and_eat.features.recipe.data

import androidx.compose.runtime.Composable
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.recipe.pres.RecipePres

class RecipeRepo {
    val description : List<String>
    val ingredients : List<String>
    val steps : List<String>
    @Composable
    fun RecipeData(){
        RecipePres(
            Picture = R.drawable.recipe,
            Cooked = 1234,
            PictureHeight = 300,
            Rating = 3.3,
            Description = description,
            InFavor = false,
            Name = "Бараньи ребрышки по-узбекски",
            Steps = steps,
            Ingredients = ingredients,
        )
    }

    init {
        description = listOf("Рецепт рецепт рецепт рецепт рецепт рецепт рецепт рецепт",
            "Рецепт рецепт рецепт",
            "Рецепт рецепт рецепт рецепт рецепт рецепт рецепт",
            "Рецепт рецепт рецепт рецепт рецепт рецепт рецепт рецепт")
        ingredients = listOf("ingred 1", "ingred 2")
        steps = listOf("step 1", "step 2")
    }
}