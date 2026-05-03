package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoldText
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.MediumIcon
import com.vk_edu.feed_and_eat.common.graphics.RepeatButton
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.ui.theme.ExtraLargeText
import com.vk_edu.feed_and_eat.ui.theme.LargeText

@Composable
fun HomeScreen(
    navigateToRoute: (String) -> Unit,
    navigateNoState: (String) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = {
            GlobalNavigationBar(
                navigateToRoute,
                navigateNoState,
                BottomScreen.HomeScreen.route
            )
        }
    ) { padding ->
        val loading by viewModel.loading.collectAsState()
        val errorMessage by viewModel.errorMessage.collectAsState()
        val largeCardData by viewModel.largeCardData.collectAsState()
        val favouriteRecipeIds by viewModel.favouriteRecipeIds.collectAsState()
        val favouritesCollectionId by viewModel.favouritesCollectionId.collectAsState()
        val cardsDataOfRow1 by viewModel.cardsDataOfRow1.collectAsState()
        val cardsDataOfRow2 by viewModel.cardsDataOfRow2.collectAsState()
        val cardsDataOfRow3 by viewModel.cardsDataOfRow3.collectAsState()
        val cardsDataOfRow4 by viewModel.cardsDataOfRow4.collectAsState()

        if (loading)
            Box(
                modifier = Modifier
                    .background(colorResource(R.color.pale_cyan))
                    .padding(padding)
            ) {
                SearchCard(navigateToRoute)
                LoadingCircular()
            }
        else if (errorMessage != null)
            Box(
                modifier = Modifier
                    .background(colorResource(R.color.pale_cyan))
                    .padding(padding)
            ) {
                SearchCard(navigateToRoute)
                RepeatButton(onClick = {
                    viewModel.clearError()
                    viewModel.getFavouriteRecipeIds()
                    viewModel.getLargeCardData()
                    viewModel.getCardsDataOfRow1()
                    viewModel.getCardsDataOfRow2()
                    viewModel.getCardsDataOfRow3()
                    viewModel.getCardsDataOfRow4()
                })
            }
        else
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(colorResource(R.color.pale_cyan))
                    .padding(padding)
                    .testTag("home_screen_content")
            ) {
                SearchCard(navigateToRoute)

                LargeCard(
                    cardData = largeCardData,
                    inFavourites = largeCardData.recipeId in favouriteRecipeIds,
                    favouritesCollectionId = favouritesCollectionId,
                    addToFavourites = viewModel::addRecipeToUserCollection,
                    removeFromFavourites = viewModel::removeRecipeFromUserCollection,
                    navigateToRoute = navigateToRoute,
                )

                var columnWidthDp by remember { mutableStateOf(0.dp) }
                val localDensity = LocalDensity.current
                CardsRow(
                    title = stringResource(R.string.title2),
                    cards = cardsDataOfRow1,
                    favouriteRecipeIds = favouriteRecipeIds,
                    favouritesCollectionId = favouritesCollectionId,
                    addToFavourites = viewModel::addRecipeToUserCollection,
                    removeFromFavourites = viewModel::removeRecipeFromUserCollection,
                    columnWidthDp = columnWidthDp,
                    navigateToRoute = navigateToRoute,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            columnWidthDp = with(localDensity) { coordinates.size.width.toDp() }
                        },
                )

                CardsRow(
                    title = stringResource(R.string.title3),
                    cards = cardsDataOfRow2,
                    favouriteRecipeIds = favouriteRecipeIds,
                    favouritesCollectionId = favouritesCollectionId,
                    addToFavourites = viewModel::addRecipeToUserCollection,
                    removeFromFavourites = viewModel::removeRecipeFromUserCollection,
                    columnWidthDp = columnWidthDp,
                    navigateToRoute = navigateToRoute,
                )

                CardsRow(
                    title = stringResource(R.string.title4),
                    cards = cardsDataOfRow3,
                    favouriteRecipeIds = favouriteRecipeIds,
                    favouritesCollectionId = favouritesCollectionId,
                    addToFavourites = viewModel::addRecipeToUserCollection,
                    removeFromFavourites = viewModel::removeRecipeFromUserCollection,
                    columnWidthDp = columnWidthDp,
                    navigateToRoute = navigateToRoute,
                )

                CardsRow(
                    title = stringResource(R.string.title5),
                    cards = cardsDataOfRow4,
                    favouriteRecipeIds = favouriteRecipeIds,
                    favouritesCollectionId = favouritesCollectionId,
                    addToFavourites = viewModel::addRecipeToUserCollection,
                    removeFromFavourites = viewModel::removeRecipeFromUserCollection,
                    columnWidthDp = columnWidthDp,
                    navigateToRoute = navigateToRoute,
                )

                Spacer(modifier = Modifier.size(12.dp))
            }
    }
}

@Composable
fun SearchCard(navigateToRoute: (String) -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(12.dp, 12.dp, 12.dp, 20.dp)) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardColors(
                colorResource(R.color.white), colorResource(R.color.white),
                colorResource(R.color.white), colorResource(R.color.white)
            ),
            modifier = Modifier
                .height(52.dp)
                .testTag("home_search_card")
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            onClick = {
                navigateToRoute(BottomScreen.SearchScreen.route)
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, 0.dp, 4.dp, 0.dp)
            ) {
                LightText(text = stringResource(R.string.searchLabel), fontSize = LargeText)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .background(colorResource(R.color.medium_cyan), RoundedCornerShape(22.dp))
                ) {
                    MediumIcon(
                        painter = painterResource(R.drawable.search),
                        color = colorResource(R.color.white),
                        modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
                    )
                }
            }
        }
    }
}

@Composable
fun LargeCard(
    cardData: RecipeCard,
    inFavourites: Boolean,
    favouritesCollectionId: String?,
    addToFavourites: (String, RecipeCard) -> Unit,
    removeFromFavourites: (String, RecipeCard) -> Unit,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        BoldText(text = stringResource(R.string.title1), fontSize = ExtraLargeText)
        DishCard(
            recipeCard = cardData,
            inFavourites = inFavourites,
            favouritesCollectionId = favouritesCollectionId,
            addToFavourites = addToFavourites,
            removeFromFavourites = removeFromFavourites,
            navigateToRoute = navigateToRoute,
            largeCard = true,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
    }
}

@Composable
fun CardsRow(
    title: String,
    cards: List<RecipeCard>,
    favouriteRecipeIds: List<String>,
    favouritesCollectionId: String?,
    addToFavourites: (String, RecipeCard) -> Unit,
    removeFromFavourites: (String, RecipeCard) -> Unit,
    columnWidthDp: Dp,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(0.dp, 20.dp, 0.dp, 0.dp)
    ) {
        BoldText(
            text = title,
            fontSize = ExtraLargeText,
            modifier = Modifier.padding(12.dp, 0.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(12.dp, 0.dp),
            modifier = Modifier.testTag("home_recipes_row_$title")
        ) {
            items(cards) { cardData ->
                DishCard(
                    recipeCard = cardData,
                    inFavourites = cardData.recipeId in favouriteRecipeIds,
                    favouritesCollectionId = favouritesCollectionId,
                    addToFavourites = addToFavourites,
                    removeFromFavourites = removeFromFavourites,
                    navigateToRoute = navigateToRoute,
                    modifier = Modifier.width((columnWidthDp - 44.dp) / 2)
                )
            }
        }
    }
}
