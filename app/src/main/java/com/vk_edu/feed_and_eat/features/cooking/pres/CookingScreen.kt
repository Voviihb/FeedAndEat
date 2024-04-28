package com.vk_edu.feed_and_eat.features.cooking.pres

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R


@Composable
fun CookingScreen(
    viewModel: CookingScreenViewModel = hiltViewModel()
) {
    val activeTimerState = viewModel.activeTimerState.collectAsState(emptyMap())
    val pausedTimerState = viewModel.pausedTimerState.collectAsState(emptyMap())
    val counter by viewModel.counter
    val start: (String, Int) -> Unit = viewModel::startTimer
    val stop: (String) -> Unit = viewModel::stopTimer
    val pause: (String) -> Unit = viewModel::pauseTimer
    val resume: (String) -> Unit = viewModel::resumeTimer
    val cancel: () -> Unit = viewModel::cancelAllTimers
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
                    start("timer$counter", 10 * counter)
                    viewModel.updateCounter(1)
                }) {
                    Column(
                        modifier = Modifier
                            .width(35.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Start timer", fontSize = 16.sp)
                    }
                }

                Button(onClick = {
                    viewModel.updateCounter(-1)
                    stop("timer$counter")
                }) {
                    Column(
                        modifier = Modifier
                            .width(35.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Stop timer", fontSize = 16.sp)
                    }
                }

                Button(onClick = {
                    viewModel.updateCounter(-1)
                    pause("timer$counter")
                }) {
                    Column(
                        modifier = Modifier
                            .width(35.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Pause timer", fontSize = 16.sp)
                    }
                }

                Button(onClick = {
                    resume("timer$counter")
                    viewModel.updateCounter(1)
                }) {
                    Column(
                        modifier = Modifier
                            .width(35.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Resume timer", fontSize = 16.sp)
                    }
                }

                Button(onClick = {
                    cancel()
                    viewModel.updateCounter(-counter)
                }) {
                    Column(
                        modifier = Modifier
                            .width(35.dp)
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Cancel all", fontSize = 16.sp)
                    }
                }
            }
            Column {
                activeTimerState.value.forEach { (timerId, secondsLeft) ->
                    val hours: Int = secondsLeft.div(60).div(60)
                    val minutes: Int = secondsLeft.div(60)
                    val seconds: Int = secondsLeft.rem(60)
                    Text(
                        text = stringResource(id = R.string.timer_is_at).format(
                            timerId,
                            hours,
                            minutes,
                            seconds
                        ), fontSize = 20.sp
                    )
                }
                pausedTimerState.value.forEach { (timerId, secondsLeft) ->
                    val hours: Int = secondsLeft.div(60).div(60)
                    val minutes: Int = secondsLeft.div(60)
                    val seconds: Int = secondsLeft.rem(60)
                    Text(
                        text = stringResource(id = R.string.paused_timer_is_at).format(
                            timerId,
                            hours,
                            minutes,
                            seconds
                        ), fontSize = 20.sp
                    )
                }
            }
        }
    }
}