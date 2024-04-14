package com.vk_edu.feed_and_eat.features.search.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.MediumIcon
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.ui.theme.LargeText
import kotlinx.coroutines.runBlocking

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
            CardsGrid(viewModel = viewModel)

            SearchCard(viewModel = viewModel)
        }
    }
}

@Composable
fun SearchCard(viewModel: SearchScreenViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(12.dp, 12.dp, 12.dp, 20.dp)) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardColors(colorResource(R.color.white), colorResource(R.color.white),
                colorResource(R.color.white), colorResource(R.color.white)),
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(26.dp))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp, 0.dp)
            ) {
                val keyboardController = LocalSoftwareKeyboardController.current
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
                    modifier = Modifier.requiredHeight(64.dp),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.setRequest()
                            keyboardController?.hide()
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    onValueChange = { value -> viewModel.requestBodyChanged(value) }
                )
                Button(
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonColors(colorResource(R.color.medium_cyan), colorResource(R.color.medium_cyan),
                        colorResource(R.color.medium_cyan), colorResource(R.color.medium_cyan)),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(44.dp),
                    onClick = {
                        viewModel.setRequest()
                        keyboardController?.hide()
                    }
                ) {
                    MediumIcon(
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
fun CardsGrid(viewModel: SearchScreenViewModel, modifier: Modifier = Modifier) {
    val gridState = rememberLazyGridState()
    val cardsData = viewModel.cardsDataPager.collectAsLazyPagingItems()
    if (viewModel.reloadData.value) {
        runBlocking {
            gridState.scrollToItem(0)
        }
        cardsData.refresh()
        viewModel.reloadDataFinished()
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(12.dp, 84.dp, 12.dp, 12.dp)
    ) {
        items(cardsData.itemCount) { index ->
            val cardData = cardsData[index]
            DishCard(
                link = cardData?.link ?: "",
                ingredients = cardData?.ingredients ?: 0,
                steps = cardData?.steps ?: 0,
                name = cardData?.name ?: "",
                rating = cardData?.rating ?: 0.0,
                cooked = cardData?.cooked ?: 0
            )
        }
    }
}
