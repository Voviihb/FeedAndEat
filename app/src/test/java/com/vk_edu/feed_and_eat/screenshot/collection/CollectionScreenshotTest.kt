package com.vk_edu.feed_and_eat.screenshot.collection

import com.vk_edu.feed_and_eat.features.collection.pres.CardsGridContent
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest
import org.junit.Test

class CollectionScreenshotTest : BaseScreenshotTest() {

    private val sampleCards = listOf(
        RecipeCard(
            recipeId = "1",
            name = "Паста карбонара",
            image = "",
            ingredients = 5,
            steps = 4,
            rating = 4.7,
            cooked = 120,
        ),
        RecipeCard(
            recipeId = "2",
            name = "Борщ классический",
            image = "",
            ingredients = 12,
            steps = 8,
            rating = 4.9,
            cooked = 350,
        ),
        RecipeCard(
            recipeId = "3",
            name = "Салат Цезарь",
            image = "",
            ingredients = 7,
            steps = 3,
            rating = 4.3,
            cooked = 90,
        ),
    )

    @Test
    fun cardsGrid_withItems_noFavourites() {
        snapshot("cards_grid_with_items_no_fav") {
            CardsGridContent(
                cards = sampleCards,
                favouriteRecipeIds = emptyList(),
                favouritesCollectionId = null,
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                collectionId = "col-1",
                navigateToCollection = {},
            )
        }
    }

    @Test
    fun cardsGrid_withItems_someFavourites() {
        snapshot("cards_grid_with_items_some_fav") {
            CardsGridContent(
                cards = sampleCards,
                favouriteRecipeIds = listOf("1", "3"),
                favouritesCollectionId = "fav-id",
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                collectionId = "col-1",
                navigateToCollection = {},
            )
        }
    }

    @Test
    fun cardsGrid_empty() {
        snapshot("cards_grid_empty") {
            CardsGridContent(
                cards = emptyList(),
                favouriteRecipeIds = emptyList(),
                favouritesCollectionId = null,
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                collectionId = "col-1",
                navigateToCollection = {},
            )
        }
    }
}