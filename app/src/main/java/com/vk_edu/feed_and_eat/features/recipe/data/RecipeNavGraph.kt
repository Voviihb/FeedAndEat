package com.vk_edu.feed_and_eat.features.recipe.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.recipe.data.models.Routes
import com.vk_edu.feed_and_eat.features.recipe.pres.preview.RecipePreview
import com.vk_edu.feed_and_eat.features.recipe.pres.step.CongratulationScreen
import com.vk_edu.feed_and_eat.features.recipe.pres.step.StepScreen


@Composable
fun RecipeNavGraph(
    navigateToRoute: (String) -> Unit,
    navigateBack : () -> Unit,
    navController: NavHostController,
    recipe : Recipe,
){
    val instructions = recipe.instructions
    val navId = stringResource(id = R.string.nav_id)

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
            RecipePreview(
                navigateBack,
                navigateToStep,
                recipe
            )
        }
        composable(Routes.Congrats.route){
            CongratulationScreen(
                recipe.name,
                navigateToRoute
            )
        }
        composable(
            Routes.Step.route + "/{" + navId + "}",
            arguments = listOf(navArgument(navId){ type = NavType.StringType })
        ) {backStackEntry->
            val id = backStackEntry.arguments?.getString(navId)
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