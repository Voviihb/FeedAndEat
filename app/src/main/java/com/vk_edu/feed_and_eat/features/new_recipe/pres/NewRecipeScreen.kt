package com.vk_edu.feed_and_eat.features.new_recipe.pres

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoldText
import com.vk_edu.feed_and_eat.common.graphics.DarkText
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.common.graphics.OutlinedTextInput
import com.vk_edu.feed_and_eat.common.graphics.OutlinedThemeButton
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.ui.theme.ExtraSmallText
import com.vk_edu.feed_and_eat.features.collection.pres.CollectionRoutes
import com.vk_edu.feed_and_eat.ui.theme.MediumText
import com.vk_edu.feed_and_eat.ui.theme.SmallText


@Composable
fun NewRecipeScreen(
    navigateToRoute : (String) -> Unit,
    navigateBack: () -> Unit,
    collectionId: String,
    navigateToCollection: (String) -> Unit,
    viewModel: NewRecipeScreenViewModel = hiltViewModel()
) {
    viewModel.setCollectionId(collectionId)
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .background(colorResource(R.color.white))
    ) {
        MainPart(viewModel)
        ButtonsBlock(viewModel)
        WindowDialog(
            viewModel,
            navigateToCollection,
            collectionId
        )
        WindowCancelDialog(
            viewModel,
            navigateToCollection,
            collectionId
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPart(viewModel: NewRecipeScreenViewModel, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp, 12.dp, 12.dp, 116.dp)
    ) {
        OutlinedTextInput(
            text = viewModel.name.value,
            fontSize = MediumText,
            placeholderText = "Recipe name",
            modifier = Modifier
                .height(56.dp)
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
                modifier = Modifier.weight(1f)
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
                    .height(56.dp)
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
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white), RoundedCornerShape(12.dp))
                .border(2.dp, colorResource(R.color.dark_cyan), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ) {
            OutlinedTextField(
                value = viewModel.currentStep.value.paragraph,
                textStyle = TextStyle(fontSize = MediumText, color = colorResource(R.color.black)),
                placeholder = {
                    LightText(
                        text = "Enter instruction of step",
                        fontSize = MediumText
                    )
                },
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
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                onValueChange = { value ->
                    viewModel.changeInstruction(value)
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.currentStep.value.timers?.size ?: 0) { index ->
                    val timerState = viewModel.currentStep.value.timers?.get(index)
                    if (timerState != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(
                                    if (index % 2 == 0)
                                        colorResource(R.color.light_cyan)
                                    else
                                        colorResource(R.color.white)
                                )
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                RadioButton(
                                    selected = timerState.type == TimerType.CONSTANT.str,
                                    colors = RadioButtonColors(
                                        colorResource(R.color.black), colorResource(R.color.black),
                                        colorResource(R.color.black), colorResource(R.color.black)
                                    ),
                                    modifier = Modifier.size(20.dp),
                                    onClick = { viewModel.changeTimerType(index) }
                                )
                                ClickableText(
                                    text = AnnotatedString("Const"),
                                    style = TextStyle(
                                        fontSize = ExtraSmallText,
                                        color = colorResource(R.color.black),
                                        fontWeight = if (timerState.type == TimerType.CONSTANT.str) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    onClick = { viewModel.changeTimerType(index) }
                                )
                            }

                            if (timerState.type == TimerType.CONSTANT.str) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    var hours by remember { mutableStateOf("") }
                                    var minutes by remember { mutableStateOf("") }


                                    TextField(
                                        value = hours,
                                        textStyle = TextStyle(
                                            fontSize = ExtraSmallText,
                                            color = colorResource(R.color.black)
                                        ),
                                        placeholder = {
                                            LightText(
                                                text = "h",
                                                fontSize = ExtraSmallText,
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            errorContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent,
                                            errorIndicatorColor = colorResource(R.color.red),
                                            focusedTextColor = colorResource(R.color.black),
                                            unfocusedTextColor = colorResource(R.color.black),
                                            disabledTextColor = colorResource(R.color.black),
                                            cursorColor = colorResource(R.color.black),
                                            errorCursorColor = colorResource(R.color.red)
                                        ),
                                        modifier = Modifier
                                            .requiredHeight(64.dp)
                                            .requiredWidth(52.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onValueChange = { value ->
                                            hours = value
                                            viewModel.changeTimerNum(
                                                index,
                                                hours,
                                                minutes
                                            )
                                        }
                                    )
                                    BoldText(text = ":", fontSize = ExtraSmallText)
                                    TextField(
                                        value = minutes,
                                        textStyle = TextStyle(
                                            fontSize = ExtraSmallText,
                                            color = colorResource(R.color.black)
                                        ),
                                        placeholder = {
                                            LightText(
                                                text = "m",
                                                fontSize = ExtraSmallText,
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            errorContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent,
                                            errorIndicatorColor = colorResource(R.color.red),
                                            focusedTextColor = colorResource(R.color.black),
                                            unfocusedTextColor = colorResource(R.color.black),
                                            disabledTextColor = colorResource(R.color.black),
                                            cursorColor = colorResource(R.color.black),
                                            errorCursorColor = colorResource(R.color.red)
                                        ),
                                        modifier = Modifier
                                            .requiredHeight(64.dp)
                                            .requiredWidth(52.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onValueChange = { value ->
                                            minutes = value
                                            viewModel.changeTimerNum(
                                                index,
                                                hours,
                                                minutes
                                            )
                                        }
                                    )
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    var hours1 by remember { mutableStateOf("") }
                                    var minutes1 by remember { mutableStateOf("") }
                                    var hours2 by remember { mutableStateOf("") }
                                    var minutes2 by remember { mutableStateOf("") }

                                    TextField(
                                        value = hours1,
                                        textStyle = TextStyle(
                                            fontSize = ExtraSmallText,
                                            color = colorResource(R.color.black)
                                        ),
                                        placeholder = {
                                            LightText(
                                                text = "h",
                                                fontSize = ExtraSmallText,
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            errorContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent,
                                            errorIndicatorColor = colorResource(R.color.red),
                                            focusedTextColor = colorResource(R.color.black),
                                            unfocusedTextColor = colorResource(R.color.black),
                                            disabledTextColor = colorResource(R.color.black),
                                            cursorColor = colorResource(R.color.black),
                                            errorCursorColor = colorResource(R.color.red)
                                        ),
                                        modifier = Modifier
                                            .requiredHeight(64.dp)
                                            .requiredWidth(52.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onValueChange = { value ->
                                            hours1 = value
                                            viewModel.changeTimerNum1(
                                                index,
                                                hours1,
                                                minutes1
                                            )
                                        }
                                    )
                                    BoldText(text = ":", fontSize = ExtraSmallText)
                                    TextField(
                                        value = minutes1,
                                        textStyle = TextStyle(
                                            fontSize = ExtraSmallText,
                                            color = colorResource(R.color.black)
                                        ),
                                        placeholder = {
                                            LightText(
                                                text = "m",
                                                fontSize = ExtraSmallText,
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            errorContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent,
                                            errorIndicatorColor = colorResource(R.color.red),
                                            focusedTextColor = colorResource(R.color.black),
                                            unfocusedTextColor = colorResource(R.color.black),
                                            disabledTextColor = colorResource(R.color.black),
                                            cursorColor = colorResource(R.color.black),
                                            errorCursorColor = colorResource(R.color.red)
                                        ),
                                        modifier = Modifier
                                            .requiredHeight(64.dp)
                                            .requiredWidth(52.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onValueChange = { value ->
                                            minutes1 = value
                                            viewModel.changeTimerNum1(
                                                index,
                                                hours1,
                                                minutes1
                                            )
                                        }
                                    )

                                    BoldText(text = "-", fontSize = ExtraSmallText)

                                    TextField(
                                        value = hours2,
                                        textStyle = TextStyle(
                                            fontSize = ExtraSmallText,
                                            color = colorResource(R.color.black)
                                        ),
                                        placeholder = {
                                            LightText(
                                                text = "h",
                                                fontSize = ExtraSmallText,
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            errorContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent,
                                            errorIndicatorColor = colorResource(R.color.red),
                                            focusedTextColor = colorResource(R.color.black),
                                            unfocusedTextColor = colorResource(R.color.black),
                                            disabledTextColor = colorResource(R.color.black),
                                            cursorColor = colorResource(R.color.black),
                                            errorCursorColor = colorResource(R.color.red)
                                        ),
                                        modifier = Modifier
                                            .requiredHeight(64.dp)
                                            .requiredWidth(52.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onValueChange = { value ->
                                            hours2 = value
                                            viewModel.changeTimerNum2(
                                                index,
                                                hours2,
                                                minutes2
                                            )
                                        }
                                    )
                                    BoldText(text = ":", fontSize = ExtraSmallText)
                                    TextField(
                                        value = minutes2,
                                        textStyle = TextStyle(
                                            fontSize = ExtraSmallText,
                                            color = colorResource(R.color.black)
                                        ),
                                        placeholder = {
                                            LightText(
                                                text = "m",
                                                fontSize = ExtraSmallText,
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            errorContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent,
                                            errorIndicatorColor = colorResource(R.color.red),
                                            focusedTextColor = colorResource(R.color.black),
                                            unfocusedTextColor = colorResource(R.color.black),
                                            disabledTextColor = colorResource(R.color.black),
                                            cursorColor = colorResource(R.color.black),
                                            errorCursorColor = colorResource(R.color.red)
                                        ),
                                        modifier = Modifier
                                            .requiredHeight(64.dp)
                                            .requiredWidth(52.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onValueChange = { value ->
                                            minutes2 = value
                                            viewModel.changeTimerNum2(
                                                index,
                                                hours2,
                                                minutes2
                                            )
                                        }
                                    )

                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                ClickableText(
                                    text = AnnotatedString("Range"),
                                    style = TextStyle(
                                        fontSize = ExtraSmallText,
                                        color = colorResource(R.color.black),
                                        fontWeight = if (timerState.type == TimerType.RANGE.str) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    onClick = { viewModel.changeTimerType(index) }
                                )
                                RadioButton(
                                    selected = timerState.type == TimerType.RANGE.str,
                                    colors = RadioButtonColors(
                                        colorResource(R.color.black), colorResource(R.color.black),
                                        colorResource(R.color.black), colorResource(R.color.black)
                                    ),
                                    modifier = Modifier.size(20.dp),
                                    onClick = { viewModel.changeTimerType(index) }
                                )
                            }

                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        OutlinedThemeButton(
                            text = "Add new timer",
                            fontSize = SmallText,
                            modifier = Modifier
                                .height(32.dp)
                                .width(200.dp)
                                .padding(4.dp, 0.dp),
                            onClick = {
                                viewModel.addTimer()
                            }
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun ButtonsBlock(viewModel: NewRecipeScreenViewModel, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                if (viewModel.currentStepIndex.value > 0)
                    OutlinedThemeButton(
                        text = "Go to previous",
                        fontSize = SmallText,
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 4.dp, 0.dp),
                        onClick = {
                            viewModel.goToPreviousStep()
                        }
                    )
            }
            OutlinedThemeButton(
                text = "Add new step",
                fontSize = SmallText,
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .padding(4.dp, 0.dp),
                onClick = {
                    viewModel.createNewStep()
                }
            )
            Box(modifier = Modifier.weight(1f)) {
                if (viewModel.currentStepIndex.value < viewModel.steps.value.size - 1)
                    OutlinedThemeButton(
                        text = "Go to next",
                        fontSize = SmallText,
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxWidth()
                            .padding(4.dp, 0.dp, 0.dp, 0.dp),
                        onClick = {
                            viewModel.goToNextStep()
                        }
                    )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedThemeButton(
                text = "Save recipe",
                fontSize = MediumText,
                modifier = Modifier
                    .height(40.dp)
                    .weight(2f),
                onClick = {
                    viewModel.openWindowDialog()
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedThemeButton(
                text = "Cancel creation",
                fontSize = MediumText,
                modifier = Modifier
                    .height(40.dp)
                    .weight(2f),
                onClick = {
                    viewModel.openWindowCancelDialog()
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WindowDialog(
    viewModel: NewRecipeScreenViewModel,
    navigateToCollection: (String) -> Unit,
    collectionId: String
) {
    if (viewModel.activeWindowDialog.value) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AlertDialog(
                onDismissRequest = { viewModel.closeDialogs() },
                title = {
                    LightText(
                        text = "Choose tags for your recipe (if you want) and confirm saving recipe",
                        fontSize = SmallText
                    )
                },
                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(colorResource(R.color.white_cyan), RoundedCornerShape(8.dp))
                            .border(
                                1.dp,
                                colorResource(R.color.medium_cyan),
                                RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .verticalScroll(rememberScrollState())
                    ) {
                        FlowRow(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            val localDensity = LocalDensity.current
                            for (index in 0 until viewModel.tags.value.size) {
                                var rowHeightDp by remember { mutableStateOf(100.dp) }
                                Card(
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardColors(
                                        colorResource(R.color.white), colorResource(R.color.white),
                                        colorResource(R.color.white), colorResource(R.color.white)
                                    ),
                                    modifier = Modifier.height(rowHeightDp),
                                    onClick = { viewModel.tagCheckingChanged(index) }
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(8.dp, 4.dp)
                                    ) {
                                        Checkbox(
                                            checked = viewModel.tags.value[index].ckecked,
                                            colors = CheckboxColors(
                                                colorResource(R.color.black),
                                                colorResource(R.color.black),
                                                colorResource(R.color.white),
                                                colorResource(R.color.white),
                                                colorResource(R.color.white),
                                                colorResource(R.color.white),
                                                colorResource(R.color.white),
                                                colorResource(R.color.black),
                                                colorResource(R.color.black),
                                                colorResource(R.color.black),
                                                colorResource(R.color.black),
                                                colorResource(R.color.black)
                                            ),
                                            modifier = Modifier.size(16.dp),
                                            onCheckedChange = { viewModel.tagCheckingChanged(index) }
                                        )
                                        DarkText(
                                            text = viewModel.tags.value[index].name,
                                            fontSize = SmallText,
                                            modifier = Modifier
                                                .onGloballyPositioned { coordinates ->
                                                    val rowHeightDpCurrent =
                                                        with(localDensity) { coordinates.size.height.toDp() } + 8.dp
                                                    if (rowHeightDp == 100.dp)
                                                        rowHeightDp = rowHeightDpCurrent
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    OutlinedThemeButton(
                        text = "Confirm save",
                        fontSize = MediumText,
                        modifier = Modifier.height(40.dp),
                        onClick = {
                            viewModel.closeDialogs()
                            viewModel.saveRecipe()
                            navigateToCollection("${CollectionRoutes.Collection.route}/$collectionId")
                        }
                    )
                },
                modifier = Modifier
                    .border(
                        2.dp,
                        colorResource(id = R.color.dark_cyan),
                        RoundedCornerShape(24.dp)
                    )
                    .clip(RoundedCornerShape(24.dp))
            )
        }
    }
}

@Composable
fun WindowCancelDialog(
    viewModel: NewRecipeScreenViewModel,
    navigateToCollection: (String) -> Unit,
    collectionId: String
) {
    if (viewModel.activeWindowCancelDialog.value) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AlertDialog(
                onDismissRequest = { viewModel.closeDialogs() },
                title = {
                    LightText(
                        text = "Are you sure you want to leave? All your changes will not be saved!",
                        fontSize = SmallText
                    )
                },
                text = { },
                confirmButton = {
                    OutlinedThemeButton(
                        text = "Confirm cancel",
                        fontSize = MediumText,
                        modifier = Modifier.height(40.dp),
                        onClick = {
                            viewModel.closeDialogs()
                            navigateToCollection("${CollectionRoutes.Collection.route}/$collectionId")
                        }
                    )
                },
                modifier = Modifier
                    .border(
                        2.dp,
                        colorResource(id = R.color.dark_cyan),
                        RoundedCornerShape(24.dp)
                    )
            )
        }
    }
}
