package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.navigation.pres.NavBarScreen

@Composable
fun HomeScreen(navController : NavHostController) {
    Scaffold(
        bottomBar = {NavBarScreen(navController = navController)}
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Text(stringResource(id = R.string.HomeScreen))
        }
    }
}