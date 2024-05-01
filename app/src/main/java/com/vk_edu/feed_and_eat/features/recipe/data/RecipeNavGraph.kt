package com.vk_edu.feed_and_eat.features.recipe.data

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.recipe.data.models.Routes
import com.vk_edu.feed_and_eat.features.recipe.pres.preview.StartRecipe
import com.vk_edu.feed_and_eat.features.recipe.pres.step.CongratulationScreen
import com.vk_edu.feed_and_eat.features.recipe.pres.step.StepScreen


@Composable
fun RecipeNavGraph(
    navigateBack: () -> Unit,
    navController: NavHostController,
    recipe : Recipe,
){
    val instructions = recipe.instructions

    NavHost(
        navController = navController,
        startDestination = Routes.Recipe.route,
    ){
        val navigateToStep: (Int) -> Unit = {
            navController.navigate(Routes.Step.route + "/$it"){
                launchSingleTop = true
                restoreState = true
            }
        }

        val navigateToRecipe: (String) -> Unit = {
            navController.navigate(it) {
                launchSingleTop = true
                restoreState = true
            }
        }

        composable(Routes.Recipe.route){
            StartRecipe(navigateBack, navigateToStep, recipe)
        }
        composable(Routes.Congrats.route){
            CongratulationScreen(
                recipe.name,
                navigateBack
            )
        }
        composable(
            Routes.Step.route + "/{id}",
            arguments = listOf(navArgument("id"){ type = NavType.StringType })
        ) {backStackEntry->
            val id = backStackEntry.arguments?.getString("id")
            StepScreen(
                navigateToStep = navigateToStep,
                navigateToRecipe = navigateToRecipe,
                data = instructions[id?.toInt() ?: 0],
                id = id?.toInt() ?: 0,
                maxId = instructions.size - 1,
                name = recipe.name
            )
        }
    }
}