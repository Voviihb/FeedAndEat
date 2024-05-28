package com.vk_edu.feed_and_eat.features.recipe.pres.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.CustomSideDrawer
import com.vk_edu.feed_and_eat.common.graphics.DishImage
import com.vk_edu.feed_and_eat.common.graphics.LargeIcon
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.MediumIcon
import com.vk_edu.feed_and_eat.common.graphics.RatingBar
import com.vk_edu.feed_and_eat.common.graphics.RepeatButton
import com.vk_edu.feed_and_eat.common.graphics.SquareArrowButton
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.ui.theme.ExtraLargeText
import com.vk_edu.feed_and_eat.ui.theme.MediumText
import com.vk_edu.feed_and_eat.ui.theme.SmallText

@Composable
fun RecipeImageContainer(
    model : Recipe,
    modifier : Modifier = Modifier
){
    val configuration = LocalConfiguration.current
    val labelHeight = 32
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = (screenWidth.value / 4f * 3f).dp

    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
            .height(screenHeight)
            .width(screenWidth)
    ){
        if (model.image != null){
            DishImage(
                link = model.image,
                modifier = Modifier
            )
        } else {
            LargeIcon(
                painter = painterResource(id = R.drawable.broken_image),
                color = colorResource(id = R.color.gray),
                modifier = Modifier.aspectRatio(4f / 3f)
            )
        }
        Text(text = model.name,
            fontSize = ExtraLargeText,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width((screenWidth.value + 4).dp)
                .height(labelHeight.dp)
                .background(colorResource(id = R.color.transparent_pale_cyan))
                .border(1.dp, colorResource(id = R.color.medium_cyan))
        )
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
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.short_recipe),
            color = colorResource(R.color.gray),
            textAlign = TextAlign.Left,
            fontSize = ExtraLargeText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = modifier
                .background(
                    colorResource(id = R.color.white_cyan),
                    shape = RoundedCornerShape(20.dp),
                )
                .clip(shape = RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .defaultMinSize(minWidth = 20.dp, minHeight = 300.dp)
                .border(
                    1.dp,
                    colorResource(id = R.color.dark_cyan),
                    shape = RoundedCornerShape(20.dp)
                ),
        ){
            repeat(description.size){ index ->
                Text(
                    text = (index + 1).toString() + ". " + description[index],
                    modifier = Modifier
                        .padding(start = 12.dp, end = 24.dp, top = 8.dp),
                    fontSize = MediumText
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
            .padding(top = 4.dp)

    ){
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .padding(start = 12.dp, end = 36.dp)
        ) {
            Row{
                RatingBar(model.rating.toFloat(), modifier = Modifier.height(25.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    model.rating.toString(),
                    fontSize = ExtraLargeText
                )
            }
            Row{
                MediumIcon(
                    painter = painterResource(R.drawable.cooked_icon),
                    color = colorResource(id = R.color.black),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = model.cooked.toString(),
                    fontSize = ExtraLargeText,
                    modifier = Modifier.align(Alignment.Bottom),
                )
            }
        }
    }
}

@Composable
fun InfoSquareButton(
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(colorResource(R.color.transparent)),
        modifier = Modifier
            .height(60.dp)
            .width(60.dp)
            .background(colorResource(R.color.white), shape = RoundedCornerShape(12.dp))
            .clip(shape = RoundedCornerShape(12.dp))
            .border(
                2.dp,
                color = colorResource(id = R.color.dark_cyan),
                shape = RoundedCornerShape(12.dp)
            ),
    ){
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(id = R.string.info),
            tint = colorResource(id = R.color.dark_cyan),
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        )
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

@Composable
fun RecipeInfo(
    recipe: Recipe,
){
    val names = listOf(R.string.calories_data, R.string.fats_data, R.string.proteins_data, R.string.carbons_data, R.string.sugar_data).map{ stringResource( id = it ) }
    val energyData = listOf(
        recipe.nutrients.Calories,
        recipe.nutrients.Sugar,
        recipe.nutrients.Protein,
        recipe.nutrients.Fat,
        recipe.nutrients.Carbohydrates
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ) {
        listOf(
            listOf(stringResource(id = R.string.small_ingredients), recipe.ingredients.size),
            listOf(stringResource(id = R.string.step), recipe.instructions.size)
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
        BoxWithCards(bigText = recipe.ingredients.map { it.name }.toList())
        Text(
            stringResource(
                id = R.string.tags_data),
            fontSize = SmallText,
            color = colorResource(R.color.gray),
        )
        BoxWithCards(bigText = recipe.tags ?: listOf())
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

@Composable
fun RecipePreview(
    navigateBack : () -> Unit,
    navigateToStep: (Int) -> Unit,
    step : Int? = null,
    viewModel: RecipesScreenViewModel,
){
    val recipe = viewModel.recipe.value
    if (step != null){
        if (viewModel.loading.value) {
            LoadingCircular()
        } else {
            if (viewModel.errorMessage.value != null) {
                RepeatButton(onClick = {
                    viewModel.clearError()
                    viewModel.clearCollectionError()
                    viewModel.loadRecipeById(viewModel.recipe.value.id ?: "")
                    viewModel.loadCollections()
                })
            } else {
                navigateToStep(step - 1)
            }
        }
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    drawerState.isAnimationRunning

    CustomSideDrawer(
        direction = LayoutDirection.Rtl,
        drawerState = drawerState,
        hiddenContent = {
            RecipeInfo(recipe)
        },
        actionButton = {onClick -> InfoSquareButton{ onClick() } },
        hiddenContentModifier = Modifier
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
    ) {
        listOf(recipe).forEach { model ->
            Scaffold(
                bottomBar = {
                    BottomBar(
                        navigateToStep,
                        viewModel,
                        recipe,
                        Modifier.height(60.dp)
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) {padding ->
                Box(
                    modifier = Modifier.padding(padding)
                ) {
                    Column {
                        LazyColumn(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            item{
                                RecipeImageContainer(model)
                            }
                            item {
                                RatingContainer(model)
                            }
                            item {
                                TextContainer(model)
                            }
                            item{
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SquareArrowButton(navigateBack)
                    }
                }
            }
        }
    }
}