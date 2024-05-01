package com.vk_edu.feed_and_eat.features.recipe.pres.step

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.LargeIcon
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.recipe.data.models.Routes
import com.vk_edu.feed_and_eat.features.recipe.pres.timer.Timer
import com.vk_edu.feed_and_eat.ui.theme.LargeText

@Composable
fun TimeInfoCard(time : Int?, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(12.dp, 12.dp, 12.dp, 20.dp)) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardColors(
                colorResource(R.color.white), colorResource(R.color.white),
                colorResource(R.color.white), colorResource(R.color.white)
            ),
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            onClick = { /* TODO add function */ }
        ) {
            Row(
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp, 0.dp, 8.dp, 0.dp)
            ) {
                LargeIcon(
                    painter = painterResource(R.drawable.clock),
                    color = colorResource(R.color.black),
                    modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                if (time != null){
                    LightText(text = time.toString(), fontSize = LargeText)
                } else {
                    LightText(text = stringResource(id = R.string.unlimited), fontSize = LargeText)
                }
            }
        }
    }
}

@Composable
fun TimeInfoCard(time1 : Int, time2 : Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(12.dp, 12.dp, 12.dp, 20.dp)) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardColors(
                colorResource(R.color.white), colorResource(R.color.white),
                colorResource(R.color.white), colorResource(R.color.white)
            ),
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            onClick = { /* TODO add function */ }
        ) {
            Row(
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp, 0.dp, 8.dp, 0.dp)
            ) {
                LargeIcon(
                    painter = painterResource(R.drawable.clock),
                    color = colorResource(R.color.black),
                    modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                LightText(text = if (time2 > time1) "$time1 - $time2" else "$time2 - $time1", fontSize = LargeText)
            }
        }
    }
}

@Composable
fun TextContainer(
    text : String
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 15.dp)
            .background(colorResource(R.color.transparent)),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.short_recipe),
            color = colorResource(R.color.gray),
            textAlign = TextAlign.Left,
            fontSize = 25.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.transparent)),
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            userScrollEnabled = true,
            modifier = Modifier
                .background(
                    colorResource(R.color.white_cyan),
                    shape = RoundedCornerShape(20.dp),
                )
                .clip(shape = RoundedCornerShape(20.dp))
                .height(280.dp)
                .border(
                    1.dp,
                    colorResource(R.color.cyan_fae),
                    shape = RoundedCornerShape(20.dp)
                ),
        ){
            item {
                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 25.dp, top = 10.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun ButtonContainer(
    navigateToStep: (Int) -> Unit,
    navigateToRecipe: (String) -> Unit,
    clear: () -> Unit,
    id : Int,
    maxId : Int,
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(4.dp)
    ) {
        val texts = listOf(R.string.backstep, R.string.finish, R.string.nextstep).map { stringResource(it)}
        val functions = listOf(
            {if (id > 0) navigateToStep(id - 1) else navigateToRecipe(Routes.Recipe.route); clear()},
            { navigateToRecipe(Routes.Recipe.route); clear() },
            {if (id < maxId) navigateToStep(id + 1) else navigateToRecipe(Routes.Congrats.route); clear()}
            )
        repeat(3){index ->
            Button(
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    if (index % 2 == 0) colorResource(id = R.color.white_cyan) else colorResource(id = R.color.cyan_fae),
                    contentColor = colorResource(R.color.black)
                ),
                border = BorderStroke(
                    2.dp,
                    color = if (index % 2 == 0) colorResource(id = R.color.dark_cyan) else colorResource(R.color.white_cyan),
                ),
                onClick = functions[index],
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(112.dp)
            ) {
                Text(
                    text = texts[index],
                    overflow = TextOverflow.Visible
                )
            }
        }
    }
}


@Composable
fun StepScreen(
    navigateToStep: (Int) -> Unit,
    navigateToRecipe: (String) -> Unit,
    data: Instruction,
    id : Int,
    maxId : Int,
    name : String,
    viewModel: StepScreenViewModel = hiltViewModel()
){
    viewModel.id.value = id
    viewModel.name.value = name
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.pale_cyan))
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.height(300.dp)
            ){
                Column {
                    if (!data.timers.isNullOrEmpty()){
                        val constant = data.timers.sumOf { it.number ?: 0 }
                        val mins = data.timers.sumOf { it.lowerLimit ?: 0 }
                        if (mins == 0){
                            TimeInfoCard(time = constant)
                        } else {
                            val maxs = data.timers.sumOf { it.upperLimit ?: 0 }
                            TimeInfoCard(mins, maxs)
                        }
                    } else {
                        TimeInfoCard(null)
                    }
                    TextContainer(data.paragraph)
                }
            }
            if (!data.timers.isNullOrEmpty()) {
                Timer(
                    data.timers,
                    viewModel
                )
            }
            ButtonContainer(navigateToStep, navigateToRecipe, { viewModel.clear() }, id, maxId)
        }
    }
