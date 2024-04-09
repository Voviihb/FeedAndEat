package com.vk_edu.feed_and_eat.features.recipe.pres

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoxofText
import com.vk_edu.feed_and_eat.common.graphics.ExpandableInfo
import com.vk_edu.feed_and_eat.common.graphics.RatingBarPres
import com.vk_edu.feed_and_eat.common.graphics.SquareArrowButton
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.features.recipe.data.models.RecipeDataModel


@Composable
fun InfoSurface(
    surfaceWidth : Int,
    model : RecipeDataModel,
){
    val ingredients = model.ingredients
    val tags = model.tags
    val energyData = model.energyData
    val names = listOf(R.string.calories, R.string.fats, R.string.proteins, R.string.carbons)
    Surface(
        modifier = Modifier
            .width(surfaceWidth.dp)
            .border(
                2.dp,
                colorResource(id = R.color.dark_cyan),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(stringResource(id = R.string.ingridients),
                modifier = Modifier.padding(5.dp)
            )
            BoxofText(bigText = ingredients)
            Text(stringResource(id = R.string.tags),
                modifier = Modifier.padding(5.dp)
            )
            BoxofText(bigText = tags)
            Text(stringResource(id = R.string.energy_value),
                modifier = Modifier.padding(5.dp),
                fontSize = 20.sp,
                color = Color.Gray
            )
            LazyColumn(
                contentPadding = PaddingValues(start = 8.dp,
                                                top = 8.dp,
                                                bottom = 8.dp,
                                                end = 70.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(names.size){ i ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            text = stringResource(id = names[i]),
                            fontSize = 20.sp,
                            color = Color.Gray
                        )
                        Text(text = energyData[i].toString() + " " + stringResource(id = R.string.gramm),
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }
            }
        }
    }
}



@Composable
fun BackButtonContainer(
    model: RecipeDataModel,
    navigateBack : () -> Unit,
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
                ExpandableInfo(width = 350, surface = {
                    InfoSurface(350, model)
                })
            }
        }
    }
}

@Composable
fun RecipeImageContainer(
    model : RecipeDataModel
){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .background(Color.Transparent)
            .heightIn(0.dp, (screenHeight * 0.3).toInt().dp)

    ) {
        Image(
            painter = painterResource(id = model.picture),
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
                Text(text = model.name,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .background(colorResource(id = R.color.pale_cyan)),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StartCookingContainer(
    model: RecipeDataModel
){
    val steps = model.steps
    val ingredients = model.ingredients
    Column(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(colorResource(id = R.color.pale_cyan))
            .border(
                1.dp,
                colorResource(id = R.color.dark_cyan),
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = stringResource(R.string.ingr) + ":" + ingredients.size,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                colors = ButtonColors(
                    colorResource(id = R.color.pale_cyan),
                    Color.Black, Color.White, Color.Black),
            ) {
                Text(text = stringResource(R.string.start_cooking),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
            Text(text = stringResource(R.string.steps) + ":" + steps.size,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun AddCollectionButtons(){
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .border(
                    2.dp,
                    colorResource(id = R.color.medium_cyan),
                    shape = RoundedCornerShape(10.dp)
                )

        ) {
            Button(onClick = { /*TODO*/ },
                shape = RectangleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonColors(
                    colorResource(id = R.color.medium_cyan),
                    Color.White,
                    Color.White,
                    Color.Black),
                modifier = Modifier
                    .weight(1f)

            ) {
                Text(
                    stringResource(R.string.add_to_favourite),
                    maxLines = 1,
                    fontSize = 15.sp
                )
            }
            Button(onClick = { /*TODO*/ },
                shape = RectangleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonColors(Color.White, Color.Gray, Color.White, Color.Black),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    stringResource(R.string.add_to_playlist),
                    maxLines = 1,
                    fontSize = 15.sp,
                )
            }
        }
    }
}

@Composable
fun TextContainer(
    model : RecipeDataModel
){
    val description = model.description
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(horizontal = 15.dp)
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = stringResource(R.string.short_recipe),
            color = Color.Gray,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .background(Color.Transparent),
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .background(
                    colorResource(id = R.color.white_cyan),
                    shape = RoundedCornerShape(20.dp),
                )
                .clip(shape = RoundedCornerShape(20.dp))
                .height(280.dp)
                .border(
                    1.dp,
                    colorResource(id = R.color.dark_cyan),
                    shape = RoundedCornerShape(20.dp)
                ),

            ){
            items(description.size){ index ->
                Text(
                    text = (index + 1).toString() + ". " + description[index],
                    modifier = Modifier
                        .padding(start = 15.dp, end = 25.dp, top = 10.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun RatingContainer(
    model : RecipeDataModel
){
    Column(
        modifier = Modifier
        .height(50.dp)
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            RatingBarPres(model.rating)
            Spacer(modifier = Modifier.width(5.dp))
            Text(model.rating.toString(), modifier = Modifier
                .background(Color.Transparent),
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.width(70.dp))
            Image(
                painter = painterResource(R.drawable.povar),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
            )
            Text(
                text = model.cooked.toString(),
                fontSize = 25.sp
            )
        }
    }
}


@Composable
fun RecipeScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack : () -> Unit
) {
    val viewModel: RecipeScreenViewModel = hiltViewModel()
    viewModel.getRecipe()
    val model = viewModel.recipe.value

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.SearchScreen.route) }
    ) {padding ->
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