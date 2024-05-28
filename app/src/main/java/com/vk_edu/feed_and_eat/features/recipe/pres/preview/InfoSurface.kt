package com.vk_edu.feed_and_eat.features.recipe.pres.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.ui.theme.MediumText
import com.vk_edu.feed_and_eat.ui.theme.SmallText
import kotlinx.coroutines.launch

@Composable
fun InfoSurface(
    model : Recipe,
    drawerState: DrawerState,
    onClick : () -> Unit
){
    val ingredients = model.ingredients
    val energyData = listOf(
        model.nutrients.Calories,
        model.nutrients.Sugar,
        model.nutrients.Protein,
        model.nutrients.Fat,
        model.nutrients.Carbohydrates
    )
    val names = listOf(R.string.calories_data, R.string.fats_data, R.string.proteins_data, R.string.carbons_data, R.string.sugar_data).map{ stringResource( id = it ) }
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    var previous by remember { mutableStateOf((-360.0).dp) }
    var current by remember { mutableStateOf(0.dp) }

    var actualPadding by remember { mutableIntStateOf(0) }
    val buttonSize = 60
    val maxWidth = 360.dp
    val density = LocalDensity.current


    LaunchedEffect(key1 = Unit) {
        snapshotFlow { drawerState.currentOffset }
            .collect {
                val job = launch {
                    current = with(density) { it.toDp() }
                    if (previous < current) {
                        while ((maxWidth + buttonSize.dp - actualPadding.dp) > screenWidth.dp) {
                            actualPadding += 1
                        }
                    }
                    if (previous >= current) {
                        while (actualPadding > 0) {
                            actualPadding -= 1
                        }
                    }
                    previous = current
                }
                job.join()
            }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(Alignment.End, unbounded = true)
                .width(420.dp)
                .padding(start = actualPadding.dp)
        ){
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier.width(60.dp)
            ){
                InfoSquareButton(onClick = {
                    actualPadding = if ((maxWidth + buttonSize.dp - actualPadding.dp) > screenWidth.dp){
                        ((maxWidth.value + buttonSize) - screenWidth).toInt()
                    } else {
                        0
                    }
                    onClick()
                })
            }
            Box(
                modifier = Modifier
                    .width((maxWidth.value - actualPadding).dp)
                    .fillMaxHeight()
                    .background(
                        colorResource(R.color.white),
                        RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp))
                    .border(
                        2.dp,
                        colorResource(id = R.color.dark_cyan),
                        RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp)
                    )
            ){
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    listOf(
                        listOf(stringResource(id = R.string.small_ingredients), model.ingredients.size),
                        listOf(stringResource(id = R.string.step), model.instructions.size)
                    ).forEach{data ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(12.dp))
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.pale_cyan), RoundedCornerShape(12.dp))
                                .border(2.dp, colorResource(id = R.color.dark_cyan), RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                        ){
                            Text(
                                text = "${data[0]}: " + data[1],
                                fontSize = MediumText,
                                color = colorResource(id = R.color.gray),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                    }
                    Text(
                        stringResource(
                            id = R.string.ingredients),
                        fontSize = SmallText,
                        color = colorResource(R.color.gray),
                    )
                    BoxWithCards(bigText = ingredients.map { it.name }.toList())
                    Text(
                        stringResource(
                            id = R.string.tags_data),
                        fontSize = SmallText,
                        color = colorResource(R.color.gray),
                    )
                    BoxWithCards(bigText = model.tags ?: listOf())
                    Text(
                        stringResource(
                            id = R.string.energy_value),
                        fontSize = MediumText,
                        color = colorResource(R.color.gray)
                    )
                    Column {
                        for (i in names.indices){
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, end = 12.dp)
                            ){
                                Text(
                                    text = names[i],
                                    fontSize = MediumText,
                                    color = colorResource(R.color.gray)
                                )
                                Text(text = (energyData[i] ?: "?").toString() + " " + stringResource(id = R.string.gramm))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextBox(text : String){
    val lightWhite =  colorResource(id = R.color.white)
    Text(
        text = text,
        maxLines = 1,
        textAlign = TextAlign.Center,
        fontSize = MediumText,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .padding(start = 8.dp, top = 4.dp)
            .background(lightWhite, shape = RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp)),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BoxWithCards(bigText : List<String?>){
    Box(
        modifier = Modifier

    ) {
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .height(200.dp)
                .background(colorResource(id = R.color.pale_cyan), RoundedCornerShape(12.dp))
                .border(2.dp, colorResource(id = R.color.dark_cyan), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ){
            for (item in bigText){
                if (item != null){
                    TextBox(
                        text = item,
                    )
                }
            }
        }
    }
}