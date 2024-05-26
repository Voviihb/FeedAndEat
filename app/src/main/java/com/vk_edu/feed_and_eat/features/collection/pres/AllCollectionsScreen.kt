package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoldText
import com.vk_edu.feed_and_eat.common.graphics.DishImage
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.RepeatButton
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.features.navigation.pres.Screen
import com.vk_edu.feed_and_eat.ui.theme.LargeText


@Composable
fun AllCollectionsScreen(
    navigateToRoute: (String) -> Unit,
    viewModel: AllCollectionsScreenViewModel = hiltViewModel()
) {
    viewModel.loadAllUserCollections()

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.CollectionOverviewScreen.route) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.pale_cyan))
        ) {
            if (viewModel.loading.value)
                LoadingCircular()
            else if (viewModel.errorMessage.value != null)
                RepeatButton(onClick = {
                    viewModel.clearError()
                    viewModel.loadAllUserCollections()
                })
            else
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.collectionsData.value) { compilation ->
                        CollectionCard(
                            compilation = compilation,
                            navigateToRoute = navigateToRoute,
                        )
                    }
                }
        }
    }
}

@Composable
fun CollectionCard(
    compilation: CollectionDataModel,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            colorResource(R.color.white), colorResource(R.color.white),
            colorResource(R.color.white), colorResource(R.color.white)
        ),
        modifier = modifier.shadow(12.dp, RoundedCornerShape(16.dp)),
        onClick = {
            navigateToRoute(Screen.CollectionScreen.route + "/${compilation.id}")
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DishImage(link = compilation.picture ?: "")
            BoldText(
                text = compilation.name,
                fontSize = LargeText,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
