package com.vk_edu.feed_and_eat.features.navigation.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk_edu.feed_and_eat.R


@Composable
fun GlobalNavigationBar(
    navigateToRoute : (String) -> Unit,
    currentDestination : String
){
    val barScreens = listOf(
        BottomScreen.HomeScreen,
        BottomScreen.SearchScreen,
        BottomScreen.CollectionOverviewScreen,
        BottomScreen.InProgressScreen,
        BottomScreen.ProfileScreen
    )
    val bottomBarData = MutableList(5) { BottomData(R.color.light_cyan, 40) }
    val currentIndex = barScreens.map { it.route }.indexOf(currentDestination)
    if (currentIndex in bottomBarData.indices) {
        bottomBarData[currentIndex] = BottomData(R.color.medium_cyan, 45)
    }

    BottomNavigation {
        repeat(5) { index ->
            BottomNavigationItem(
                icon = {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                    ){
                        Icon(
                            painter = painterResource(barScreens[index].drawable),
                            contentDescription = stringResource(barScreens[index].name),
                            tint = colorResource(bottomBarData[index].color),
                            modifier = Modifier.size(bottomBarData[index].size.dp)
                        )
                        Text(
                            text = stringResource(barScreens[index].name),
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(bottomBarData[index].color)
                        )
                    }
                },
                selectedContentColor = colorResource(id = R.color.medium_cyan),
                unselectedContentColor = colorResource(id = R.color.light_cyan),
                selected = true,
                modifier = Modifier
                    .background(Color.White)
                    .padding(vertical = 8.dp),
                onClick = {
                    navigateToRoute(barScreens[index].route)
                }
            )
        }
    }
}
