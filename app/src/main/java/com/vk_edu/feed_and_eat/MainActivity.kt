package com.vk_edu.feed_and_eat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.vk_edu.feed_and_eat.features.recipe.data.RecipeRepo
import com.vk_edu.feed_and_eat.ui.theme.FeedAndEatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeedAndEatTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Text("klgmbf", modifier = Modifier
//                        .height(80.dp)
//
//                        .bottomBorder(5.dp, Color.Red)
//                        .background(Color.Green)
//                    )
                    val recipeRepo = RecipeRepo()
                    recipeRepo.RecipeData()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FeedAndEatTheme {
        Greeting("Android")
    }
}