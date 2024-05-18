package com.vk_edu.feed_and_eat.features.new_recipe.pres

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar


@Composable
fun NewRecipeScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: NewRecipeScreenViewModel = hiltViewModel()
) {
    val imageUri by viewModel.imagePath
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.CollectionScreen.route) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.pale_cyan))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                ImagePickerScreen(imageUri = imageUri, viewModel = viewModel)

                val instructions = listOf(
                    Instruction("123 bla 123", null),
                    Instruction("no no no", listOf(Timer("constant", number = 11)))
                )
                val tags = listOf("Makarony", "Myaso")
                Button(onClick = {
                    viewModel.addNewRecipe(
                        name = "Test 123",
                        instructions = instructions,
                        tags = tags
                    )
                }) {
                    Text("Add new recipe")
                }
            }

        }
    }


}

@Composable
fun ImagePickerScreen(imageUri: Uri?, viewModel: NewRecipeScreenViewModel) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.imageChanged(uri)
        }

    Button(onClick = { launcher.launch("image/*") }) {
        Text(text = "Выбрать изображение")
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUri)
            .build(),
        contentDescription = "",
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
    )
}