package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoldText
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LargeIcon
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.ui.theme.DarkTurquoise
import com.vk_edu.feed_and_eat.ui.theme.ExtraLargeText
import com.vk_edu.feed_and_eat.ui.theme.LargeText
import com.vk_edu.feed_and_eat.ui.theme.LightTurquoise
import com.vk_edu.feed_and_eat.ui.theme.White

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(LightTurquoise)
    ) {
        Box(modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 20.dp)) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardColors(White, White, White, White),
                modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp)),
                onClick = {}
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LightText(
                        text = stringResource(R.string.searchLabel),
                        fontSize = LargeText,
                        modifier = Modifier.padding(16.dp, 0.dp)
                    )
                    LargeIcon(
                        painter = painterResource(R.drawable.delete),
                        color = DarkTurquoise,
                        modifier = Modifier.padding(4.dp, 0.dp)
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            BoldText(text = stringResource(R.string.title1), fontSize = ExtraLargeText)

            viewModel.getCardTitle1()
            val cardData = viewModel.cardTitle1.value
            DishCard(
                link = cardData.link,
                ingredients = cardData.ingredients,
                steps = cardData.steps,
                name = cardData.name,
                rating = cardData.rating,
                cooked = cardData.cooked,
                largeCard = true,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }

        var columnWidthDp by remember { mutableStateOf(0.dp) }
        val localDensity = LocalDensity.current
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
                .onGloballyPositioned { coordinates ->
                    columnWidthDp = with(localDensity) { coordinates.size.width.toDp() }
                }
        ) {
            BoldText(
                text = stringResource(R.string.title2),
                fontSize = ExtraLargeText,
                modifier = Modifier.padding(12.dp, 0.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp, 0.dp)
            ) {
                viewModel.getCardsTitle2()
                items(viewModel.cardsTitle2) { cardData ->
                    DishCard(
                        link = cardData.link,
                        ingredients = cardData.ingredients,
                        steps = cardData.steps,
                        name = cardData.name,
                        rating = cardData.rating,
                        cooked = cardData.cooked,
                        modifier = Modifier.width((columnWidthDp - 44.dp) / 2)
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp)
        ) {
            BoldText(
                text = stringResource(R.string.title3),
                fontSize = ExtraLargeText,
                modifier = Modifier.padding(12.dp, 0.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp, 0.dp)
            ) {
                viewModel.getCardsTitle3()
                items(viewModel.cardsTitle3) { cardData ->
                    DishCard(
                        link = cardData.link,
                        ingredients = cardData.ingredients,
                        steps = cardData.steps,
                        name = cardData.name,
                        rating = cardData.rating,
                        cooked = cardData.cooked,
                        modifier = Modifier.width((columnWidthDp - 44.dp) / 2)
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 12.dp)
        ) {
            BoldText(
                text = stringResource(R.string.title4),
                fontSize = ExtraLargeText,
                modifier = Modifier.padding(12.dp, 0.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp, 0.dp)
            ) {
                viewModel.getCardsTitle4()
                items(viewModel.cardsTitle4) { cardData ->
                    DishCard(
                        link = cardData.link,
                        ingredients = cardData.ingredients,
                        steps = cardData.steps,
                        name = cardData.name,
                        rating = cardData.rating,
                        cooked = cardData.cooked,
                        modifier = Modifier.width((columnWidthDp - 44.dp) / 2)
                    )
                }
            }
        }
    }
}