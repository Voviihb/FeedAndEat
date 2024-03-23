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
import kotlin.random.Random

@Composable
fun DishScreen() {
    val viewModel: DishScreenViewModel = viewModel()
    val errorMsg by viewModel.errorMessage

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
                    viewModel.postDish("Banana ${Random.nextInt(0, 100)}", 15, 10)
                }) {
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Post banana", fontSize = 24.sp)
                    }
                }

                Button(onClick = {
                    viewModel.loadDishes()
                }) {
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Load bananas", fontSize = 24.sp)
                    }
                }
            }
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(viewModel.dishesList) { dish ->
                    Text(
                        text = "id = ${dish.id}, name = ${dish.name}, rating = ${dish.rating}",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }


    }
}