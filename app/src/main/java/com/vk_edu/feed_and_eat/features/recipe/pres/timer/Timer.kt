package com.vk_edu.feed_and_eat.features.recipe.pres.timer

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import com.vk_edu.feed_and_eat.features.recipe.pres.step.StepScreenViewModel
import java.lang.Long.max
import java.util.concurrent.TimeUnit


@Composable
fun FinishMessage(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(300.dp)
            .background(colorResource(id = R.color.white), shape = RoundedCornerShape(150.dp))
    ){
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.tick),
                tint = colorResource(id = R.color.salad),
                contentDescription = stringResource(id = R.string.success)
            )
            Text(
                stringResource(id = R.string.cangonextstep),
                fontSize = 24.sp,
                color = colorResource(id = R.color.gray),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DropDownTimerList(
    timerList: List<Timer>,
    viewModel: StepScreenViewModel
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                painter = painterResource(id = R.drawable.clock),
                tint = colorResource(id = R.color.cyan_fae),
                contentDescription = "More"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            var text: String
            timerList.forEachIndexed { index, timer ->
                if (timer.type == "constant") {
                    text = (String.format(
                        "%02d:%02d:%02d",
                        timer.number!! / 60,
                        timer.number % 60,
                        0
                    ))
                } else {
                    text = (String.format(
                        "%02d:%02d:%02d - %02d:%02d:%02d",
                        timer.lowerLimit!! / 60,
                        timer.lowerLimit % 60,
                        0,
                        timer.upperLimit!! / 60,
                        timer.upperLimit% 60,
                        0,
                    ))
                }
                DropdownMenuItem(
                    onClick = {
                        viewModel.currentTimer.value = index
                    },
                    text = { Text(text) }
                )
                }
            }
        }
    }

@Composable
fun CountdownConstantTimer(
    totalMillis: Long,
    name : String,
    viewModel : StepScreenViewModel = hiltViewModel()
) {
    val activeTimerState by viewModel.activeTimerState.collectAsState(emptyMap())

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(400.dp)
    ) {
        Box{
            if (viewModel.runTimerFlag[name]?.value == true){
                CountdownCircleAnimation(totalMillis, totalMillis)
                CountdownTimerDisplay(totalMillis)
            } else {
                activeTimerState.filter { it.key == name }.forEach { (_, timer) ->
                    Log.d("ACTIVE", timer.toString())
                    CountdownCircleAnimation(timer.totalSec.toLong() * 1000, timer.remainingSec.toLong() * 1000)
                    CountdownTimerDisplay(timer.remainingSec)
                    if (timer.remainingSec <= 1){
                        Log.d("OVER", "")
                        viewModel.iterateTimerOrder(1)
                    }
                }
            }
        }

        if ((viewModel.isRunning[name]?.value == false) &&
            (viewModel.runTimerFlag[name]?.value == true))
        {
            StartButton {
                viewModel.startTimer(name, totalMillis.toInt() / 1000)
            }
        } else {
            ButtonContainer(
                playAction = {
                    viewModel.resumeTimer(name)
                },
                pauseAction = {
                    viewModel.pauseTimer(name)
                },
                dropAction = {
                    viewModel.stopTimer(name)
                    viewModel.changeInit(name)
                },
                name = name,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun CountdownRangeTimer(
    minMillis: Long,
    maxMillis: Long,
    name : String,
    viewModel : StepScreenViewModel = hiltViewModel()
) {
    val activeTimerState by viewModel.activeTimerState.collectAsState(emptyMap())

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(400.dp)
    ) {
        Box {
            if ((viewModel.runTimerFlag[name]?.value == true)) {
                CountdownCircleAnimation(minMillis, minMillis)
                CountdownTimerDisplay(max(viewModel.sliderPosition[name]?.value ?: 0,  minMillis))
            } else {
                activeTimerState.filter { it.key == name }.forEach { (_, timer) ->
                    CountdownCircleAnimation(
                        timer.totalSec.toLong() * 1000,
                        timer.remainingSec.toLong() * 1000
                    )
                    CountdownTimerDisplay(timer.remainingSec)
                    if (timer.remainingSec <= 1){
                        viewModel.iterateTimerOrder(1)
                    }
                }
            }
        }
        if (viewModel.runTimerFlag[name]?.value == true) {
            Slider(
                value = viewModel.sliderPosition[name]?.value?.toFloat() ?: minMillis.toFloat(),
                onValueChange = { newValue ->
                    viewModel.changeSliderValue(name, newValue.toLong())
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

        if ((viewModel.isRunning[name]?.value == false) &&
            (viewModel.runTimerFlag[name]?.value == true))
        {
            StartButton {
                viewModel.startTimer(
                    name = name,
                    time = viewModel.sliderPosition[name]?.value?.toInt()?.div(1000)?: (minMillis.toInt() / 1000),
                )
            }
        } else {
            ButtonContainer(
                playAction = {
                    viewModel.resumeTimer(name)
                },
                pauseAction = {
                    viewModel.pauseTimer(name)
                },
                dropAction = {
                    viewModel.stopTimer(name)
                    viewModel.changeInit(name)
                    viewModel.changeSliderValue(name, minMillis)
                },
                name = name,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.white),
            colorResource(id = R.color.black)
        ),
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.white), RoundedCornerShape(12.dp))
            .border(2.dp, colorResource(id = R.color.black), RoundedCornerShape(12.dp))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.play),
            contentDescription = stringResource(id = R.string.to_play)
        )
    }
}

@Composable
private fun PauseButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.white),
            colorResource(id = R.color.black),
        ),
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.white), RoundedCornerShape(12.dp))
            .border(2.dp, colorResource(id = R.color.black), RoundedCornerShape(12.dp))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.pause),
            contentDescription = stringResource(id = R.string.to_pause)
        )
    }
}

