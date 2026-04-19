package com.vk_edu.feed_and_eat.screenshot.recipe

import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest
import org.junit.Test

class DishCardScreenshotTest : BaseScreenshotTest() {

    private val sampleCard = RecipeCard(
        recipeId = "1",
        name = "Паста карбонара",
        image = "",
        ingredients = 5,
        steps = 4,
        rating = 4.7,
        cooked = 120,
    )

    private val longNameCard = RecipeCard(
        recipeId = "2",
        name = "Очень длинное название блюда которое не влезает в одну строку",
        image = "",
        ingredients = 12,
        steps = 8,
        rating = 3.2,
        cooked = 7,
    )

    // region Small card (largeCard = false, default)

    @Test
    fun dishCard_small_notInFavourites() {
        snapshot("dish_card_small_not_fav") {
            DishCard(
                recipeCard = sampleCard,
                inFavourites = false,
                favouritesCollectionId = "fav-id",
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                navigateToRoute = {},
            )
        }
    }

    @Test
    fun dishCard_small_inFavourites() {
        snapshot("dish_card_small_in_fav") {
            DishCard(
                recipeCard = sampleCard,
                inFavourites = true,
                favouritesCollectionId = "fav-id",
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                navigateToRoute = {},
            )
        }
    }

    @Test
    fun dishCard_small_longName() {
        snapshot("dish_card_small_long_name") {
            DishCard(
                recipeCard = longNameCard,
                inFavourites = false,
                favouritesCollectionId = null,
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                navigateToRoute = {},
            )
        }
    }

    // endregion

    // region Large card (largeCard = true)

    @Test
    fun dishCard_large_notInFavourites() {
        snapshot("dish_card_large_not_fav") {
            DishCard(
                recipeCard = sampleCard,
                inFavourites = false,
                favouritesCollectionId = "fav-id",
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                navigateToRoute = {},
                largeCard = true,
            )
        }
    }

    @Test
    fun dishCard_large_inFavourites() {
        snapshot("dish_card_large_in_fav") {
            DishCard(
                recipeCard = sampleCard,
                inFavourites = true,
                favouritesCollectionId = "fav-id",
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                navigateToRoute = {},
                largeCard = true,
            )
        }
    }

    @Test
    fun dishCard_large_longName() {
        snapshot("dish_card_large_long_name") {
            DishCard(
                recipeCard = longNameCard,
                inFavourites = false,
                favouritesCollectionId = null,
                addToFavourites = { _, _ -> },
                removeFromFavourites = { _, _ -> },
                navigateToRoute = {},
                largeCard = true,
            )
        }
    }

    // endregion
}