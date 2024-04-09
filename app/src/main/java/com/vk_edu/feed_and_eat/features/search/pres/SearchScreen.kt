package com.vk_edu.feed_and_eat.features.search.pres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.features.navigation.pres.Screen


@Composable
fun SearchScreen(
    navigateToRoute : (String) -> Unit,
) {
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.SearchScreen.route) }
    ) {padding ->
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Text(stringResource(id = R.string.SearchScreen))
        Button(
            onClick = { navigateToRoute(Screen.NewRecipeScreen.route) }
        ){
            Text("New Recipe")
        }
        Button(
            onClick = { navigateToRoute(Screen.RecipeScreen.route) }
        ){
            Text("To Single Recipe")
        }
    }
    }
}