package com.vk_edu.feed_and_eat.features.newrecipe.pres

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R


@Composable
fun NewRecipeScreen() {
    Column(
        modifier = Modifier.padding(0.dp)
    ) {
        Text(stringResource(id = R.string.NewRecipeScreen))
    }
}