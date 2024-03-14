package com.vk_edu.feed_and_eat.features.recipe.pres

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BackSquareButton
import com.vk_edu.feed_and_eat.common.graphics.InfoSquareButton
import com.vk_edu.feed_and_eat.common.graphics.RatingBarPreview

@Composable
fun BackButtonContainer(){
    //    extra container for back button & info
    Column() {
//        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(modifier = Modifier
            .background(Color.Transparent)
            .height(60.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item {
//                Spacer(Modifier.width(20.dp))
                BackSquareButton()
            }
            item {
                InfoSquareButton()
            }
        }
    }
}

@Composable
fun RecipeNameContainer(
    Name : String,
    PictureHeight : Int
){
    //    container for recipe name
    Column(modifier = Modifier,
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumn(modifier = Modifier
            .height(PictureHeight.dp)
            .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ){
            item{
                Text(text = Name,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .background(Color(red = 0xCF, blue = 0xFF, green = 0xFB, alpha = 0xBF)),
                    textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun StartCookingContainer(
    Ingredients : List<String>,
    Steps : List<String>
){
    //        Info about Recipe + Start cooking
    Column(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .background(Color(red = 0xCF, blue = 0xFF, green = 0xFB))
            .border(
                1.dp,
                Color(red = 0x00, blue = 0xB6, green = 0xBB, alpha = 0xFF),
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Text(text = stringResource(R.string.ingridients) + ":" + Ingredients.size,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                colors = ButtonColors(
                    Color(red = 0xCF, blue = 0xFF, green = 0xFB, alpha = 0xFF),
                    Color.Black, Color.White, Color.Black),
            ) {
                Text(text = stringResource(R.string.start_cooking),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
            Text(text = stringResource(R.string.steps) + ":" + Steps.size,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ButtonsContainer(){
//        Buttons add to collection
    Column(
        modifier = Modifier
            .padding(15.dp)
//                .clip(shape = RoundedCornerShape(10.dp))
//                .background(Color.Yellow, shape = RoundedCornerShape(10.dp))
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
                .border(
                    2.dp,
                    Color(red = 0x08, blue = 0xE8, green = 0xDF, alpha = 0xFF),
                    shape = RoundedCornerShape(10.dp)
                )

        ) {
            Button(onClick = { /*TODO*/ },
                shape = RectangleShape,
                colors = ButtonColors(Color(red = 0x08, blue = 0xE8, green = 0xDF, alpha = 0xFF), Color.White, Color.White, Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
            ) {
                Text(
                    stringResource(R.string.add_to_favourite),
                    fontSize = 12.sp
                )
            }
            Button(onClick = { /*TODO*/ },
                shape = RectangleShape,
                colors = ButtonColors(Color.White, Color.Gray, Color.White, Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
            ) {
                Text(
                    stringResource(R.string.add_to_playlist),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TextContainer(                          
    Description : List<String>,
){
    //Container which represents recipe description
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
                    Color(red = 0xE4, blue = 0xFE, green = 0xFA, alpha = 0xFF),
                    shape = RoundedCornerShape(20.dp),
                )
                .clip(shape = RoundedCornerShape(20.dp))
                .height(280.dp)
                .border(
                    1.dp,
                    Color(red = 0x00, blue = 0xB6, green = 0xBB, alpha = 0xFF),
                    shape = RoundedCornerShape(20.dp)
                ),

            ){
            for (i in 0..(Description.size - 1)){
                item {
                    Text(
                        text = (i + 1).toString() + ". " + Description[i],
                        modifier = Modifier
                            .padding(start = 15.dp, end = 25.dp, top = 10.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RatingContainer(                          
    Rating : Double,
    Cooked : Int,
){
    Column(modifier = Modifier
        .height(50.dp)
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            RatingBarPreview(Rating)
            Spacer(modifier = Modifier.width(5.dp))
            Text(Rating.toString(), modifier = Modifier
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
                text = Cooked.toString(),
                fontSize = 25.sp
            )
        }
    }
}


@Composable
fun RecipePres(
    Picture : Int,
    Rating : Double,
    Cooked : Int,
    Description : List<String>,
    InFavor : Boolean,
    Name : String,
    Ingredients : List<String>,
    Steps : List<String>,
    PictureHeight : Int
    ) {
//    global container
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(red = 0xF0, green = 0xF0, blue = 0xF0))
    ) {
        Image(
            painter = painterResource(id = Picture),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(PictureHeight.dp)
        )
        RatingContainer(Rating = Rating, Cooked = Cooked)
        TextContainer(Description = Description)
        ButtonsContainer()
        StartCookingContainer(Ingredients = Ingredients, Steps = Steps)

    }//end of global container
    RecipeNameContainer(Name = Name, PictureHeight = PictureHeight)
    BackButtonContainer()
}