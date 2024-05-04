package com.vk_edu.feed_and_eat.features.navigation.pres

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
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
        BottomScreen.CollectionScreen,
        BottomScreen.InProgressScreen,
        BottomScreen.ProfileScreen
    )
    val bottomBarData = MutableList(5){BottomData(R.color.light_cyan, 40)}
    val currentIndex = barScreens.map { it.route }.indexOf(currentDestination)
    if (currentIndex in bottomBarData.indices){
        bottomBarData[currentIndex] = BottomData(R.color.medium_cyan, 45)
    }

    BottomNavigation(
        modifier = Modifier
    ){
        repeat(5) {index ->
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
                            color = colorResource(bottomBarData[index].color),
                            modifier = Modifier
                        )
                    }
                },
                selectedContentColor = colorResource(id = R.color.medium_cyan),
                unselectedContentColor = colorResource(id = R.color.light_cyan),
                selected = true,
                onClick = {
                    navigateToRoute(barScreens[index].route)
                },
                modifier = Modifier
                    .background(Color.White)
                    .padding(vertical = 8.dp),
                )
            }
        }
    }

sealed class Screen(val route: String) {
    data object LoginScreen : Screen("LoginScreen")
    data object RegisterScreen : Screen("RegisterScreen")
    data object NewRecipeScreen : Screen("NewRecipeScreen")
    data object RecipeScreen : Screen("RecipeScreen")
}

sealed class BottomScreen(
    val route: String,
    val drawable : Int,
    val name : Int,
    ) {
    data object HomeScreen : BottomScreen("HomeScreen",  R.drawable.home,  R.string.main)
    data object  SearchScreen : BottomScreen("SearchScreen", R.drawable.search, R.string.search)
    data object CollectionScreen : BottomScreen("CollectionScreen", R.drawable.collection, R.string.collection)
    data object InProgressScreen : BottomScreen("inProgressScreen", R.drawable.progress, R.string.inProgress)
    data object ProfileScreen : BottomScreen("ProfileScreen", R.drawable.profile, R.string.profile)

}

data class BottomData(var color : Int, var size : Int)