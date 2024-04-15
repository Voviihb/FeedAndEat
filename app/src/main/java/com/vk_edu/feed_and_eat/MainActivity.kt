package com.vk_edu.feed_and_eat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vk_edu.feed_and_eat.features.dishes.pres.RecipesScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val navController = rememberNavController()
//            Column(
//                verticalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxSize()
//            ){
//                NavGraph(navController = navController, context = LocalContext.current)
//            }
            RecipesScreen()
        }
    }
}