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
import androidx.hilt.navigation.compose.hiltViewModel
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

fun onSelect(
    list1: MutableList<Int>,
    list2: MutableList<Int>,
    str: String?
): Boolean {
    list1.replaceAll { R.color.lightcyan }
    list2.replaceAll { 40 }
    for (index in 0..4) {
        if (BarScreens[index].route == str) {
            list1[index] = R.color.mediumcyan
            list2[index] = 45
        }
    }
    return true
}

@Composable
fun NavBar(viewModel: NavBarViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val bottomBarState = rememberSaveable {
        mutableStateOf(true)
    }
    val bottomBarColors = rememberSaveable {
        mutableStateOf(MutableList(5) { R.color.lightcyan })
    }
    val bottomBarIcons = rememberSaveable {
        mutableStateOf(MutableList(5) { 40 })
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

    val startDestination = viewModel.getStartDestination()

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
                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        navBackStackEntry?.destination
                        repeat(5) { index ->
                            BottomNavigationItem(
                                icon = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                    ) {
                                        Icon(
                                            painter = painterResource(drawables[index]),
                                            contentDescription = stringResource(names[index]),
                                            tint = colorResource((bottomBarColors.value)[index]),
                                            modifier = Modifier.size(bottomBarIcons.value[index].dp)
                                        )
                                        Text(
                                            text = stringResource(names[index]),
                                            fontSize = 15.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = colorResource((bottomBarColors.value)[index]),
                                            modifier = Modifier
                                        )
                                    }
                                },
                                selectedContentColor = colorResource(id = R.color.mediumcyan),
                                unselectedContentColor = colorResource(id = R.color.lightcyan),
                                selected = onSelect(
                                    bottomBarColors.value,
                                    bottomBarIcons.value,
                                    navController.currentBackStackEntry?.destination?.route
                                ),
                                onClick = {
                                    navController.navigate(BarScreens[index].route) {
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
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.HomeScreen.route) {
                bottomBarState.value = true
                HomeScreen()
            }
            composable(Screen.SearchScreen.route) {
                bottomBarState.value = true
                SearchScreen()
            }
            composable(Screen.CollectionScreen.route) {
                bottomBarState.value = true
                CollectionScreen(
                    { navController.navigate(Screen.NewRecipeScreen.route) },
                    { navController.navigate(Screen.RecipeScreen.route) },
                )
            }
            composable(Screen.InProgressScreen.route) {
                bottomBarState.value = true
                InProgressScreen()
            }
            composable(Screen.ProfileScreen.route) {
                bottomBarState.value = true
                ProfileScreen { navController.navigate(Screen.LoginScreen.route) }
            }
            composable(Screen.LoginScreen.route) {
                bottomBarState.value = false
                LoginScreen(
                    { navController.navigate(Screen.HomeScreen.route) },
                    { navController.navigate(Screen.RegisterScreen.route) }
                )
            }
            composable(Screen.RegisterScreen.route) {
                bottomBarState.value = false
                RegisterScreen(
                    { navController.navigate(Screen.HomeScreen.route) },
                    { navController.navigate(Screen.LoginScreen.route) }
                )
            }
            composable(Screen.NewRecipeScreen.route) {
                bottomBarState.value = true
                NewRecipeScreen()
            }
            composable(Screen.RecipeScreen.route) {
                bottomBarState.value = true
                RecipeScreen {
                    val previous = navController.previousBackStackEntry?.destination?.route
                    if (previous != null) {
                        navController.navigate(previous)
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("HomeScreen")
    data object SearchScreen : Screen("SearchScreen")
    data object CollectionScreen : Screen("CollectionScreen")
    data object InProgressScreen : Screen("inProgressScreen")
    data object ProfileScreen : Screen("ProfileScreen")
    data object LoginScreen : Screen("LoginScreen")
    data object RegisterScreen : Screen("RegisterScreen")
    data object NewRecipeScreen : Screen("NewRecipeScreen")
    data object RecipeScreen : Screen("RecipeScreen")
}
