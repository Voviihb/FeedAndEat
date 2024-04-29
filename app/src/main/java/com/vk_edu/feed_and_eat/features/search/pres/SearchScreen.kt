package com.vk_edu.feed_and_eat.features.search.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DarkText
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.MediumIcon
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.ui.theme.LargeText
import com.vk_edu.feed_and_eat.ui.theme.MediumText
import com.vk_edu.feed_and_eat.ui.theme.SmallText
import kotlinx.coroutines.runBlocking


private val TYPES_OF_SORTING = listOf("newness", "rating", "popularity")
private const val CALORIES_INT = 0
private const val SUGAR_INT = 1
private const val CARBOHYDRATES_INT = 2
private const val FAT_INT = 3
private const val PROTEIN_INT = 4

@Composable
fun SearchScreen(
    navigateToRoute: (String) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val loading by viewModel.loading
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.SearchScreen.route) }
    ) { padding ->
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .background(colorResource(R.color.pale_cyan))
                .padding(padding)
        ) {
            if (loading) {
                LoadingCircular(padding = PaddingValues(4.dp))
            } else {
                CardsGrid(viewModel = viewModel)

                SearchCard(viewModel = viewModel)

                SortingAndFiltersBlock(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SearchCard(viewModel: SearchScreenViewModel, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(12.dp, 12.dp, 12.dp, 20.dp)) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardColors(
                colorResource(R.color.white), colorResource(R.color.white),
                colorResource(R.color.white), colorResource(R.color.white)
            ),
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
                    value = viewModel.searchForm.value,
                    textStyle = TextStyle(
                        fontSize = LargeText,
                        color = colorResource(R.color.black)
                    ),
                    placeholder = {
                        LightText(
                            text = stringResource(R.string.searchLabel),
                            fontSize = LargeText
                        )
                    },
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
                    onValueChange = { value -> viewModel.requestChanged(value) }
                )
                Button(
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonColors(
                        colorResource(R.color.medium_cyan), colorResource(R.color.medium_cyan),
                        colorResource(R.color.medium_cyan), colorResource(R.color.medium_cyan)
                    ),
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
            if (cardData != null)
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


@Composable
fun SortingAndFiltersBlock(viewModel: SearchScreenViewModel, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.666667f)
            .background(colorResource(R.color.white), RoundedCornerShape(24.dp, 0.dp, 0.dp, 24.dp))
            .border(
                2.dp,
                colorResource(R.color.dark_cyan),
                RoundedCornerShape(24.dp, 0.dp, 0.dp, 24.dp)
            )
            .clip(RoundedCornerShape(24.dp, 0.dp, 0.dp, 24.dp))
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Sorting(viewModel = viewModel)

            TagsFilter(viewModel = viewModel)

            NutrientFilter(title = "Calories", nutrient = CALORIES_INT, viewModel = viewModel)
            NutrientFilter(title = "Sugar", nutrient = SUGAR_INT, viewModel = viewModel)
            NutrientFilter(
                title = "Carbohydrates",
                nutrient = CARBOHYDRATES_INT,
                viewModel = viewModel
            )
            NutrientFilter(title = "Fat", nutrient = FAT_INT, viewModel = viewModel)
            NutrientFilter(title = "Protein", nutrient = PROTEIN_INT, viewModel = viewModel)
        }
    }
}

