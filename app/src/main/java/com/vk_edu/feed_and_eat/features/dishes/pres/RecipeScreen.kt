package com.vk_edu.feed_and_eat.features.dishes.pres

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoxWithCards
import com.vk_edu.feed_and_eat.common.graphics.ExpandableInfo
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.RatingBarPres
import com.vk_edu.feed_and_eat.common.graphics.SquareArrowButton
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar


@Composable
fun InfoSurface(
    surfaceWidth : Int,
    model : Recipe,
){
    val ingredients = model.ingredients
    val energyData = listOf(
        model.nutrients?.calories,
        model.nutrients?.sugar,
        model.nutrients?.protein,
        model.nutrients?.fat,
        model.nutrients?.carbohydrates
    )
    val names = listOf(R.string.calories, R.string.fats, R.string.proteins, R.string.carbons, R.string.sugar)
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.Red)
            .width(surfaceWidth.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(stringResource(
                id = R.string.ingridients),
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(4.dp)
            )
            BoxWithCards(bigText = ingredients?.map { it.name }?.toList() ?: listOf())
            Text(stringResource(
                id = R.string.tags),
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(4.dp)
            )
            BoxWithCards(bigText = model.tags ?: listOf())
            Text(stringResource(
                id = R.string.energy_value),
                modifier = Modifier.padding(4.dp),
                fontSize = 20.sp,
                color = Color.Gray
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
                            text = stringResource(id = names[i]),
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                        Text(text = (energyData[i] ?: "?").toString() + " " + stringResource(id = R.string.gramm))
                    }

                }
            }
        }
    }
}



@Composable
fun BackButtonContainer(
    model: Recipe,
    navigateBack : () -> Unit
){
    Column {
        LazyRow(modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            item {
                SquareArrowButton(navigateBack)
            }
            item {
                ExpandableInfo(width = 352, surface = {
                    InfoSurface(352, model)
                })
            }
        }
    }
}

@Composable
fun RecipeImageContainer(
    model : Recipe
){
    Column(
        modifier = Modifier
            .heightIn(0.dp, 280.dp)
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Top
    ) {
        AsyncImage(
            model = model.image,
            contentDescription = stringResource(id = R.string.image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )
        LazyColumn(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
        ){
            item{
                Text(text = model.name!!,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(colorResource(id = R.color.lightwhite)),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StartCookingContainer(
    model: Recipe
){
    val steps = model.instructions
    val ingredients = model.ingredients
    Column(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .background(colorResource(id = R.color.lightwhite))
            .border(
                1.dp,
                colorResource(id = R.color.dark_cyan),
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Text(text = stringResource(R.string.ingridients) + " :" + ingredients?.size,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                colors = ButtonColors(
                    colorResource(id = R.color.lightwhite),
                    Color.Black, Color.White, Color.Black),
            ) {
                Text(text = stringResource(R.string.start_cooking),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
            Text(text = stringResource(R.string.steps) + ": " + steps?.size,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AddCollectionButtons(){
    Column(
        modifier = Modifier
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
                    Color.White,
                    Color.White,
                    Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
            ) {
                Text(
                    stringResource(R.string.add_to_favourite),
                    fontSize = 12.sp,
                    overflow = TextOverflow.Visible,
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    maxLines = 1
                )
            }
            Button(onClick = { /*TODO*/ },
                shape = RectangleShape,
                colors = ButtonColors(Color.White, Color.Gray, Color.White, Color.Black),
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
    model : Recipe
){
    val description = model.instructions?.map { it.paragraph }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(horizontal = 12.dp)
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.short_recipe),
            color = Color.Gray,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .background(Color.Transparent),
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            userScrollEnabled = true,
            modifier = Modifier
                .background(
                    colorResource(id = R.color.white_cyan),
                    shape = RoundedCornerShape(20.dp),
                )
                .clip(shape = RoundedCornerShape(20.dp))
                .height(280.dp)
                .border(
                    1.dp,
                    colorResource(id = R.color.turqoise),
                    shape = RoundedCornerShape(20.dp)
                ),
            ){
            if (description != null){
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
}

@Composable
fun RatingContainer(
    model : Recipe
){
    Column(
        modifier = Modifier
            .height(52.dp)
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            RatingBarPres(model.rating ?: 0.0)
            Spacer(modifier = Modifier.width(4.dp))
            Text(model.rating.toString(), modifier = Modifier
                .background(Color.Transparent),
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
fun RepeatButton(
    onClick : () -> Unit,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonColors(
                colorResource(id = R.color.pale_cyan),
                Color.Black, Color.White, Color.Black),
            modifier = Modifier
                .background(
                    color = colorResource(id = R.color.pale_cyan),
                    RoundedCornerShape(4.dp)
                )
                .border(
                    2.dp,
                    colorResource(id = R.color.turqoise),
                    RoundedCornerShape(4.dp)
                )
                .height(40.dp)
                .width(72.dp)
        ) {
            Text(
                text = stringResource(id = R.string.repeat),
                fontSize = 12.sp,
                overflow = TextOverflow.Visible,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )
        }
    }
}

@Composable
fun RecipeScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack : () -> Unit,
    viewModel: RecipesScreenViewModel = hiltViewModel()
) {
    val id = "083KzNCvzuf4CIKoeJFB"
    viewModel.loadRecipeById(id)
    val recipeList by viewModel.recipesList

    val reload = {
        viewModel.loadRecipeById(id)
        viewModel.clearError()
    }

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.SearchScreen.route) },
    ) {padding ->
        if (viewModel.loading.value){
            LoadingCircular(padding)
        } else {
            if (viewModel.errorMessage.value != null){
                RepeatButton(reload)
            } else {
                recipeList.forEach{model ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.white))
                        .padding(padding)
                    ) {
                        Box{
                            Column{
                                RecipeImageContainer(model)
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    RatingContainer(model)
                                    TextContainer(model)
                                    AddCollectionButtons()
                                    StartCookingContainer(model)
                                }
                            }
                            BackButtonContainer(model, navigateBack)
                        }
                    }
                }
            }
        }
    }
}