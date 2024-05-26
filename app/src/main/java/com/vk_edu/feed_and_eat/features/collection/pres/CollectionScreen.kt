package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.ActivityNavigator
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.RepeatButton
import com.vk_edu.feed_and_eat.common.graphics.SquareArrowButton
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar


@Composable
fun CollectionScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack: () -> Unit,
    id : String,
    destination: String,
    viewModel: CollectionScreenViewModel = hiltViewModel()
) {
    viewModel.collectionRecipes(id)

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, destination) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.pale_cyan))
        ) {
            if (viewModel.loading.value)
                LoadingCircular()
            else if (viewModel.errorMessage.value != null)
                RepeatButton(onClick = {
                    viewModel.clearError()
                    viewModel.collectionRecipes(id)
                })
            else
                CardsGrid(viewModel = viewModel, navigateToRoute)

            SquareArrowButton(onClick = navigateBack)
        }
    }
}

@Composable
fun CardsGrid(
    viewModel: CollectionScreenViewModel,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userFavourites by viewModel.favouriteRecipeIds
    val favouritesId by viewModel.favouritesCollectionId

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(viewModel.cardsData.value) { cardData ->
            DishCard(
                recipeCard = cardData,
                inFavourites = cardData.recipeId in userFavourites,
                favouritesCollectionId = favouritesId,
                addToFavourites = viewModel::addRecipeToUserCollection,
                removeFromFavourites = viewModel::removeRecipeFromUserCollection,
                navigateToRoute = navigateToRoute
            )
        }
    }
}