package com.vk_edu.feed_and_eat.features.new_recipe.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar


@Composable
fun NewRecipeScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: NewRecipeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.CollectionScreen.route) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.pale_cyan))
        ) {

        }
    }
}
