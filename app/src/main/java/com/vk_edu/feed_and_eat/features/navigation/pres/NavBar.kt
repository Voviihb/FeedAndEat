package com.vk_edu.feed_and_eat.features.navigation.pres


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
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

val BarScreens = listOf(
    Screen.HomeScreen,
    Screen.SearchScreen,
    Screen.CollectionScreen,
    Screen.InProgressScreen,
    Screen.ProfileScreen
)

@Composable
fun NavBar(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val bottomBarState = rememberSaveable {
        mutableStateOf(true)
    }

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

    Scaffold(
        modifier = Modifier
            .background(Color.White)
        ,
         bottomBar = {
             AnimatedVisibility(
                 visible = bottomBarState.value,
                 enter = slideInVertically(initialOffsetY = { it }),
                 exit = slideOutVertically(targetOffsetY = { it }),
                 content = {
            BottomNavigation(
                modifier = Modifier
                .padding(vertical = 4.dp)
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                repeat(5) {index ->
                    BottomNavigationItem(
                        icon = {
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                            ){
                                Icon(
                                    painter = painterResource(drawables[index]),
                                    contentDescription = stringResource(names[index]),
                                    tint = colorResource(R.color.lightcyan)
                                )
                                Text(
                                    text = stringResource(names[index]),
                                    fontSize = 15.sp,
                                    color = colorResource(R.color.lightcyan),
                                )
                            }
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == BarScreens[index].route } == true,
                        onClick = {
                            navController.navigate(BarScreens[index].route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
            startDestination = Screen.HomeScreen.route,
            modifier = Modifier.padding(padding)){
            composable(Screen.HomeScreen.route){
                bottomBarState.value = true
                HomeScreen()
            }
            composable(Screen.SearchScreen.route){
                bottomBarState.value = true
                SearchScreen()
            }
            composable(Screen.CollectionScreen.route){
                bottomBarState.value = true
                CollectionScreen(
                    { navController.navigate(Screen.NewRecipeScreen.route) },
                    { navController.navigate(Screen.RecipeScreen.route) },
                    )
            }
            composable(Screen.InProgressScreen.route){
                bottomBarState.value = true
                InProgressScreen()
            }
            composable(Screen.ProfileScreen.route){
                bottomBarState.value = true
                ProfileScreen { navController.navigate(Screen.LoginScreen.route) }
            }
            composable(Screen.LoginScreen.route){
                bottomBarState.value = false
                LoginScreen(
                    context,
                    { navController.navigate(Screen.HomeScreen.route) },
                    { navController.navigate(Screen.RegisterScreen.route) }
                )
            }
            composable(Screen.RegisterScreen.route){
                bottomBarState.value = false
                RegisterScreen(
                    context,
                    { navController.navigate(Screen.HomeScreen.route) },
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
    data object HomeScreen : Screen("HomeScreen")
    data object  SearchScreen : Screen("SearchScreen")
    data object CollectionScreen : Screen("CollectionScreen")
    data object InProgressScreen : Screen("inProgressScreen")
    data object ProfileScreen : Screen("ProfileScreen")
    data object LoginScreen : Screen("LoginScreen")
    data object RegisterScreen : Screen("RegisterScreen")
    data object NewRecipeScreen : Screen("NewRecipeScreen")
    data object RecipeScreen : Screen("RecipeScreen")
}
