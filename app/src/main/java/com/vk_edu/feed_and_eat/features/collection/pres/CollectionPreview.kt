package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.RepeatButton
import com.vk_edu.feed_and_eat.common.graphics.SquareArrowButton


@Composable
fun CollectionPreview(
    navigateToRoute: (String) -> Unit,
    navigateToCollection: (String) -> Unit,
    id : String,
    viewModel: CollectionScreenViewModel
) {
    Box(
        modifier = Modifier
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

            SquareArrowButton(onClick = { navigateToCollection(CollectionRoutes.AllCollections.route) })
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