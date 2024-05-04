package com.vk_edu.feed_and_eat.features.recipe.pres.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.features.recipe.data.RecipeNavGraph


@Composable
fun RepeatButton(
    onClick : () -> Unit,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonColors(
                colorResource(id = R.color.pale_cyan),
                colorResource(R.color.black), colorResource(R.color.white), colorResource(R.color.black)
            ),
            modifier = Modifier
                .background(
                    color = colorResource(id = R.color.pale_cyan),
                    RoundedCornerShape(4.dp)
                )
                .border(
                    2.dp,
                    colorResource(id = R.color.cyan_fae),
                    RoundedCornerShape(4.dp)
                )
                .height(40.dp)
                .width(72.dp)
        ) {
            Text(
                text = stringResource(id = R.string.repeat),
                fontSize = 12.sp,
                overflow = TextOverflow.Visible,
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun RecipeScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack : () -> Unit,
    id : String,
    destination : String,
    viewModel: RecipesScreenViewModel = hiltViewModel()
) {
    viewModel.loadRecipeById(id)
    val recipe by viewModel.recipe

    val reload = {
        viewModel.loadRecipeById(id)
        viewModel.clearError()
    }
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, destination) },
    ) {padding ->
        if (viewModel.loading.value){
            LoadingCircular()
        } else {
            if (viewModel.errorMessage.value != null){
                RepeatButton(reload)
            } else {
                Box(modifier = Modifier.padding(padding)){
                    val navController = rememberNavController()
                    RecipeNavGraph(
                        navigateToRoute,
                        navigateBack = navigateBack,
                        navController,
                        recipe
                    )
                }
            }
        }
    }
}