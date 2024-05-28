package com.vk_edu.feed_and_eat.features.recipe.pres.step

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DishImage
import com.vk_edu.feed_and_eat.common.graphics.RatingBar
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.ui.theme.ExtraLargeText
import com.vk_edu.feed_and_eat.ui.theme.LargeIconSize
import com.vk_edu.feed_and_eat.ui.theme.LargeText
import com.vk_edu.feed_and_eat.ui.theme.MediumText

@Composable
private fun RateRecipe(
    review: Review,
    recipe: Recipe,
    viewModel: CongratulationsScreenViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.rate_recipe),
            fontSize = ExtraLargeText,
            fontWeight = FontWeight.SemiBold
        )

        RatingBar(
            review.mark.toFloat(),
            stars = 5,
            modifier = Modifier.height(LargeIconSize),
            onRatingChanged = { viewModel.markChanged(it) }
        )

        Button(
            contentPadding = PaddingValues(8.dp),
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.dark_cyan),
                contentColor = colorResource(R.color.black)
            ),
            border = BorderStroke(
                2.dp,
                colorResource(R.color.white_cyan),
            ),
            onClick = { viewModel.saveReview(recipe = recipe) },
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = stringResource(R.string.rate),
                fontSize = LargeText,
                overflow = TextOverflow.Visible
            )
        }
    }
}

@Composable
fun CongratulationScreen(
    recipe: Recipe,
    navigateNoState: (String) -> Unit,
    viewModel: CongratulationsScreenViewModel = hiltViewModel()
){
    val review by viewModel.reviewState

    LaunchedEffect(Unit) {
        viewModel.loadOldReview(recipe)
    }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white_cyan))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(12f)
        ) {
            if (recipe.image != null) {
                DishImage(
                    link = recipe.image,
                    modifier = Modifier
                )
            }
            Text(
                text = stringResource(R.string.congratulations),
                fontSize = 24.sp,
                color = colorResource(id = R.color.gray),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier
                .padding(horizontal = 12.dp)
                .weight(3f)
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.dark_cyan),
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.pale_cyan),
                            RoundedCornerShape(12.dp)
                        )

                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            2.dp,
                            colorResource(id = R.color.dark_cyan),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                )
            }
            RateRecipe(
                review = review,
                recipe = recipe,
                viewModel = viewModel,
                modifier = Modifier.weight(5f)
            )
        }
        Button(
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.pale_cyan),
                contentColor = colorResource(R.color.black)
            ),
            border = BorderStroke(
                2.dp,
                colorResource(id = R.color.dark_cyan),
            ),
            onClick = {
                viewModel.incrementCookedField(recipe = recipe)
                navigateNoState(BottomScreen.HomeScreen.route)
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.to_main),
                overflow = TextOverflow.Visible,
                textAlign = TextAlign.Center,
                fontSize = MediumText,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}