package com.vk_edu.feed_and_eat.features.new_recipe.pres

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.common.graphics.OutlinedTextInput
import com.vk_edu.feed_and_eat.common.graphics.OutlinedThemeButton
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.ui.theme.LargeText
import com.vk_edu.feed_and_eat.ui.theme.MediumText


@Composable
fun NewRecipeScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: NewRecipeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.CollectionScreen.route) }
    ) { padding ->
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.white))
        ) {
            FirstPart(viewModel)
            SecondPart(viewModel, navigateBack)
        }
    }
}

@Composable
fun SecondPart(viewModel: NewRecipeScreenViewModel, navigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                if (viewModel.currentStepIndex.value > 0)
                    OutlinedThemeButton(
                        text = "Go to previous",
                        fontSize = MediumText,
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 8.dp, 0.dp),
                        onClick = {
                            viewModel.goToPreviousStep()
                        }
                    )
            }
            OutlinedThemeButton(
                text = "Add new step",
                fontSize = MediumText,
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    .padding(8.dp, 0.dp),
                onClick = {
                    viewModel.createNewStep()
                }
            )
            Box(modifier = Modifier.weight(1f)) {
                if (viewModel.currentStepIndex.value < viewModel.steps.value.size - 1)
                    OutlinedThemeButton(
                        text = "Go to next",
                        fontSize = MediumText,
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                            .padding(8.dp, 0.dp, 0.dp, 0.dp),
                        onClick = {
                            viewModel.goToNextStep()
                        }
                    )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedThemeButton(
                text = "Save recipe",
                fontSize = LargeText,
                modifier = Modifier
                    .height(64.dp)
                    .weight(2f),
                onClick = {
                    viewModel.saveRecipe()
                    navigateBack()
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedThemeButton(
                text = "Cancel creation",
                fontSize = LargeText,
                modifier = Modifier
                    .height(64.dp)
                    .weight(2f),
                onClick = navigateBack
            )
        }
    }
}

@Composable
fun FirstPart(viewModel: NewRecipeScreenViewModel, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp, 12.dp, 12.dp, 164.dp)
    ) {
        OutlinedTextInput(
            text = viewModel.name.value,
            fontSize = LargeText,
            placeholderText = "Recipe name",
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(),
            onValueChange = { value ->
                viewModel.changeName(value)
            }
        )

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            viewModel.changeImagePath(uri)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            LightText(
                text = if (viewModel.imagePath.value == null)
                    "Load dish photo:"
                else
                    "Load new dish photo:",
                fontSize = MediumText,
                modifier = Modifier
                    .height(64.dp)
                    .weight(1f)
            )
            OutlinedButton(
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    colorResource(R.color.white_cyan), colorResource(R.color.white_cyan),
                    colorResource(R.color.white_cyan), colorResource(R.color.white_cyan)
                ),
                border = BorderStroke(1.dp, colorResource(R.color.medium_cyan)),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(64.dp)
                    .weight(1f),
                onClick = {
                    launcher.launch("image/*")
                }
            ) {
                if (viewModel.imagePath.value == null)
                    Text(
                        text = "Click to load",
                        color = colorResource(R.color.dark_cyan),
                        fontSize = MediumText
                    )
                else
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(viewModel.imagePath.value)
                            .build(),
                        contentDescription = "dish image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white), RoundedCornerShape(12.dp))
                .border(2.dp, colorResource(R.color.dark_cyan), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            OutlinedTextInput(
                text = viewModel.currentStep.value.paragraph,
                fontSize = MediumText,
                placeholderText = "Enter instruction of step",
                singleLine = false,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                onValueChange = { value ->
                    viewModel.changeInstruction(value)
                }
            )
        }
    }
}