@Composable
fun NutrientFilter(
    title: String,
    nutrient: Int,
    viewModel: SearchScreenViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        LightText(text = title, fontSize = MediumText)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = viewModel.filtersForm.value.nutrients[nutrient].min,
                textStyle = TextStyle(fontSize = MediumText, color = colorResource(R.color.black)),
                placeholder = { LightText(text = "from", fontSize = MediumText) },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white_cyan),
                    focusedContainerColor = colorResource(R.color.white_cyan),
                    errorContainerColor = colorResource(R.color.white_cyan),
                    focusedIndicatorColor = colorResource(R.color.medium_cyan),
                    unfocusedIndicatorColor = colorResource(R.color.medium_cyan),
                    disabledIndicatorColor = colorResource(R.color.medium_cyan),
                    errorIndicatorColor = colorResource(R.color.medium_cyan),
                    focusedTextColor = colorResource(R.color.black),
                    unfocusedTextColor = colorResource(R.color.black),
                    disabledTextColor = colorResource(R.color.black),
                    cursorColor = colorResource(R.color.black),
                    errorCursorColor = colorResource(R.color.black)
                ),
                modifier = Modifier
                    .padding(0.dp, 0.dp, 8.dp, 0.dp)
                    .weight(1f),
                onValueChange = { viewModel.nutrientMinChanged(nutrient, it) }
            )
            OutlinedTextField(
                value = viewModel.filtersForm.value.nutrients[nutrient].max,
                textStyle = TextStyle(fontSize = MediumText, color = colorResource(R.color.black)),
                placeholder = { LightText(text = "to", fontSize = MediumText) },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white_cyan),
                    focusedContainerColor = colorResource(R.color.white_cyan),
                    errorContainerColor = colorResource(R.color.white_cyan),
                    focusedIndicatorColor = colorResource(R.color.medium_cyan),
                    unfocusedIndicatorColor = colorResource(R.color.medium_cyan),
                    disabledIndicatorColor = colorResource(R.color.medium_cyan),
                    errorIndicatorColor = colorResource(R.color.medium_cyan),
                    focusedTextColor = colorResource(R.color.black),
                    unfocusedTextColor = colorResource(R.color.black),
                    disabledTextColor = colorResource(R.color.black),
                    cursorColor = colorResource(R.color.black),
                    errorCursorColor = colorResource(R.color.black)
                ),
                modifier = Modifier
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    .weight(1f),
                onValueChange = { viewModel.nutrientMaxChanged(nutrient, it) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsFilter(viewModel: SearchScreenViewModel, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        LightText(text = "Tags", fontSize = MediumText)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(colorResource(R.color.white_cyan), RoundedCornerShape(8.dp))
                .border(1.dp, colorResource(R.color.medium_cyan), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .verticalScroll(rememberScrollState())
        ) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                val localDensity = LocalDensity.current
                var rowHeightDp by remember { mutableStateOf(100.dp) }
                for (index in 0 until viewModel.filtersForm.value.tags.size) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardColors(
                            colorResource(R.color.white), colorResource(R.color.white),
                            colorResource(R.color.white), colorResource(R.color.white)
                        ),
                        modifier = Modifier.height(rowHeightDp),
                        onClick = { viewModel.tagCheckingChanged(index) }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp, 4.dp)
                        ) {
                            Checkbox(
                                checked = viewModel.filtersForm.value.tags[index].ckecked,
                                colors = CheckboxColors(
                                    colorResource(R.color.black),
                                    colorResource(R.color.black),
                                    colorResource(R.color.white),
                                    colorResource(R.color.white),
                                    colorResource(R.color.white),
                                    colorResource(R.color.white),
                                    colorResource(R.color.white),
                                    colorResource(R.color.black),
                                    colorResource(R.color.black),
                                    colorResource(R.color.black),
                                    colorResource(R.color.black),
                                    colorResource(R.color.black)
                                ),
                                modifier = Modifier.size(16.dp),
                                onCheckedChange = { viewModel.tagCheckingChanged(index) }
                            )
                            DarkText(
                                text = viewModel.filtersForm.value.tags[index].name,
                                fontSize = SmallText,
                                modifier = Modifier
                                    .onGloballyPositioned { coordinates ->
                                        val rowHeightDpCurrent =
                                            with(localDensity) { coordinates.size.width.toDp() } + 8.dp
                                        if (rowHeightDpCurrent < rowHeightDp)
                                            rowHeightDp = rowHeightDpCurrent
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Sorting(viewModel: SearchScreenViewModel, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        LightText(text = "Sort by", fontSize = MediumText)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (index in 0..2) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = viewModel.sortingForm.value == index,
                        colors = RadioButtonColors(
                            colorResource(R.color.black), colorResource(R.color.black),
                            colorResource(R.color.black), colorResource(R.color.black)
                        ),
                        modifier = Modifier.height(16.dp),
                        onClick = { viewModel.sortingChanged(index) }
                    )
                    ClickableText(
                        text = AnnotatedString(TYPES_OF_SORTING[index]),
                        style = TextStyle(
                            fontSize = MediumText,
                            color = colorResource(R.color.black),
                            fontWeight = if (viewModel.sortingForm.value == index) FontWeight.Bold else FontWeight.Normal
                        ),
                        onClick = { viewModel.sortingChanged(index) }
                    )
                }
            }
        }
    }
}
