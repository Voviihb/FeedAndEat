package com.vk_edu.feed_and_eat.features.inprogress

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
fun InProgressScreen(
    navigateToRoute : (String) -> Unit,
) {
    val id = "3HJYVJkuAmC8GbW97Oot"
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.InProgressScreen.route) }
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ){
                Text(stringResource(id = R.string.inProgressScreen))
                Button(onClick = {navigateToRoute(Screen.RecipeScreen.route + "/$id")}) {
                    Text(text = "Load Recipe")
                }
            }
        }
    }
}