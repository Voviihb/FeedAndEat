package com.vk_edu.feed_and_eat.features.navigation.pres


//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.collection.pres.CollectionScreen
import com.vk_edu.feed_and_eat.features.inprogress.InProgressScreen
import com.vk_edu.feed_and_eat.features.login.pres.LoginScreen
import com.vk_edu.feed_and_eat.features.login.pres.RegisterScreen
import com.vk_edu.feed_and_eat.features.main.pres.HomeScreen
import com.vk_edu.feed_and_eat.features.newrecipe.pres.NewRecipeScreen
import com.vk_edu.feed_and_eat.features.profile.pres.ProfileScreen
import com.vk_edu.feed_and_eat.features.recipe.pres.RecipeScreen
import com.vk_edu.feed_and_eat.features.search.pres.SearchScreen


@Composable
fun GlobalNavigation(){
    val barScreens = listOf(
        BottomScreen.HomeScreen,
        BottomScreen.SearchScreen,
        BottomScreen.CollectionScreen,
        BottomScreen.InProgressScreen,
        BottomScreen.ProfileScreen
    )
    val navController = rememberNavController()
    val context = LocalContext.current
    val bottomBarState = rememberSaveable {
        mutableStateOf(true)
    }
    val bottomBarData = rememberSaveable {
        mutableStateOf(MutableList(5){BottomData(R.color.lightcyan, 40)})
    }
    bottomBarData.value[0] = BottomData(R.color.mediumcyan, 45)

    Scaffold(
        modifier = Modifier
            .background(Color.White),
         bottomBar = {
             AnimatedVisibility(
                 visible = bottomBarState.value,
                 enter = slideInVertically(initialOffsetY = { it }),
                 exit = slideOutVertically(targetOffsetY = { it }),
                 content = {
            BottomNavigation(
                modifier = Modifier
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                navBackStackEntry?.destination
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
                                    tint = colorResource((bottomBarData.value)[index].color),
                                    modifier = Modifier.size(bottomBarData.value[index].size.dp)
                                )
                                Text(
                                    text = stringResource(barScreens[index].name),
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = colorResource((bottomBarData.value)[index].color),
                                    modifier = Modifier
                                )
                            }
                        },
                        selectedContentColor = colorResource(id = R.color.mediumcyan),
                        unselectedContentColor = colorResource(id = R.color.lightcyan),
                        selected = true,
                        onClick = {
                            navController.navigate(barScreens[index].route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            val str = navController.currentBackStackEntry?.destination?.route
                            if (str in (barScreens.map { it.route })){
                                bottomBarData.value.replaceAll { BottomData(R.color.lightcyan, 40) }
                                for (i in 0..4){
                                    if (barScreens[i].route == str){
                                        bottomBarData.value[i].color = R.color.mediumcyan
                                        bottomBarData.value[i].size = 45
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .background(Color.White)
                            .padding(vertical = 8.dp),
                        )
                    }
                }
            })
        }
        ) {padding ->
        NavHost(
            navController = navController,
            startDestination = BottomScreen.HomeScreen.route,
            modifier = Modifier.padding(padding)){
            composable(BottomScreen.HomeScreen.route){
                bottomBarState.value = true
                HomeScreen()
            }
            composable(BottomScreen.SearchScreen.route){
                bottomBarState.value = true
                SearchScreen(
                    { navController.navigate(Screen.NewRecipeScreen.route) },
                    { navController.navigate(Screen.RecipeScreen.route) },
                )
            }
            composable(BottomScreen.CollectionScreen.route){
                bottomBarState.value = true
                CollectionScreen()
            }
            composable(BottomScreen.InProgressScreen.route){
                bottomBarState.value = true
                InProgressScreen()
            }
            composable(BottomScreen.ProfileScreen.route){
                bottomBarState.value = true
                ProfileScreen { navController.navigate(Screen.LoginScreen.route) }
            }
            composable(Screen.LoginScreen.route){
                bottomBarState.value = false
                LoginScreen(
                    context,
                    { navController.navigate(BottomScreen.HomeScreen.route) },
                    { navController.navigate(Screen.RegisterScreen.route) }
                )
            }
            composable(Screen.RegisterScreen.route){
                bottomBarState.value = false
                RegisterScreen(
                    context,
                    { navController.navigate(BottomScreen.HomeScreen.route) },
                    { navController.navigate(Screen.LoginScreen.route) }
                )
            }
            composable(Screen.NewRecipeScreen.route){
                bottomBarState.value = true
                NewRecipeScreen()
            }
            composable(Screen.RecipeScreen.route){
                bottomBarState.value = true
                RecipeScreen {
                    val previous = navController.previousBackStackEntry?.destination?.route
                    if (previous != null){
                        navController.navigate(previous)
                    }
                }
            }
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