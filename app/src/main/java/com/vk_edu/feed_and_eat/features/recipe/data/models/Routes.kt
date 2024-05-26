package com.vk_edu.feed_and_eat.features.recipe.data.models

sealed class Routes(val route : String) {
    data object Recipe: Routes("Recipe")
    data object Step: Routes("Step")
    data object Congrats: Routes("Congrats")
    data object Id : Routes("/{id}")
    data object StartWithStep : Routes("StartWithStep")
}