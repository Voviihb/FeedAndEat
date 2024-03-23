package com.vk_edu.feed_and_eat.features.navigation.pres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vk_edu.feed_and_eat.R

@Composable
fun NavButton(
    drawable: Int,
    label : String,
    description : String,
    color : Color,
    onClick : () -> Unit = {},
){
    Button(
        onClick = onClick,

        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        colors = ButtonColors(
            Color.Transparent,
            color,
            Color.Gray,
            Color.Black
            ),
        modifier = Modifier
            .padding(vertical = 5.dp)
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ){

            Icon(
                painter = painterResource(id = drawable),
                contentDescription = description,
                tint = color
            )
            Text(
                text = label,
                fontSize = 15.sp,
            )
        }

    }

}

@Composable
fun NavBarScreen(navController: NavController) {

    val drawables = listOf(
        R.drawable.home,
        R.drawable.search,
        R.drawable.collection,
        R.drawable.progress,
        R.drawable.profile,
        )
    val names = listOf(
        R.string.main,
        R.string.search,
        R.string.collection,
        R.string.inProgress,
        R.string.profile,
        )
    val destinations = listOf(
        stringResource(R.string.HomeScreen),
        stringResource(R.string.SearchScreen),
        stringResource(R.string.CollectionScreen),
        stringResource(R.string.inProgressScreen),
        stringResource(R.string.ProfileScreen),
    )
    val navigate = listOf(
        {navController.navigate(destinations[0])},
        {navController.navigate(destinations[1])},
        {navController.navigate(destinations[2])},
        {navController.navigate(destinations[3])},
        {navController.navigate(destinations[4])},
    )
    val current = navController.currentBackStackEntry?.destination?.route
    val colors = MutableList(5){(colorResource(id = R.color.lightcyan))}
    if (current in destinations){
        colors[destinations.indexOf(current)] = colorResource(id = R.color.mediumcyan)
    }else{
        val previous = navController.previousBackStackEntry?.destination?.route
        colors[destinations.indexOf(previous)] = colorResource(id = R.color.mediumcyan)
    }

    LazyRow(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        items(drawables.size){ index ->
                NavButton(
                    drawable = drawables[index],
                    label = stringResource(names[index]),
                    description = stringResource(names[index]),
                    color = colors[index],
                    onClick = navigate[index],
                )
        }
    }
}