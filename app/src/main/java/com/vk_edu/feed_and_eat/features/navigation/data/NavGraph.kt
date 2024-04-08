package com.vk_edu.feed_and_eat.features.navigation.data

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vk_edu.feed_and_eat.features.collection.pres.CollectionScreen
import com.vk_edu.feed_and_eat.features.inprogress.InProgressScreen
import com.vk_edu.feed_and_eat.features.login.pres.LoginScreen
import com.vk_edu.feed_and_eat.features.login.pres.RegisterScreen
import com.vk_edu.feed_and_eat.features.main.pres.HomeScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.NavBarViewModel
import com.vk_edu.feed_and_eat.features.navigation.pres.Screen
import com.vk_edu.feed_and_eat.features.newrecipe.pres.NewRecipeScreen
import com.vk_edu.feed_and_eat.features.profile.pres.ProfileScreen
import com.vk_edu.feed_and_eat.features.dishes.pres.RecipeScreen
import com.vk_edu.feed_and_eat.features.search.pres.SearchScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    context: Context,
    viewModel: NavBarViewModel = hiltViewModel()
) {
    val navigateToRoute: (String) -> Unit = {
        navController.navigate(it) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateBack: () -> Unit = {
        val previous = navController.previousBackStackEntry?.destination?.route
        if (previous != null) {
            navController.navigate(previous)
        }
    }

    NavHost(
        navController = navController,
        startDestination = viewModel.getStartDestination(),
        modifier = Modifier.padding(0.dp)
    ) {
        composable(BottomScreen.HomeScreen.route) {
            HomeScreen(navigateToRoute)
        }
        composable(BottomScreen.SearchScreen.route) {
            SearchScreen(navigateToRoute)
        }
        composable(BottomScreen.CollectionScreen.route) {
            CollectionScreen(navigateToRoute)
        }
        composable(BottomScreen.InProgressScreen.route) {
            InProgressScreen(navigateToRoute)
        }
        composable(BottomScreen.ProfileScreen.route) {
            ProfileScreen(navigateToRoute)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                context,
                navigateToRoute,
            )
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(
                navigateToRoute
            )
        }
        composable(Screen.NewRecipeScreen.route) {
            NewRecipeScreen(navigateToRoute)
        }
        composable(Screen.RecipeScreen.route) {
            RecipeScreen(
                navigateToRoute,
                navigateBack
            )
        }
    }
}