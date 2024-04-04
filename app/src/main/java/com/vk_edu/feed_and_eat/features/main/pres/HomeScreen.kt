package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LargeIcon
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.ui.theme.DarkTurquoise
import com.vk_edu.feed_and_eat.ui.theme.LargeText
import com.vk_edu.feed_and_eat.ui.theme.White


data class CardData(val link: String, val ingredients: Int, val steps: Int,
                    val name: String, val rating: Double, val cooked: Int)

@Composable
fun HomeScreen() {
    val elements = List(8) {
        CardData(
            link = "https://img.spoonacular.com/recipes/641732-556x370.jpg",
            ingredients = 15,
            steps = 10,
            name = "Dulce De Leche Swirled Amaretto Frozen Yogurt",
            rating = 3.0,
            cooked = 156
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp, 84.dp, 8.dp, 8.dp)
        ) {
            items(elements) { cardData ->
                DishCard(
                    link = cardData.link,
                    ingredients = cardData.ingredients,
                    steps = cardData.steps,
                    name = cardData.name,
                    rating = cardData.rating,
                    cooked = cardData.cooked
                )
            }
        }

        Box(
            modifier = Modifier.height(84.dp).padding(8.dp, 16.dp)
        ) {
            Card (
                shape = RoundedCornerShape(24.dp),
                colors = CardColors(White, White, White, White),
                modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(40.dp)),
                onClick = {}
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp, 4.dp, 4.dp)
                ) {
                    LightText(text = stringResource(R.string.searchLabel), fontSize = LargeText)
                    LargeIcon(painter = painterResource(R.drawable.delete), color = DarkTurquoise)
                }
            }
        }
    }
}