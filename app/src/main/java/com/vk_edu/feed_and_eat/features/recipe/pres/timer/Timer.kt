package com.vk_edu.feed_and_eat.features.recipe.pres.timer

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import com.vk_edu.feed_and_eat.features.recipe.domain.repository.TimerImpl
import com.vk_edu.feed_and_eat.features.recipe.pres.step.StepScreenViewModel
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
                modifier = Modifier.widthIn(200.dp, 300.dp)
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
            timerList.forEachIndexed { index, timer ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.currentTimer.value = index
                    }
                ) {
                    if (timer.type == "constant") {
                        Text(String.format(
                            "%02d:%02d:%02d",
                            timer.number!! / 60,
                            timer.number % 60,
                            0
                        ))
                    } else {
                        Text(String.format(
                            "%02d:%02d:%02d - %02d:%02d:%02d",
                            timer.lowerLimit!! / 60,
                            timer.lowerLimit % 60,
                            0,
                            timer.upperLimit!! / 60,
                            timer.upperLimit% 60,
                            0,
                        ))
                    }
                }
            }
        }
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
    var displaySlider by viewModel.isDropped
    val timerClass = TimerImpl(viewModel)

    LaunchedEffect(key1 = sliderPosition, key2 = isRunning) {
        if (isRunning) {
            timerClass.startJob()
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
                ButtonContainer(
                    playAction = {
                        isRunning = true
                        displaySlider = false
                     },
                    pauseAction = { isRunning = false },
                    dropAction = {
                        remainingMillis = sliderPosition
                        isRunning = false
                        displaySlider = true
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun CountdownConstantTimer(
    totalMillis: Long,
    name : String,
    timerViewModel : TimerViewModel = hiltViewModel()
) {
    val activeTimerState by timerViewModel.activeTimerState.collectAsState(emptyMap())

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(400.dp)
    ) {
        Box{
            if (timerViewModel.runTimerFlag[name]?.value == true){
                CountdownCircleAnimation(totalMillis, totalMillis)
                CountdownTimerDisplay(totalMillis)
            } else {
                activeTimerState.filter { it.key == name }.forEach { (name, timer) ->
                    CountdownCircleAnimation(timer.totalSec.toLong() * 1000, timer.remainingSec.toLong() * 1000)
                    CountdownTimerDisplay(timer.remainingSec)
                }
            }
        }

        if ((timerViewModel.isRunning[name]?.value == false) &&
            (timerViewModel.runTimerFlag[name]?.value == true))
        {
            StartButton {
                timerViewModel.startTimer(name, totalMillis.toInt() / 1000)
            }
        } else {
            ButtonContainer(
                playAction = {
                    timerViewModel.resumeTimer(name)
                    timerViewModel.changeTimerState(name)
                },
                pauseAction = {
                    timerViewModel.pauseTimer(name)
                },
                dropAction = {
                    timerViewModel.stopTimer(name)
                    timerViewModel.changeInit(name)
                    timerViewModel.startTimer(name, totalMillis.toInt() / 1000)
                    timerViewModel.pauseTimer(name)
                },
                name = name,
                viewModel = timerViewModel
            )
        }
    }
}

@Composable
private fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.white)),
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
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.white)),
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
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.white)),
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
    viewModel: StepScreenViewModel,
){
    Row(
        horizontalArrangement = Arrangement.Center,
    ) {
        DropButton (dropAction)
        if (viewModel.isRunning.value){
            PauseButton(pauseAction)
        } else {
            StartButton(playAction)
        }
    }
}

@Composable
fun ButtonContainer(
    playAction: () -> Unit,
    pauseAction: () -> Unit,
    dropAction: () -> Unit,
    name : String,
    viewModel: TimerViewModel,
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
){
    val name = "${viewModel.name.value} - step ${viewModel.id.value}:"
    val currentTimer by viewModel.currentTimer
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(350.dp)
    ) {
        if (currentTimer < timerList.size){
            if (timerList[currentTimer].type == "constant"){
                val timerViewModel : TimerViewModel = hiltViewModel()
                timerViewModel.changeInit(name)
                CountdownConstantTimer(
                    totalMillis = 60 * 1000 * timerList[currentTimer].number!!.toLong(),
                    name = name,
                    timerViewModel = timerViewModel,
                )
            } else {
                viewModel.setRemainingMillis(
                    60 * 1000 * Integer.min(timerList[currentTimer].lowerLimit!!, timerList[currentTimer].upperLimit!!).toLong(),
                )
                CountdownRangeTimer(
                    60 * 1000 * Integer.min(timerList[currentTimer].lowerLimit!!,
                        timerList[currentTimer].upperLimit!!).toLong(),
                    60 * 1000 * Math.max(timerList[currentTimer].lowerLimit!!,
                        timerList[currentTimer].upperLimit!!).toLong(),
                    viewModel,
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