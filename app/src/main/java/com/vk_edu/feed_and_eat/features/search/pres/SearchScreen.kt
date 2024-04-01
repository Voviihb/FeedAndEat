package com.vk_edu.feed_and_eat.features.search.pres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vk_edu.feed_and_eat.R


@Composable
fun SearchScreen(
    toNewRecipe : () -> Unit,
    toSingleRecipe : () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(stringResource(id = R.string.HomeScreen))
        Button(
            onClick = toNewRecipe
        ){
            Text("New Recipe")
        }
        Button(
            onClick = toSingleRecipe
        ){
            Text("To Single Recipe")
        }
    }
}