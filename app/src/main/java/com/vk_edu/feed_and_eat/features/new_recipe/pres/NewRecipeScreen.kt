package com.vk_edu.feed_and_eat.features.new_recipe.pres

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.LightText
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
            modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.pale_cyan))
        ) {
            CreateInstructions(viewModel)
        }
    }
}

@Composable
fun CreateInstructions(viewModel: NewRecipeScreenViewModel, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            LightText(text = "Recipe name", fontSize = LargeText, modifier = Modifier.weight(1f))
            OutlinedTextField(
                value = viewModel.name.value,
                textStyle = TextStyle(fontSize = LargeText, color = colorResource(R.color.black)),
                placeholder = { LightText(text = "Enter name", fontSize = LargeText) },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white_cyan),
                    focusedContainerColor = colorResource(R.color.white_cyan),
                    errorContainerColor = colorResource(R.color.white_cyan),
                    focusedIndicatorColor = colorResource(R.color.medium_cyan),
                    unfocusedIndicatorColor = colorResource(R.color.medium_cyan),
                    disabledIndicatorColor = colorResource(R.color.medium_cyan),
                    errorIndicatorColor = colorResource(R.color.medium_cyan),
                    focusedTextColor = colorResource(R.color.black),
                    unfocusedTextColor = colorResource(R.color.black),
                    disabledTextColor = colorResource(R.color.black),
                    cursorColor = colorResource(R.color.black),
                    errorCursorColor = colorResource(R.color.black)
                ),
                modifier = Modifier
                    .height(70.dp)
                    .weight(1f),
                onValueChange = { value ->
                    viewModel.changeName(value)
                }
            )
        }

        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                viewModel.changeImagePath(uri)
            }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            LightText(text = "Dish photo", fontSize = LargeText, modifier = Modifier.weight(1f))
            OutlinedButton(
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    colorResource(R.color.white_cyan), colorResource(R.color.white_cyan),
                    colorResource(R.color.white_cyan), colorResource(R.color.white_cyan)
                ),
                border = BorderStroke(1.dp, colorResource(R.color.medium_cyan)),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(70.dp)
                    .weight(1f),
                onClick = {
                    launcher.launch("image/*")
                }
            ) {
                if (viewModel.imagePath.value == null)
                    Text(
                        text = "Load recipe",
                        color = colorResource(R.color.dark_cyan),
                        fontSize = LargeText
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

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(viewModel.instructions.value) { instruction ->
                Row {
                    Text(instruction.paragraph)
                    if (instruction.timers != null) {
                        Text(instruction.timers.toString())
                    }
                }
            }
        }

        if (viewModel.newInstruction.value == null)
            OutlinedButton(
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    colorResource(R.color.white_cyan), colorResource(R.color.white_cyan),
                    colorResource(R.color.white_cyan), colorResource(R.color.white_cyan)
                ),
                border = BorderStroke(1.dp, colorResource(R.color.medium_cyan)),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth(0.5f),
                onClick = {
                    viewModel.createInstruction()
                }
            ) {
                Text(
                    text = "Add instruction",
                    color = colorResource(R.color.dark_cyan),
                    fontSize = LargeText
                )
            }
        else
            Column {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LightText(text = "Text paragraph", fontSize = MediumText)

                    OutlinedTextField(
                        value = viewModel.newInstruction.value?.paragraph ?: "",
                        textStyle = TextStyle(
                            fontSize = MediumText,
                            color = colorResource(R.color.black)
                        ),
                        placeholder = {
                            LightText(
                                text = "Enter paragraph",
                                fontSize = MediumText
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = colorResource(R.color.white_cyan),
                            focusedContainerColor = colorResource(R.color.white_cyan),
                            errorContainerColor = colorResource(R.color.white_cyan),
                            focusedIndicatorColor = colorResource(R.color.medium_cyan),
                            unfocusedIndicatorColor = colorResource(R.color.medium_cyan),
                            disabledIndicatorColor = colorResource(R.color.medium_cyan),
                            errorIndicatorColor = colorResource(R.color.medium_cyan),
                            focusedTextColor = colorResource(R.color.black),
                            unfocusedTextColor = colorResource(R.color.black),
                            disabledTextColor = colorResource(R.color.black),
                            cursorColor = colorResource(R.color.black),
                            errorCursorColor = colorResource(R.color.black)
                        ),
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        onValueChange = { value ->
                            viewModel.changeParagraph(value)
                        }
                    )

                    if (viewModel.newTimer.value == null) {
                        OutlinedButton(
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonColors(
                                colorResource(R.color.white_cyan), colorResource(R.color.white_cyan),
                                colorResource(R.color.white_cyan), colorResource(R.color.white_cyan)
                            ),
                            border = BorderStroke(1.dp, colorResource(R.color.medium_cyan)),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .height(70.dp)
                                .fillMaxWidth(0.5f),
                            onClick = {
                                viewModel.createTimer()
                            }
                        ) {
                            Text(
                                text = "Add new timer",
                                color = colorResource(R.color.dark_cyan),
                                fontSize = LargeText
                            )
                        }
                    }
                    else {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Enter timer values")
                            Row() {
                                OutlinedTextField(
                                    value = (viewModel.newTimer.value?.lowerLimit ?: "0").toString(),
                                    textStyle = TextStyle(fontSize = MediumText, color = colorResource(R.color.black)),
                                    placeholder = { LightText(text = "Lower limit", fontSize = LargeText) },
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = colorResource(R.color.white_cyan),
                                        focusedContainerColor = colorResource(R.color.white_cyan),
                                        errorContainerColor = colorResource(R.color.white_cyan),
                                        focusedIndicatorColor = colorResource(R.color.medium_cyan),
                                        unfocusedIndicatorColor = colorResource(R.color.medium_cyan),
                                        disabledIndicatorColor = colorResource(R.color.medium_cyan),
                                        errorIndicatorColor = colorResource(R.color.medium_cyan),
                                        focusedTextColor = colorResource(R.color.black),
                                        unfocusedTextColor = colorResource(R.color.black),
                                        disabledTextColor = colorResource(R.color.black),
                                        cursorColor = colorResource(R.color.black),
                                        errorCursorColor = colorResource(R.color.black)
                                    ),
                                    modifier = Modifier
                                        .height(50.dp)
                                        .weight(1f),
                                    onValueChange = { value ->
                                        viewModel.lowerLimitChanged(value)
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                OutlinedTextField(
                                    value = (viewModel.newTimer.value?.upperLimit ?: "0").toString(),
                                    textStyle = TextStyle(fontSize = MediumText, color = colorResource(R.color.black)),
                                    placeholder = { LightText(text = "Upper limit", fontSize = LargeText) },
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = colorResource(R.color.white_cyan),
                                        focusedContainerColor = colorResource(R.color.white_cyan),
                                        errorContainerColor = colorResource(R.color.white_cyan),
                                        focusedIndicatorColor = colorResource(R.color.medium_cyan),
                                        unfocusedIndicatorColor = colorResource(R.color.medium_cyan),
                                        disabledIndicatorColor = colorResource(R.color.medium_cyan),
                                        errorIndicatorColor = colorResource(R.color.medium_cyan),
                                        focusedTextColor = colorResource(R.color.black),
                                        unfocusedTextColor = colorResource(R.color.black),
                                        disabledTextColor = colorResource(R.color.black),
                                        cursorColor = colorResource(R.color.black),
                                        errorCursorColor = colorResource(R.color.black)
                                    ),
                                    modifier = Modifier
                                        .height(50.dp)
                                        .weight(1f),
                                    onValueChange = { value ->
                                        viewModel.upperLimitChanged(value)
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        }
                    }


                    OutlinedButton(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonColors(
                            colorResource(R.color.white_cyan), colorResource(R.color.white_cyan),
                            colorResource(R.color.white_cyan), colorResource(R.color.white_cyan)
                        ),
                        border = BorderStroke(2.dp, colorResource(R.color.medium_cyan)),
                        contentPadding = PaddingValues(36.dp, 16.dp),
                        onClick = {
                            viewModel.addInstruction()
                        }
                    ) {
                        Text(
                            text = "Save paragraph",
                            color = colorResource(R.color.dark_cyan),
                            fontSize = MediumText
                        )
                    }

                }
            }
    }
}
