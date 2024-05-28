package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoldText
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.MediumIcon
import com.vk_edu.feed_and_eat.common.graphics.RepeatButton
import com.vk_edu.feed_and_eat.common.graphics.SquareArrowButton
import com.vk_edu.feed_and_eat.features.navigation.pres.Screen
import com.vk_edu.feed_and_eat.ui.theme.LargeText


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

    val localDensity = LocalDensity.current

    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }

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
                navigateToRoute = navigateToRoute,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                    }
            )
        }
        item {
            AddDishCard(navigateToRoute = navigateToRoute)
        }
    }
}

@Composable
fun AddDishCard(
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            colorResource(R.color.white), colorResource(R.color.white),
            colorResource(R.color.white), colorResource(R.color.white)
        ),
        modifier = modifier
            .fillMaxHeight()
            .shadow(12.dp, RoundedCornerShape(16.dp)),
        onClick = {
            navigateToRoute(Screen.NewRecipeScreen.route)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            MediumIcon(
                painter = painterResource(id = R.drawable.plus),
                color = colorResource(id = R.color.dark_cyan),
                modifier = Modifier
                    .aspectRatio(4f / 3f)
                    .padding(24.dp)
            )
            BoldText(
                text = stringResource(id = R.string.new_recipe),
                fontSize = LargeText,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}