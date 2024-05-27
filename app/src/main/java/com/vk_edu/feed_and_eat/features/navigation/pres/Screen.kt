package com.vk_edu.feed_and_eat.features.navigation.pres

sealed class Screen(val route: String) {
    data object LoginScreen : Screen("LoginScreen")
    data object RegisterScreen : Screen("RegisterScreen")
    data object NewRecipeScreen : Screen("NewRecipeScreen")
    data object RecipeScreen : Screen("RecipeScreen")
    data object CollectionScreen : Screen("CollectionScreen")
    data object Id : Screen("/{id}")
    data object Number : Screen("/{number}")
}
