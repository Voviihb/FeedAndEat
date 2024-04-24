package com.vk_edu.feed_and_eat.features.recipe.pres

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@Composable
fun FinishMessage(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(400.dp)
            .width(400.dp)
            .background(colorResource(id = R.color.white), shape = RoundedCornerShape(200.dp))
    ){
        Text(
        "All timers finished",
            fontSize = 24.sp,
            color = colorResource(id = R.color.gray)
        )
    }
}

@Composable
fun CountdownListTimer(
    timerList : List<Timer>,
    viewModel : StepScreenViewModel,
){
    val currentTimer by viewModel.currentTimer

    if (currentTimer < timerList.size){
        if (timerList[currentTimer].type == "constant"){
            viewModel.setRemainingMillis(
                60 * 1000 * timerList[currentTimer].number!!.toLong(),
            )
            viewModel.dropTimers()
            CountdownTimer(
                totalMillis = 60 * 1000 * timerList[currentTimer].number!!.toLong(),
                viewModel,
            )
        } else {
            viewModel.setRemainingMillis(
                60 * 1000 * Integer.min(timerList[currentTimer].lowerLimit!!, timerList[currentTimer].upperLimit!!).toLong(),
                )
            viewModel.dropTimers()
            CountdownRangeTimer(
                60 * 1000 * Integer.min(timerList[currentTimer].lowerLimit!!, timerList[currentTimer].upperLimit!!).toLong(),
                60 * 1000 * Math.max(timerList[currentTimer].lowerLimit!!, timerList[currentTimer].upperLimit!!).toLong(),
                viewModel,
            )
        }
    } else {
        FinishMessage()
    }
}


@Composable
fun  CountdownRangeTimer(
    minMillis: Long,
    maxMillis: Long,
    viewModel: StepScreenViewModel,
) {
    var sliderPosition by viewModel.sliderPosition

    var remainingMillis by viewModel.remainingMillis
    var isRunning by viewModel.isRunning
    var job by viewModel.job
    var displaySlider by viewModel.displaySlider

    LaunchedEffect(key1 = sliderPosition, key2 = isRunning) {
        if (isRunning) {
            job = launch {
                while (remainingMillis > 0) {
                    delay(1000)
                    remainingMillis -= 1000
                    if (remainingMillis <= 0){
                        viewModel.changeValue(1)
                    }
                }
            }
        } else {
            job?.cancel()
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(400.dp)
    ) {
        Box{
            CountdownCircleAnimation(sliderPosition, remainingMillis)
            CountdownTimerDisplay(remainingMillis)
        }

        if (displaySlider) {
            Slider(
                value = sliderPosition.toFloat(),
                onValueChange = { newValue ->
                    sliderPosition = newValue.toLong()
                    remainingMillis = newValue.toLong()
                },
                valueRange = minMillis.toFloat()..maxMillis.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = colorResource(id = R.color.cyan_fae),
                    activeTrackColor = colorResource(id = R.color.cyan_fae),
                    inactiveTrackColor = colorResource(id = R.color.gray)
                ),
                modifier = Modifier
                    .width(180.dp)
                    .height(30.dp)
            )
        } else {
            Box(modifier = Modifier.height(30.dp))
        }

        if (!isRunning && (sliderPosition == remainingMillis)) {
            StartButton {
                isRunning = true
                displaySlider = false
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                StartButton {
                    isRunning = true
                    displaySlider = false
                }
                DropButton {
                    remainingMillis = sliderPosition
                    isRunning = false
                    displaySlider = true
                }
                PauseButton { isRunning = false }
            }
        }
    }
}

@Composable
fun CountdownTimer(
    totalMillis: Long,
    viewModel: StepScreenViewModel,
) {
    var remainingMillis by viewModel.remainingMillis
    var isRunning by viewModel.isRunning
    var job by viewModel.job

    LaunchedEffect(key1 = totalMillis, key2 = isRunning) {
        if (isRunning) {
            job = launch {
                while (remainingMillis > 0) {
                    delay(1000)
                    remainingMillis -= 1000
                    if (remainingMillis <= 0){
                        viewModel.changeValue(1)
                    }
                }
            }
        } else {
            job?.cancel()
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(400.dp)
    ) {
        Box{
            CountdownCircleAnimation(totalMillis, remainingMillis)
            CountdownTimerDisplay(remainingMillis)
        }

        if (!isRunning && (totalMillis == remainingMillis)) {
            StartButton { isRunning = true }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                PauseButton { isRunning = false }
                DropButton {
                    remainingMillis = totalMillis
                    isRunning = false
                }
                StartButton { isRunning = true }
            }
        }
    }
}

@Composable
private fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.light_cyan)),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Start")
    }
}

@Composable
private fun PauseButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.cyan_fae)),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Pause")
    }
}

@Composable
private fun DropButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.light_cyan)),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Drop")
    }
}

@Composable
private fun CountdownTimerDisplay(remainingMillis: Long) {
    val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(250.dp)
    ){
        Text(
            text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
            fontSize = 32.sp,
            color = colorResource(id = R.color.gray),
        )
    }
}

@Composable
private fun CountdownCircleAnimation(
    totalMillis: Long,
    remainingMillis: Long
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (totalMillis > 0) (remainingMillis.toFloat() / totalMillis.toFloat()) else 1f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing), label = ""
    )

    val stroke = with(LocalDensity.current) { Stroke(8.dp.toPx()) }
    val paleCyan = colorResource(id = R.color.cyan_fae)

    Canvas(
        modifier = Modifier
            .size(250.dp)
    ) {
        drawArc(
            color = paleCyan,
            startAngle = 90f,
            sweepAngle = animatedProgress * 360f,
            useCenter = false,
            style = stroke,
            topLeft = Offset(size.width / 8f, size.height / 8f),
            size = size / 1.3f
        )
    }
}