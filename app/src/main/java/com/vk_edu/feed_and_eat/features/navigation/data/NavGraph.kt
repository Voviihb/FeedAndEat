package com.vk_edu.feed_and_eat.features.navigation.data

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.collection.pres.CollectionScreen
import com.vk_edu.feed_and_eat.features.inprogress.pres.InProgressScreen
import com.vk_edu.feed_and_eat.features.login.pres.LoginScreen
import com.vk_edu.feed_and_eat.features.login.pres.RegisterScreen
import com.vk_edu.feed_and_eat.features.main.pres.HomeScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.NavBarViewModel
import com.vk_edu.feed_and_eat.features.navigation.pres.Screen
import com.vk_edu.feed_and_eat.features.profile.pres.ProfileScreen
import com.vk_edu.feed_and_eat.features.recipe.pres.preview.RecipeScreen
import com.vk_edu.feed_and_eat.features.search.pres.SearchScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    context: Context,
    viewModel: NavBarViewModel = hiltViewModel()
) {
    val navId = stringResource(id = R.string.nav_id)
    val navNumber = stringResource(id = R.string.nav_number)
    val recipe = stringResource(id = R.string.recipe)

    val navigateToRoute: (String) -> Unit = {route ->
        navController.navigate(route) {
            if (route.substring(0, 6) != recipe){
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateNoState: (String) -> Unit = {route ->
        navController.navigate(route)
    }

    val navigateBack = {
        val previous = navController.previousBackStackEntry?.destination?.route
        navController.navigate(previous ?: BottomScreen.HomeScreen.route){
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = viewModel.getStartDestination(),
        modifier = Modifier.padding(0.dp)
    ) {
        composable(BottomScreen.HomeScreen.route) {
            viewModel.changeBottomDestination(BottomScreen.HomeScreen.route)
            HomeScreen(
                navigateToRoute,
                navigateNoState)
        }
        composable(BottomScreen.SearchScreen.route) {
            viewModel.changeBottomDestination(BottomScreen.SearchScreen.route)
            SearchScreen(
                navigateToRoute,
                navigateNoState
            )
        }
        composable(BottomScreen.CollectionOverviewScreen.route) {
            viewModel.changeBottomDestination(BottomScreen.CollectionOverviewScreen.route)
            CollectionScreen(
                navigateToRoute = navigateToRoute,
                navigateBack = navigateBack,
                navigateNoState = navigateNoState,
            )
        }
        composable(BottomScreen.InProgressScreen.route) {
            viewModel.changeBottomDestination(BottomScreen.InProgressScreen.route)
            InProgressScreen(
                navigateToRoute,
                navigateNoState
            )
        }
        composable(BottomScreen.ProfileScreen.route) {
            viewModel.changeBottomDestination(BottomScreen.ProfileScreen.route)
            ProfileScreen(
                navigateToRoute,
                navigateNoState
            )
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
        composable(
            route = Screen.RecipeScreen.route + Screen.Id.route + Screen.Number.route,
            arguments = listOf(
                navArgument(navId){ type = NavType.StringType},
                navArgument(navNumber){ type = NavType.IntType }
            )
        ){entry ->
            val id = entry.arguments?.getString(navId)
            val number = entry.arguments?.getInt(navNumber)
            val destination = navController.previousBackStackEntry?.destination?.route ?: BottomScreen.HomeScreen.route
            RecipeScreen(
                navigateToRoute = navigateToRoute,
                navigateBack = navigateBack,
                navigateNoState = navigateNoState,
                id = id ?: "",
                number = number ?: 0,
                destination = destination
            )
        }
        composable(
            route = Screen.RecipeScreen.route + Screen.Id.route,
            arguments = listOf(navArgument(navId){ type = NavType.StringType })
        ) {entry ->
            val id = entry.arguments?.getString(navId)
            val destination = navController.previousBackStackEntry?.destination?.route ?: BottomScreen.HomeScreen.route
            RecipeScreen(
                navigateToRoute = navigateToRoute,
                navigateBack = navigateBack,
                navigateNoState = navigateNoState,
                id = id ?: "",
                destination = destination
            )
        }
    }
}