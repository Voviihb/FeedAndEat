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
import com.vk_edu.feed_and_eat.features.recipe.pres.RecipeScreenViewModel
import com.vk_edu.feed_and_eat.ui.theme.FeedAndEatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeedAndEatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val description = listOf("Рецепт рецепт рецепт рецепт рецепт рецепт рецепт рецепт",
                            "Рецепт рецепт рецепт",
                            "Рецепт рецепт рецепт рецепт рецепт рецепт рецепт",
                            "Рецепт рецепт рецепт рецепт рецепт рецепт рецепт рецепт")
                    val ingredients = listOf("ingred 1", "ingred 2")
                    val steps = listOf("step 1", "step 2")

                    RecipeScreenViewModel(
                        Picture = R.drawable.recipe,
                        Cooked = 1234,
                        Rating = 3.3,
                        Description = description,
                        InFavor = false,
                        Name = "Бараньи ребрышки по-узбекски",
                        Steps = steps,
                        Ingredients = ingredients
                        )
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