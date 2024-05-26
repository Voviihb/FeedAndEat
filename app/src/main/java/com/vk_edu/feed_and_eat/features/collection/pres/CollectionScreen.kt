package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.vk_edu.feed_and_eat.features.collection.data.CollectionNavGraph
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar

@Composable
fun CollectionScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.CollectionOverviewScreen.route) },
    ) { padding ->
        val navController = rememberNavController()
        Box(modifier = Modifier.padding(padding)){
            CollectionNavGraph(
                navigateToRoute = navigateToRoute,
                navigateBack = navigateBack,
                destination = BottomScreen.CollectionOverviewScreen.route,
                navController = navController
            )
        }
    }
}