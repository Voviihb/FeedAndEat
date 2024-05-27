package com.vk_edu.feed_and_eat.features.recipe.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.recipe.data.models.Routes
import com.vk_edu.feed_and_eat.features.recipe.pres.preview.RecipePreview
import com.vk_edu.feed_and_eat.features.recipe.pres.preview.RecipesScreenViewModel
import com.vk_edu.feed_and_eat.features.recipe.pres.step.CongratulationScreen
import com.vk_edu.feed_and_eat.features.recipe.pres.step.StepScreen

fun getStartDestination(step : Int?): String {
    return if (step == null)
        Routes.Recipe.route
    else
        Routes.StartWithStep.route
}

@Composable
fun RecipeNavGraph(
    navigateToRoute: (String) -> Unit,
    navigateBack : () -> Unit,
    navController: NavHostController,
    step: Int? = null,
    viewModel: RecipesScreenViewModel,
){
    val recipe by viewModel.recipe
    val navId = stringResource(id = R.string.nav_id)

    NavHost(
        navController = navController,
        startDestination = getStartDestination(step),
    ){
        val navigateToStep: (Int) -> Unit = {
            navController.navigate(Routes.Step.route + "/$it"){
                launchSingleTop = true
                restoreState = true
            }
        }

        val navigateToRecipe: (String) -> Unit = {
            navController.navigate(it) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        composable(Routes.Recipe.route){
            RecipePreview(
                navigateBack = navigateBack,
                navigateToStep = navigateToStep,
                viewModel = viewModel,
            )
        }

        composable(Routes.Congrats.route){
            CongratulationScreen(
                recipe,
                navigateToRoute
            )
        }

        composable(
            route = Routes.Step.route + Routes.Id.route,
            arguments = listOf(navArgument(navId){ type = NavType.IntType })
        ) {entry ->
            val id = entry.arguments?.getInt(navId)?: 0
            StepScreen(
                navigateToStep = navigateToStep,
                navigateToRecipe = navigateToRecipe,
                data = recipe.instructions[id],
                id = id,
                maxId = recipe.instructions.size - 1,
                name = recipe.name,
                recipeId = recipe.id,
                recipeImage = recipe.image
            )
        }
        composable(
            route = Routes.StartWithStep.route,
        ){
            RecipePreview(
                navigateBack = navigateBack,
                navigateToStep = navigateToStep,
                step = step,
                viewModel = viewModel,
            )
        }
    }
}
