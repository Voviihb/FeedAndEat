package com.vk_edu.feed_and_eat.features.recipe.data

import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.recipe.domain.RecipeForm

class RecipeRepo {
    fun getRecipe(): RecipeForm {
        val description = listOf(
            "Рецепт рецепт рецепт рецепт рецепт рецепт рецепт рецепт",
            "Рецепт рецепт рецепт",
            "Рецепт рецепт рецепт рецепт рецепт рецепт рецепт",
            "Рецепт рецепт рецепт рецепт рецепт рецепт рецепт рецепт"
        )
        val ingredients = listOf(
            "ingred 1 hwerfhsdjknwausfvnhzxkajshpvnsjdz", "ingred 2",
            "ingred 1", "ingred 2",
            "ingred 1", "ingred 2",
            "ingred 1", "ingred 2",
            "ingred 1", "ingred 2",
            "ingred 1", "ingred 2",
            "ingred 1", "ingred 2",
            "ingred 1", "ingred 2",
        )
        val steps = listOf(
            "step 1", "step 2",
            "step 1", "step 2",
            "step 1", "step 2",
            "step 1", "step 2",
            "step 1", "step 2",
            "step 1", "step 2",
            "step 1", "step 2",
            "step 1", "step 2",
        )
        val energyData = listOf(38, 38, 38, 38)
        val tags = listOf(
            "tag1", "tag2",
            "tag1", "tag2",
            "tag1", "tag2",
            "tag1", "tag2",
            "tag1", "tag2",
            "tag1", "tag2",
        )

        return RecipeForm(
            picture = R.drawable.recipe,
            cooked = 1234,
            pictureHeight = 300,
            rating = 3.3,
            description = description,
            name = "Бараньи ребрышки по-узбекски",
            steps = steps,
            ingredients = ingredients,
            energyData = energyData,
            tags = tags,
        )
    }
}