package com.vk_edu.feed_and_eat.features.inprogress.pres

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoldText
import com.vk_edu.feed_and_eat.common.graphics.DarkText
import com.vk_edu.feed_and_eat.common.graphics.DishImage
import com.vk_edu.feed_and_eat.features.navigation.models.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.features.navigation.models.Screen
import com.vk_edu.feed_and_eat.features.recipe.domain.TimerState
import com.vk_edu.feed_and_eat.ui.theme.ExtraLargeText
import com.vk_edu.feed_and_eat.ui.theme.LargeText
import java.util.Locale
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
        Box(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(id = R.color.white_cyan))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                ) {
                    items(activeTimerState.keys.toList().size) { count ->
                        val timerState = activeTimerState[activeTimerState.keys.toList()[count]]
                        if (timerState != null) {
                            TimerCard(
                                name = activeTimerState.keys.toList()[count],
                                timerState = timerState,
                                navigateToRoute = navigateToRoute,
                                color = if (count % 2 == 0) R.color.light_cyan else R.color.white,
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
    color: Int,
    viewModel: InProgressScreenViewModel
) {
    val pos = name.lastIndexOf(" - ")
    val leftPart = name.slice(0 until pos)
    val rightPart = name.slice((pos + 3) until name.length).replaceFirstChar {
        it.titlecase(Locale.US)
    }
    val modifiedName = "$rightPart $leftPart"
    val time = timerState.remainingSec.toLong()
    val hours = TimeUnit.SECONDS.toHours(time)
    val minutes = TimeUnit.SECONDS.toMinutes(time) % 60
    val seconds = TimeUnit.SECONDS.toSeconds(time) % 60 % 60

    val space = rightPart.lastIndexOf(" ")
    val number = rightPart.slice(space + 1 until rightPart.length - 1)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardColors(
            colorResource(color), colorResource(color),
            colorResource(color), colorResource(color)
        ),
        onClick = {
            navigateToRoute("${Screen.RecipeScreen.route}/${timerState.recipeId}/${number}")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                var columnWidthDp by remember { mutableStateOf(0.dp) }
                val localDensity = LocalDensity.current
                if (timerState.recipeImage != null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(columnWidthDp * 3 / 4)
                            .clip(RoundedCornerShape(0.dp)),

                        ) {
                        DishImage(
                            timerState.recipeImage,
                            modifier = Modifier
                                .size(80.dp)
                                .onGloballyPositioned { coordinates ->
                                    columnWidthDp =
                                        with(localDensity) { coordinates.size.width.toDp() }
                                }
                        )
                    }

                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1.0f)
                ) {
                    DarkText(text = modifiedName, fontSize = LargeText)
                }

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (timerState.isPaused) {
                    ResumeButton(
                        onClick = { viewModel.resumeTimer(name) },
                        color = color,
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    PauseButton(
                        onClick = { viewModel.pauseTimer(name) },
                        color = color,
                        modifier = Modifier.size(40.dp)
                    )
                }

                BoldText(
                    text = stringResource(R.string.timer_format).format(
                        hours,
                        minutes,
                        seconds,
                    ),
                    fontSize = ExtraLargeText,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                DropButton(
                    onClick = { viewModel.stopTimer(name) },
                    color = color,
                    modifier = Modifier.size(40.dp)
                )
            }

        }

    }
}

@Composable
private fun PauseButton(
    onClick: () -> Unit,
    color: Int,
    modifier: Modifier = Modifier
) {
    Button(
        contentPadding = PaddingValues(4.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = color),
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
    color: Int,
    modifier: Modifier = Modifier
) {
    Button(
        contentPadding = PaddingValues(4.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = color),
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
    color: Int,
    modifier: Modifier = Modifier
) {
    Button(
        contentPadding = PaddingValues(4.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = color),
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