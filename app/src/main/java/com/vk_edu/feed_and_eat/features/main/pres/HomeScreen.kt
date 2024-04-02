package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vk_edu.feed_and_eat.common.graphics.DishCard


@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        DishCard(
            link = "https://img.spoonacular.com/recipes/665029-556x370.jpg",
            ingredients = 15,
            steps = 10,
            name = "Watermelon Popsicles with Mint, Basil & Lime",
            rating = 3.0,
            cooked = 156,
            modifier = Modifier.fillMaxWidth(0.46f)
        )
    }
}