@Composable
private fun DropButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.white),
            colorResource(id = R.color.black)
        ),
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.white), RoundedCornerShape(12.dp))
            .border(2.dp, colorResource(id = R.color.black), RoundedCornerShape(12.dp))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.drop),
            contentDescription = stringResource(id = R.string.to_drop)
        )
    }
}

@Composable
private fun CountdownTimerDisplay(remainingMillis: Int) {
    val hours = TimeUnit.SECONDS.toHours(remainingMillis.toLong())
    val minutes = TimeUnit.SECONDS.toMinutes(remainingMillis.toLong()) % 60
    val seconds = TimeUnit.SECONDS.toSeconds(remainingMillis.toLong()) % 60

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
fun ButtonContainer(
    playAction: () -> Unit,
    pauseAction: () -> Unit,
    dropAction: () -> Unit,
    name : String,
    viewModel: StepScreenViewModel,
){
    Row(
        horizontalArrangement = Arrangement.Center,
    ) {
        DropButton (dropAction)
        if (viewModel.isRunning[name]?.value == true){
            PauseButton(pauseAction)
        } else {
            StartButton(playAction)
        }
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

@Composable
fun Timer(
    timerList : List<Timer>,
    viewModel : StepScreenViewModel,
    modifier: Modifier = Modifier,
){
    val name = "${viewModel.name.value} - step ${viewModel.id.value}:"
    val currentTimer by viewModel.currentTimer
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(350.dp)
    ) {
        if (currentTimer < timerList.size){
            viewModel.changeInit(name)
            if (timerList[currentTimer].type == "constant"){
                CountdownConstantTimer(
                    totalMillis = 60 * 1000 * timerList[currentTimer].number!!.toLong(),
                    name = name,
                    viewModel = viewModel
                )
            } else {
                viewModel.changeSliderValue(
                    name,
                    60 * 1000 * Integer.min(timerList[currentTimer].lowerLimit!!,
                    timerList[currentTimer].upperLimit!!).toLong()
                )
                CountdownRangeTimer(
                    minMillis = 60 * 1000 * Integer.min(timerList[currentTimer].lowerLimit!!,
                        timerList[currentTimer].upperLimit!!).toLong(),
                    maxMillis = 60 * 1000 * (timerList[currentTimer].lowerLimit!!).coerceAtLeast(timerList[currentTimer].upperLimit!!)
                        .toLong(),
                    name = name,
                    viewModel = viewModel
                )
            }
        } else {
            FinishMessage()
        }
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier.fillMaxSize()
        ){
            DropDownTimerList(
                timerList,
                viewModel
            )
        }
    }
}