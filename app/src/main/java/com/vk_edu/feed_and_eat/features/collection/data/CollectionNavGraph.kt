package com.vk_edu.feed_and_eat.features.collection.data

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
import com.vk_edu.feed_and_eat.features.collection.pres.AllCollectionsScreen
import com.vk_edu.feed_and_eat.features.collection.pres.CollectionPreview
import com.vk_edu.feed_and_eat.features.collection.pres.CollectionRoutes
import com.vk_edu.feed_and_eat.features.collection.pres.CollectionScreenViewModel

@Composable
fun CollectionNavGraph(
    navigateToRoute: (String) -> Unit,
    navController: NavHostController,
) {
    val navId = stringResource(id = R.string.nav_id)
    val navigateToCollection: (String) -> Unit = {route ->
        navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = CollectionRoutes.AllCollections.route,
        modifier = Modifier.padding(0.dp)
    ) {
        composable(CollectionRoutes.AllCollections.route){
            AllCollectionsScreen(
                navigateToCollection
            )
        }
        composable(
            route = CollectionRoutes.Collection.route + CollectionRoutes.Id.route,
            arguments = listOf(navArgument(navId){ type = NavType.StringType })
        ){entry ->
            val id = entry.arguments?.getString(navId)
            val viewModel : CollectionScreenViewModel = hiltViewModel()
            viewModel.collectionRecipes(id ?: "")
            CollectionPreview(
                navigateToRoute = navigateToRoute,
                navigateToCollection = navigateToCollection,
                id = id ?: "",
                viewModel
            )
        }
    }
}