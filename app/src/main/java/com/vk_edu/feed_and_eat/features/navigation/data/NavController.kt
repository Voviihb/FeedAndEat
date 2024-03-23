package com.vk_edu.feed_and_eat.features.navigation.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.collection.pres.CollectionScreen
import com.vk_edu.feed_and_eat.features.inprogress.InProgressScreen
import com.vk_edu.feed_and_eat.features.login.pres.LoginScreen
import com.vk_edu.feed_and_eat.features.login.pres.RegisterScreen
import com.vk_edu.feed_and_eat.features.main.pres.HomeScreen
import com.vk_edu.feed_and_eat.features.newrecipe.pres.NewRecipeScreen
import com.vk_edu.feed_and_eat.features.profile.pres.ProfileScreen
import com.vk_edu.feed_and_eat.features.recipe.pres.RecipeScreen
import com.vk_edu.feed_and_eat.features.search.pres.SearchScreen

@Composable
fun NavController(
    navController: NavHostController,
    context: Context,
) {
    val destinations = listOf(
        stringResource(R.string.HomeScreen),
        stringResource(R.string.SearchScreen),
        stringResource(R.string.CollectionScreen),
        stringResource(R.string.inProgressScreen),
        stringResource(R.string.ProfileScreen),
        stringResource(R.string.LoginScreen),
        stringResource(R.string.RegisterScreen),
        stringResource(R.string.NewRecipeScreen),
        stringResource(R.string.RecipeScreen),
    )
    NavHost(navController = navController, startDestination = stringResource(R.string.HomeScreen)){
        composable(destinations[0]){
            HomeScreen(navController)
        }
        composable(destinations[1]){
            SearchScreen(navController)
        }
        composable(destinations[2]){
            CollectionScreen(navController)
        }
        composable(destinations[3]){
            InProgressScreen(navController)
        }
        composable(destinations[4]){
            ProfileScreen(navController)
        }
        composable(destinations[5]){
            LoginScreen(context, navController)
        }
        composable(destinations[6]){
            RegisterScreen(context, navController)
        }
        composable(destinations[7]){
            NewRecipeScreen(navController)
        }
        composable(destinations[8]){
            RecipeScreen(navController)
        }
    }
}