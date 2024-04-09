package com.vk_edu.feed_and_eat.features.newrecipe.pres

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar


@Composable
fun NewRecipeScreen(
    navigateToRoute : (String) -> Unit
) {
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.SearchScreen.route) }
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Text(stringResource(id = R.string.NewRecipeScreen))
        }
    }
}