package com.vk_edu.feed_and_eat.features.search.pres

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalFocusManager
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

@Composable
fun SearchScreen(
    navigateToRoute: (String) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.SearchScreen.route) }
    ) { padding ->
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .background(colorResource(R.color.pale_cyan))
                .padding(padding)
        ) {
            CardsGrid(viewModel = viewModel, navigateToRoute = navigateToRoute)
            SearchCard(viewModel = viewModel)

            val rightBlockEnabled = remember { mutableStateOf(false) }
            Row(modifier = Modifier.fillMaxSize()) {
                SortingAndFiltersButton(
                    rightBlockEnabled = rightBlockEnabled,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                if (rightBlockEnabled.value)
                    SortingAndFiltersBlock(
                        viewModel = viewModel,
                        rightBlockEnabled = rightBlockEnabled,
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight()
                    )
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
fun CardsGrid(
    viewModel: SearchScreenViewModel,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()
    val cardsData = viewModel.cardsDataPager.collectAsLazyPagingItems()
    if (viewModel.reloadData.value) {
        runBlocking { gridState.scrollToItem(0) }
        viewModel.setRefreshFlag()
        cardsData.refresh()
        viewModel.reloadDataFinished()
    }
    if (viewModel.loading.value && cardsData.itemCount == 0)
        LoadingCircular()
    else
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(12.dp, 84.dp, 12.dp, 12.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(cardsData.itemCount) { index ->
                val cardData = cardsData[index]
                if (cardData != null)
                    DishCard(
                        link = cardData.image,
                        ingredients = cardData.ingredients,
                        steps = cardData.steps,
                        name = cardData.name,
                        rating = cardData.rating,
                        cooked = cardData.cooked,
                        id = cardData.recipeId,
                        navigateToRoute = navigateToRoute
                    )
            }
        }
}

@Composable
fun SortingAndFiltersBlock(viewModel: SearchScreenViewModel, rightBlockEnabled: MutableState<Boolean>, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxHeight()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(60.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp, 0.dp, 0.dp, 0.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .shadow(12.dp, RoundedCornerShape(0.dp, 0.dp, 0.dp, 24.dp))
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    colorResource(R.color.white),
                    RoundedCornerShape(24.dp, 0.dp, 0.dp, 24.dp)
                )
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp)
            ) {
                Sorting(viewModel = viewModel)

                TagsFilter(viewModel = viewModel)

                NutrientFilter(title = stringResource(R.string.calories), nutrient = Nutrient.CALORIES.value, viewModel = viewModel)
                NutrientFilter(title = stringResource(R.string.sugar), nutrient = Nutrient.SUGAR.value, viewModel = viewModel)
                NutrientFilter(title = stringResource(R.string.carbohydrates), nutrient = Nutrient.CARBOHYDRATES.value, viewModel = viewModel)
                NutrientFilter(title = stringResource(R.string.fat), nutrient = Nutrient.FAT.value, viewModel = viewModel)
                NutrientFilter(title = stringResource(R.string.protein), nutrient = Nutrient.PROTEIN.value, viewModel = viewModel)

                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        colorResource(R.color.white_cyan), colorResource(R.color.white_cyan),
                        colorResource(R.color.white_cyan), colorResource(R.color.white_cyan)
                    ),
                    border = BorderStroke(2.dp, colorResource(R.color.medium_cyan)),
                    contentPadding = PaddingValues(36.dp, 16.dp),
                    onClick = {
                        rightBlockEnabled.value = false
                        viewModel.setSortingAndFilters()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.apply),
                        color = colorResource(R.color.dark_cyan),
                        fontSize = MediumText
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .height(124.dp)
                .width(2.dp)
                .padding(0.dp, 80.dp, 0.dp, 0.dp)
                .background(colorResource(R.color.white))
        ) {
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(
                        colorResource(R.color.dark_cyan),
                        RoundedCornerShape(0.dp, 0.dp, 2.dp, 0.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(
                        colorResource(R.color.dark_cyan),
                        RoundedCornerShape(0.dp, 2.dp, 0.dp, 0.dp)
                    )
            )
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
                placeholder = { LightText(text = stringResource(R.string.from), fontSize = MediumText) },
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
                placeholder = { LightText(text = stringResource(R.string.to), fontSize = MediumText) },
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
        LightText(text = stringResource(R.string.tags), fontSize = MediumText)
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
                for (index in 0 until viewModel.filtersForm.value.tags.size) {
                    var rowHeightDp by remember { mutableStateOf(100.dp) }
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
                                        val rowHeightDpCurrent = with(localDensity) { coordinates.size.height.toDp() } + 8.dp
                                        if (rowHeightDp == 100.dp)
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
    val typesOfSorting = listOf(
        stringResource(R.string.newness),
        stringResource(R.string.rating),
        stringResource(R.string.popularity)
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        LightText(text = stringResource(R.string.sort_by), fontSize = MediumText)
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
                        text = AnnotatedString(typesOfSorting[index]),
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

@Composable
fun SortingAndFiltersButton(rightBlockEnabled: MutableState<Boolean>, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier.padding(0.dp, 72.dp, 0.dp, 0.dp)
    ) {
        val focusManager = LocalFocusManager.current
        Button(
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                colorResource(R.color.white), colorResource(R.color.white),
                colorResource(R.color.white), colorResource(R.color.white)
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .size(60.dp)
                .shadow(12.dp, RoundedCornerShape(8.dp)),
            onClick = {
                focusManager.clearFocus()
                rightBlockEnabled.value = !rightBlockEnabled.value
            }
        ) {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .border(2.dp, colorResource(R.color.dark_cyan), RoundedCornerShape(8.dp))
                ) {
                    Box(modifier = Modifier.padding(4.dp)) {
                        MediumIcon(
                            painter = painterResource(R.drawable.filter),
                            color = colorResource(R.color.medium_cyan)
                        )
                    }
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        MediumIcon(
                            painter = painterResource(R.drawable.sorting),
                            color = colorResource(R.color.medium_cyan)
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .height(44.dp)
                        .width(2.dp)
                        .background(colorResource(R.color.white))
                ) {
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                            .background(
                                colorResource(R.color.dark_cyan),
                                RoundedCornerShape(0.dp, 0.dp, 0.dp, 2.dp)
                            )
                    )
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                            .background(
                                colorResource(R.color.dark_cyan),
                                RoundedCornerShape(2.dp, 0.dp, 0.dp, 0.dp)
                            )
                    )
                }
            }
        }
    }
}
