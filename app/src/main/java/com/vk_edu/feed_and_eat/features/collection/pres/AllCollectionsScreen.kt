package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DishCard
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.SquareArrowButton
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.ui.theme.MediumText


@Composable
fun AllCollectionsScreen(
    navigateToRoute : (String) -> Unit,
    navigateBack : () -> Unit,
    viewModel: AllCollectionsScreenViewModel = hiltViewModel()
) {
    viewModel.loadAllUserCollections()
    val collections by viewModel.collectionsData
    val gridState = rememberLazyGridState()

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.CollectionScreen.route) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.pale_cyan))
        ) {
            if (viewModel.loading.value)
                LoadingCircular()
            else if (viewModel.errorMessage.value != null)
                RepeatButton(viewModel = viewModel)
            else
                /* TODO replace sample*/
                LazyVerticalGrid(columns = GridCells.Fixed(2),
                    state = gridState,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(12.dp, 84.dp, 12.dp, 12.dp),
                    modifier = Modifier.fillMaxSize()) {
                    items(collections) {collection ->
                        DishCard(
                            link = collection.picture?: "",
                            ingredients = -1,
                            steps = -1,
                            name = collection.name,
                            rating = -1.0,
                            cooked = -1,
                        )
                    }
                }

            SquareArrowButton(onClick = navigateBack)
        }
    }
}

/*TODO get func from common/graphics*/
@Composable
fun RepeatButton(viewModel: AllCollectionsScreenViewModel, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            contentPadding = PaddingValues(36.dp, 16.dp),
            colors = ButtonColors(
                colorResource(R.color.pale_cyan), colorResource(R.color.pale_cyan),
                colorResource(R.color.pale_cyan), colorResource(R.color.pale_cyan)
            ),
            border = BorderStroke(2.dp, colorResource(R.color.dark_cyan)),
            onClick = {
                viewModel.clearError()
                viewModel.loadAllUserCollections()
            }
        ) {
            Text(
                text = stringResource(R.string.repeat),
                color = colorResource(R.color.dark_cyan),
                fontSize = MediumText
            )
        }
    }
}
