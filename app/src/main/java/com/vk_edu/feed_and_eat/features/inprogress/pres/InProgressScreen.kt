package com.vk_edu.feed_and_eat.features.inprogress.pres

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DishImage
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.features.navigation.pres.Screen
import com.vk_edu.feed_and_eat.features.recipe.domain.TimerState
import com.vk_edu.feed_and_eat.ui.theme.ExtraLargeText
import com.vk_edu.feed_and_eat.ui.theme.LargeText
import com.vk_edu.feed_and_eat.ui.theme.MediumText
import java.util.concurrent.TimeUnit

@Composable
fun InProgressScreen(
    navigateToRoute: (String) -> Unit,
    viewModel: InProgressScreenViewModel = hiltViewModel()
) {
    val activeTimerState by viewModel.activeTimerState.collectAsState(emptyMap())

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.InProgressScreen.route) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(id = R.color.white_cyan))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    stringResource(id = R.string.inProgressScreen),
                    fontSize = ExtraLargeText,
                    fontWeight = FontWeight.Bold,
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(activeTimerState.keys.toList()) { timer ->
                        val timerState = activeTimerState[timer]
                        if (timerState != null) {
                            TimerCard(
                                name = timer,
                                timerState = timerState,
                                navigateToRoute = navigateToRoute,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimerCard(
    name: String,
    timerState: TimerState,
    navigateToRoute: (String) -> Unit,
    viewModel: InProgressScreenViewModel
) {
    val time = timerState.remainingSec.toLong()
    val hours = TimeUnit.SECONDS.toHours(time)
    val minutes = TimeUnit.SECONDS.toMinutes(time) % 60
    val seconds = TimeUnit.SECONDS.toSeconds(time) % 60 % 60
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            colorResource(R.color.white), colorResource(R.color.white),
            colorResource(R.color.white), colorResource(R.color.white)
        ),
        border = BorderStroke(1.dp, colorResource(id = R.color.dark_cyan)),
        onClick = { navigateToRoute(Screen.RecipeScreen.route + "/${timerState.recipeId}") }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            if (timerState.recipeImage != null) {
                DishImage(
                    timerState.recipeImage,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1.0f)
            ) {
                Text(
                    text = name,
                    fontSize = LargeText,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.timer_format).format(
                        hours,
                        minutes,
                        seconds,
                    ),
                    fontSize = MediumText,
                    color = Color.Black
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                if (timerState.isPaused) {
                    ResumeButton(
                        onClick = { viewModel.resumeTimer(name) },
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    PauseButton(
                        onClick = { viewModel.pauseTimer(name) },
                        modifier = Modifier.size(40.dp)
                    )
                }

                DropButton(onClick = { viewModel.stopTimer(name) }, modifier = Modifier.size(40.dp))
            }
        }
    }
}

@Composable
private fun PauseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        contentPadding = PaddingValues(4.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.white),
            colorResource(id = R.color.black)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            colorResource(id = R.color.black)
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.pause),
            contentDescription = stringResource(id = R.string.to_pause)
        )
    }
}

@Composable
private fun DropButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        contentPadding = PaddingValues(4.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.white),
            colorResource(id = R.color.black)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            colorResource(id = R.color.black)
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.drop),
            contentDescription = stringResource(id = R.string.to_drop)
        )
    }
}

@Composable
private fun ResumeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        contentPadding = PaddingValues(4.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.white),
            colorResource(id = R.color.black)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            colorResource(id = R.color.black)
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.play),
            contentDescription = stringResource(id = R.string.to_play)
        )
    }
}