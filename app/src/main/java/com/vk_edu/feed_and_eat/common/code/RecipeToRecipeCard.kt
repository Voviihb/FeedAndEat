package com.vk_edu.feed_and_eat.common.code

import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard


fun recipeToRecipeCard(recipe : Recipe) : RecipeCard{
    return RecipeCard(
        recipeId = recipe.id ?: "",
        image = recipe.image ?: "",
        ingredients = recipe.ingredients.size,
        steps = recipe.instructions.size,
        name = recipe.name,
        rating = recipe.rating,
        cooked = recipe.cooked
    )
}