package com.vk_edu.feed_and_eat.features.recipe.pres.step

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.RatingBar
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.ui.theme.LargeIconSize

@Composable
fun CongratulationScreen(
    name: String,
    navigateToRoute: (String) -> Unit,
    viewModel: CongratulationsScreenViewModel = hiltViewModel()
) {
    val review by viewModel.reviewState
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white_cyan))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .width(400.dp)
                .background(
                    colorResource(id = R.color.white_cyan),
                    RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
                .border(
                    4.dp,
                    colorResource(id = R.color.dark_cyan),
                    RoundedCornerShape(24.dp)
                )
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = stringResource(R.string.congratulations),
                fontSize = 24.sp,
                color = colorResource(id = R.color.gray),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Box(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = name,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.red),
                    modifier = Modifier
                        .background(colorResource(id = R.color.yellow), RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            2.dp,
                            colorResource(id = R.color.red),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }

        RateRecipe(review = review, viewModel = viewModel)

        Button(
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.white_cyan),
                contentColor = colorResource(R.color.black)
            ),
            border = BorderStroke(
                2.dp,
                colorResource(id = R.color.dark_cyan),
            ),
            onClick = { navigateToRoute(BottomScreen.HomeScreen.route) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .width(112.dp)
        ) {
            Text(
                text = stringResource(id = R.string.to_main),
                overflow = TextOverflow.Visible
            )
        }
    }
}

@Composable
private fun RateRecipe(review: Review, viewModel: CongratulationsScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.rate_recipe),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    4.dp,
                    colorResource(id = R.color.dark_cyan),
                    shape = RoundedCornerShape(24.dp)
                )
                .background(Color.Transparent)

        ) {
            TextField(
                value = review.message ?: "",
                onValueChange = { viewModel.messageChanged(it) },
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(id = R.color.white),
                    focusedContainerColor = colorResource(id = R.color.white),
                    errorContainerColor = colorResource(id = R.color.white),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                ),
                placeholder = {
                    Text(text = stringResource(R.string.enter_your_opinion_about_this_recipe))
                },
                textStyle = TextStyle(fontSize = 20.sp)
            )
        }
        RatingBar(
            review.mark.toFloat(),
            stars = 5,
            modifier = Modifier.height(LargeIconSize)
        )

    }
}