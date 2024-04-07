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
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoldText
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LargeIcon
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.features.main.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.ui.theme.ExtraLargeText
import com.vk_edu.feed_and_eat.ui.theme.LargeText

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(colorResource(R.color.pale_cyan))
    ) {
        SearchCard()

        viewModel.getCardTitle1()
        val cardData = viewModel.largeCardData.value
        LargeCard(cardData = cardData)

        val localDensity = LocalDensity.current
        var columnWidthDp by remember { mutableStateOf(0.dp) }
        viewModel.getCardsTitle2()
        CardsRow(
            title = stringResource(R.string.title2),
            cards = viewModel.cardsDataOfRow1,
            columnWidthDp = columnWidthDp,
            modifier = Modifier.onGloballyPositioned { coordinates ->
                columnWidthDp = with(localDensity) { coordinates.size.width.toDp() }
            }
        )

        viewModel.getCardsTitle3()
        CardsRow(
            title = stringResource(R.string.title3),
            cards = viewModel.cardsDataOfRow2,
            columnWidthDp = columnWidthDp
        )

        viewModel.getCardsTitle4()
        CardsRow(
            title = stringResource(R.string.title4),
            cards = viewModel.cardsDataOfRow3,
            columnWidthDp = columnWidthDp
        )

        Spacer(modifier = Modifier.size(12.dp))
    }
}

@Composable
fun SearchCard(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(12.dp, 12.dp, 12.dp, 20.dp)) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardColors(colorResource(R.color.white), colorResource(R.color.white),
                colorResource(R.color.white), colorResource(R.color.white)),
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            onClick = { /* TODO add function */ }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp, 0.dp, 8.dp, 0.dp)
            ) {
                LightText(text = stringResource(R.string.searchLabel), fontSize = LargeText)
                LargeIcon(
                    painter = painterResource(R.drawable.search_icon),
                    color = colorResource(R.color.medium_cyan),
                    modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
                )
            }
        }
    }
}

@Composable
fun LargeCard(cardData: CardDataModel, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        BoldText(text = stringResource(R.string.title1), fontSize = ExtraLargeText)
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
}

@Composable
fun CardsRow(title: String, cards: List<CardDataModel>, columnWidthDp: Dp, modifier: Modifier = Modifier) {
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
            contentPadding = PaddingValues(12.dp, 0.dp)
        ) {
            items(cards) { cardData ->
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
