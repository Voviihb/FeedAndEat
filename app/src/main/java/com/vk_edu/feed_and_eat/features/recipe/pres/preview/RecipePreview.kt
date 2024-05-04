package com.vk_edu.feed_and_eat.features.recipe.pres.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoxWithCards
import com.vk_edu.feed_and_eat.common.graphics.DishImage
import com.vk_edu.feed_and_eat.common.graphics.InfoSquareButton
import com.vk_edu.feed_and_eat.common.graphics.RatingBarPres
import com.vk_edu.feed_and_eat.common.graphics.SquareArrowButton
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import kotlinx.coroutines.launch

@Composable
fun InfoSurface(
    model : Recipe,
){
    val ingredients = model.ingredients
    val energyData = listOf(
        model.nutrients.Calories,
        model.nutrients.Sugar,
        model.nutrients.Protein,
        model.nutrients.Fat,
        model.nutrients.Carbohydrates
    )
    val names = listOf(R.string.calories, R.string.fats, R.string.proteins, R.string.carbons, R.string.sugar).map{ stringResource( id = it ) }
    val configuration = LocalConfiguration.current

    val width = configuration.screenWidthDp
    Surface(
        modifier = Modifier
            .padding(12.dp)
            .width((width - 100).dp)
            .fillMaxHeight()
            .background(colorResource(R.color.white), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, colorResource(id = R.color.cyan_fae), RoundedCornerShape(12.dp))
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
        ) {
            Text(
                stringResource(
                id = R.string.ingridients),
                fontSize = 18.sp,
                color = colorResource(R.color.gray),
                modifier = Modifier.padding(4.dp)
            )
            BoxWithCards(bigText = ingredients.map { it.name }.toList())
            Text(
                stringResource(
                id = R.string.tags),
                fontSize = 18.sp,
                color = colorResource(R.color.gray),
                modifier = Modifier.padding(4.dp)
            )
            BoxWithCards(bigText = model.tags ?: listOf())
            Text(
                stringResource(
                id = R.string.energy_value),
                modifier = Modifier.padding(4.dp),
                fontSize = 20.sp,
                color = colorResource(R.color.gray)
            )
            Column {
                for (i in names.indices){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 12.dp)
                    ){
                        Text(
                            text = names[i],
                            fontSize = 18.sp,
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

@Composable
fun RecipeImageContainer(
    model : Recipe,
    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        if (model.image != null){
            DishImage(
                model.image,
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
        ){
            item{
                Text(text = model.name,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(colorResource(id = R.color.white)),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StartCookingContainer(
    model: Recipe,
    navigateToStep: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val steps = model.instructions
    val ingredients = model.ingredients
    Column(
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth()
            .background(colorResource(id = R.color.pale_cyan))
            .border(
                1.dp,
                colorResource(id = R.color.dark_cyan),
            )
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ){
            Text(text = stringResource(R.string.ingr) + " :" + ingredients.size,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Button(
                onClick = { navigateToStep(0) },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                colors = ButtonColors(
                    colorResource(id = R.color.pale_cyan),
                    colorResource(R.color.black), colorResource(R.color.white), colorResource(R.color.black)
                ),
            ) {
                Text(text = stringResource(R.string.start_cooking),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
            Text(text = stringResource(R.string.step) + ": " + steps.size,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AddCollectionButtons(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp))
                .border(
                    2.dp,
                    colorResource(id = R.color.medium_cyan),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Button(onClick = { /*TODO*/ },
                shape = RectangleShape,
                colors = ButtonColors(
                    colorResource(id = R.color.medium_cyan),
                    colorResource(R.color.white),
                    colorResource(R.color.white),
                    colorResource(R.color.black)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
            ) {
                Text(
                    stringResource(R.string.add_to_favourite),
                    fontSize = 12.sp,
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
            Button(onClick = { /*TODO*/ },
                shape = RectangleShape,
                colors = ButtonColors(
                    colorResource(R.color.white), colorResource(R.color.gray), colorResource(
                        R.color.white), colorResource(R.color.black)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
            ) {
                Text(
                    stringResource(R.string.add_to_playlist),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun TextContainer(
    model : Recipe,
    modifier: Modifier = Modifier
){
    val description = model.instructions.map { it.paragraph }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.short_recipe),
            color = colorResource(R.color.gray),
            textAlign = TextAlign.Left,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            userScrollEnabled = true,
            modifier = modifier
                .background(
                    colorResource(id = R.color.white_cyan),
                    shape = RoundedCornerShape(20.dp),
                )
                .clip(shape = RoundedCornerShape(20.dp))
                .height(280.dp)
                .border(
                    1.dp,
                    colorResource(id = R.color.cyan_fae),
                    shape = RoundedCornerShape(20.dp)
                ),
        ){
            items(description.size){ index ->
                Text(
                    text = (index + 1).toString() + ". " + description[index],
                    modifier = Modifier
                        .padding(start = 12.dp, end = 24.dp, top = 8.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun RatingContainer(
    model : Recipe,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(4.dp)
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            RatingBarPres(model.rating)
            Spacer(modifier = Modifier.width(4.dp))
            Text(model.rating.toString(),
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.width(72.dp))
            Image(
                painter = painterResource(R.drawable.povar),
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
            )
            Text(
                text = model.cooked.toString(),
                fontSize = 25.sp
            )
        }
    }
}

@Composable
fun RecipePreview(
    navigateBack : () -> Unit,
    navigateToStep: (Int) -> Unit,
    recipe : Recipe
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    listOf(recipe).forEach { model ->
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.white)),
                drawerContent = {
                    InfoSurface(model)
                },
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
                Box {
                    Column {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            RecipeImageContainer(model, Modifier.weight(5f))
                            RatingContainer(model, Modifier.weight(1f))
                            TextContainer(model, Modifier.weight(6f))
                            AddCollectionButtons(Modifier.weight(1f))
                            StartCookingContainer(
                                model,
                                navigateToStep,
                                Modifier
                                    .weight(1f)
                                    .height(40.dp)
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SquareArrowButton(navigateBack)
                        InfoSquareButton({
                            scope.launch { drawerState.open() }
                        })
                        }
                    }
                }
            }
        }
    }
}
