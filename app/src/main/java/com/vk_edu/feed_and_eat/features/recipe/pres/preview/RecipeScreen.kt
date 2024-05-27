package com.vk_edu.feed_and_eat.features.recipe.pres.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.RepeatButton
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.features.recipe.domain.RecipeNavGraph


@Composable
fun RecipeScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack: () -> Unit,
    navigateNoState: (String) -> Unit,
    id: String,
    destination: String,
    viewModel: RecipesScreenViewModel = hiltViewModel()
) {
    viewModel.loadRecipeById(id)
    if (viewModel.isUserAuthenticated()){
        viewModel.loadCollections()
    }

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, navigateNoState, destination) },
    ) { padding ->
        if (viewModel.loading.value) {
            LoadingCircular()
        } else {
            if (viewModel.errorMessage.value != null) {
                RepeatButton(onClick = {
                    viewModel.clearError()
                    viewModel.clearCollectionError()
                    viewModel.loadRecipeById(id)
                    viewModel.loadCollections()
                })
            } else {
                Box(modifier = Modifier.padding(padding)) {
                    val navController = rememberNavController()
                    RecipeNavGraph(
                        navigateToRoute = navigateToRoute,
                        navigateBack = navigateBack,
                        navController = navController,
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack: () -> Unit,
    navigateNoState: (String) -> Unit,
    id: String,
    destination: String,
    number : Int,
    viewModel: RecipesScreenViewModel = hiltViewModel()
) {
    viewModel.loadRecipeById(id)
    if (viewModel.isUserAuthenticated()){
        viewModel.loadCollections()
    }

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, navigateNoState, destination) },
    ) { padding ->
        if (viewModel.loading.value) {
            LoadingCircular()
        } else {
            if (viewModel.errorMessage.value != null) {
                RepeatButton(onClick = {
                    viewModel.clearError()
                    viewModel.clearCollectionError()
                    viewModel.loadRecipeById(id)
                    viewModel.loadCollections()
                })
            } else {
                Box(modifier = Modifier.padding(padding)) {
                    val navController = rememberNavController()
                    RecipeNavGraph(
                        navigateToRoute = navigateToRoute,
                        navigateBack = navigateBack,
                        navController = navController,
                        step = number,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}