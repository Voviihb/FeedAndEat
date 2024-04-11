package com.vk_edu.feed_and_eat.features.search.pres

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LargeIcon
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.ui.theme.LargeText

@Composable
fun SearchScreen(navigateToRoute : (String) -> Unit) {
    val viewModel: SearchScreenViewModel = hiltViewModel()

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.CollectionScreen.route) }
    ) { padding ->
        Box(
            modifier = Modifier
                .background(colorResource(R.color.pale_cyan))
                .padding(padding)
        ) {
            viewModel.addCardsData()
            CardsGrid(cardsData = viewModel.cardsData.value)

            SearchCard(viewModel = viewModel)
        }
    }
}

@Composable
fun SearchCard(viewModel: SearchScreenViewModel, modifier: Modifier = Modifier) {
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
                    .padding(4.dp, 0.dp)
            ) {
                TextField(
                    value = viewModel.searchForm.value.requestBody,
                    textStyle = TextStyle(fontSize = LargeText, color = colorResource(R.color.black)),
                    placeholder = { LightText(text = stringResource(R.string.searchLabel), fontSize = LargeText) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        errorContainerColor = colorResource(R.color.white),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedTextColor = colorResource(R.color.black),
                        unfocusedTextColor = colorResource(R.color.black),
                        disabledTextColor = colorResource(R.color.black),
                        cursorColor = colorResource(R.color.black),
                        errorCursorColor = colorResource(R.color.black)
                    ),
                    modifier = Modifier
                        .requiredHeight(64.dp)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key.keyCode == Key.Enter.keyCode)
                                viewModel.setRequest()
                            false
                        },
                    onValueChange = { value -> viewModel.requestBodyChanged(value) }
                )
                Button(
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonColors(colorResource(R.color.medium_cyan), colorResource(R.color.medium_cyan),
                        colorResource(R.color.medium_cyan), colorResource(R.color.medium_cyan)),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(44.dp),
                    onClick = { viewModel.setRequest() }
                ) {
                    LargeIcon(
                        painter = painterResource(R.drawable.search_icon),
                        color = colorResource(R.color.white),
                        modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
                    )
                }

            }
        }
    }
}

@Composable
fun CardsGrid(cardsData: List<CardDataModel>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(12.dp, 84.dp, 12.dp, 12.dp)
    ) {
        items(cardsData) { cardData ->
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
}



/*
@Composable
fun SearchScreen(
    navigateToRoute : (String) -> Unit,
) {
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.SearchScreen.route) }
    ) {padding ->
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Text(stringResource(id = R.string.SearchScreen))
        Button(
            onClick = { navigateToRoute(Screen.NewRecipeScreen.route) }
        ){
            Text("New Recipe")
        }
        Button(
            onClick = { navigateToRoute(Screen.RecipeScreen.route) }
        ){
            Text("To Single Recipe")
        }
    }
    }
}*/