package com.vk_edu.feed_and_eat.screenshot.main

import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.collection.pres.AddDishCard
import com.vk_edu.feed_and_eat.features.collection.pres.WindowDialog
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.main.pres.CardsRow
import com.vk_edu.feed_and_eat.features.main.pres.LargeCard
import com.vk_edu.feed_and_eat.features.main.pres.SearchCard
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest
import org.junit.Test

class HomeScreenshotTest : BaseScreenshotTest() {

    // region SearchCard

    @Test
    fun searchCard() {
        snapshot("search_card") {
            SearchCard(navigateToRoute = {})
        }
    }

    // endregion

    // region LargeCard

    @Test
    fun largeCard_notInFavourites() {
        snapshot("large_card_not_favourite") {
            LargeCard(
                cardData = RecipeCard(
                    recipeId = "1",
                    name = "Паста карбонара",
                    image = "",
                    ingredients = 5,
                    steps = 4,
                    rating = 4.7,
                    cooked = 120,
                ),
                inFavourites = false,
                favouritesCollectionId = null,
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                navigateToRoute = {},
            )
        }
    }

    @Test
    fun largeCard_inFavourites() {
        snapshot("large_card_in_favourite") {
            LargeCard(
                cardData = RecipeCard(
                    recipeId = "2",
                    name = "Борщ классический",
                    image = "",
                    ingredients = 12,
                    steps = 8,
                    rating = 4.9,
                    cooked = 350,
                ),
                inFavourites = true,
                favouritesCollectionId = "fav-collection-id",
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                navigateToRoute = {},
            )
        }
    }

    // endregion

    // region CardsRow

    @Test
    fun cardsRow_withItems() {
        val cards = listOf(
            RecipeCard(recipeId = "1", name = "Паста", image = "", ingredients = 5, steps = 3, rating = 4.5, cooked = 100),
            RecipeCard(recipeId = "2", name = "Суп", image = "", ingredients = 8, steps = 5, rating = 4.2, cooked = 60),
            RecipeCard(recipeId = "3", name = "Салат", image = "", ingredients = 4, steps = 2, rating = 4.8, cooked = 200),
        )
        snapshot("cards_row_with_items") {
            CardsRow(
                title = "Популярные рецепты",
                cards = cards,
                favouriteRecipeIds = listOf("2"),
                favouritesCollectionId = "fav-id",
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                columnWidthDp = 360.dp,
                navigateToRoute = {},
            )
        }
    }

    @Test
    fun cardsRow_empty() {
        snapshot("cards_row_empty") {
            CardsRow(
                title = "Новинки",
                cards = emptyList(),
                favouriteRecipeIds = emptyList(),
                favouritesCollectionId = null,
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                columnWidthDp = 360.dp,
                navigateToRoute = {},
            )
        }
    }

    // endregion

    // region AddDishCard (коллекции)

    @Test
    fun addDishCard_withCollection() {
        snapshot("add_dish_card_collection") {
            AddDishCard(
                compilation = CollectionDataModel(
                    id = "col-1",
                    name = "Избранное",
                    picture = null,
                ),
                navigateToCollection = {},
            )
        }
    }

    @Test
    fun addDishCard_newButton() {
        snapshot("add_dish_card_new_button") {
            AddDishCard(onAddClick = {})
        }
    }

    // endregion

    // region WindowDialog

    @Test
    fun windowDialog_opened() {
        snapshot("window_dialog_opened") {
            WindowDialog(
                isOpen = true,
                onDismiss = {},
                onConfirm = {},
            )
        }
    }

    // endregion
}