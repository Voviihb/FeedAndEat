package com.vk_edu.feed_and_eat.features.dishes.pres

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.dishes.domain.models.FiltersDTO
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SortFilter

@Composable
fun RecipesScreen() {
    val viewModel: RecipesScreenViewModel = viewModel()
    val errorMsg by viewModel.errorMessage
    val recipesList by viewModel.recipesList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_login),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    viewModel.loadRecipes(20)
                }) {
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Load recipes", fontSize = 24.sp)
                    }
                }

                /* Test data  */
                Button(onClick = {
                    viewModel.loadRecipeById(id = "016VAjdUxDHSfAagMB2k")
                }) {
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Load one recipe", fontSize = 24.sp)
                    }
                }
                Button(onClick = {
                    val filters = FiltersDTO(
                        sort = SortFilter.SORT_POPULARITY,
                        limit = 20,
                        tags = listOf("side dish", "lunch", "main dish")
                    )
                    viewModel.filterRecipes(filters)
                }) {
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Load filtered", fontSize = 24.sp)
                    }
                }
            }
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(recipesList) { dish ->
                    Text(
                        text = dish.name.toString(),
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}