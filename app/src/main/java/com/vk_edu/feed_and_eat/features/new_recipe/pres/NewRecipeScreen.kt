package com.vk_edu.feed_and_eat.features.new_recipe.pres

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
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
import androidx.compose.ui.res.stringResource
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
import com.vk_edu.feed_and_eat.common.graphics.SwipeToDismissItem
import com.vk_edu.feed_and_eat.features.collection.pres.CollectionRoutes
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import com.vk_edu.feed_and_eat.features.search.pres.Nutrient
import com.vk_edu.feed_and_eat.ui.theme.ExtraSmallText
import com.vk_edu.feed_and_eat.ui.theme.MediumText
import com.vk_edu.feed_and_eat.ui.theme.SmallText
import com.vk_edu.feed_and_eat.ui.theme.SmallestText

@Composable
fun NewRecipeScreen(
    navigateToRoute: (String) -> Unit,
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
            placeholderText = stringResource(R.string.recipe_name_placeholder),
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
                    stringResource(R.string.load_dish_photo)
                else
                    stringResource(R.string.load_new_dish_photo),
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
                        text = stringResource(R.string.click_to_load),
                        color = colorResource(R.color.dark_cyan),
                        fontSize = MediumText
                    )
                else
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(viewModel.imagePath.value)
                            .build(),
                        contentDescription = stringResource(id = R.string.imageDescription),
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
                        text = stringResource(R.string.enter_instruction_of_step),
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
                itemsIndexed(
                    items = viewModel.currentStep.value.timers ?: emptyList(),
                    key = { _, timer -> timer.id }) { index, timer ->
                    SwipeToDismissItem(item = timer,
                        onDismissed = {
                            viewModel.deleteTimer(timer)
                        },
                        content = {
                            TimerItem(
                                index = index,
                                timerState = timer,
                                viewModel = viewModel
                            )
                        }
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        OutlinedThemeButton(
                            text = stringResource(R.string.add_new_timer),
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
                        text = stringResource(R.string.go_to_previous),
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
                text = stringResource(R.string.add_new_step),
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
                if (viewModel.currentStepIndex.value < viewModel.steps.value.size - 1) {
                    OutlinedThemeButton(
                        text = stringResource(R.string.go_to_next),
                        fontSize = SmallText,
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxWidth()
                            .padding(4.dp, 0.dp, 0.dp, 0.dp),
                        onClick = {
                            viewModel.goToNextStep()
                        }
                    )
                } else if (viewModel.steps.value.size > 1 &&
                    viewModel.currentStepIndex.value == viewModel.steps.value.size - 1
                ) {
                    OutlinedThemeButton(
                        text = stringResource(R.string.delete_step),
                        fontSize = SmallText,
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxWidth()
                            .padding(4.dp, 0.dp, 0.dp, 0.dp),
                        onClick = {
                            viewModel.deleteCurrentStep()
                        }
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedThemeButton(
                text = stringResource(R.string.save_recipe),
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
                text = stringResource(R.string.cancel_creation),
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
        val nutrients by viewModel.nutrients

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AlertDialog(
                onDismissRequest = { viewModel.closeDialogs() },
                title = {
                    LightText(
                        text = stringResource(R.string.alert_msg_new_recipe),
                        fontSize = SmallText
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(
                                    colorResource(R.color.white_cyan),
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.dp,
                                    colorResource(R.color.medium_cyan),
                                    RoundedCornerShape(8.dp)
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .verticalScroll(rememberScrollState()),
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
                                            colorResource(R.color.white),
                                            colorResource(R.color.white),
                                            colorResource(R.color.white),
                                            colorResource(R.color.white)
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
                                                onCheckedChange = {
                                                    viewModel.tagCheckingChanged(
                                                        index
                                                    )
                                                }
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

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            NutritionInputField(
                                label = stringResource(id = R.string.calories_data),
                                value = nutrients.calories.toString(),
                                placeholder = stringResource(id = R.string.kcal)
                            ) {
                                viewModel.changeNutrient(Nutrient.CALORIES, it)
                            }
                            NutritionInputField(
                                label = stringResource(id = R.string.fats_data),
                                value = nutrients.fat.toString(),
                                placeholder = stringResource(id = R.string.gramm)
                            ) {
                                viewModel.changeNutrient(Nutrient.FAT, it)
                            }
                            NutritionInputField(
                                label = stringResource(id = R.string.proteins_data),
                                value = nutrients.protein.toString(),
                                placeholder = stringResource(id = R.string.gramm)
                            ) {
                                viewModel.changeNutrient(Nutrient.PROTEIN, it)
                            }
                            NutritionInputField(
                                label = stringResource(id = R.string.carbons_data),
                                value = nutrients.carbohydrates.toString(),
                                placeholder = stringResource(id = R.string.gramm)
                            ) {
                                viewModel.changeNutrient(Nutrient.CARBOHYDRATES, it)
                            }
                            NutritionInputField(
                                label = stringResource(id = R.string.sugar_data),
                                value = nutrients.sugar.toString(),
                                placeholder = stringResource(id = R.string.gramm)
                            ) {
                                viewModel.changeNutrient(Nutrient.SUGAR, it)
                            }
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                DarkText(
                                    text = stringResource(R.string.servings),
                                    fontSize = MediumText
                                )
                                ServingsDropdown(
                                    selectedServings = viewModel.servings.value.amount ?: 0
                                ) {
                                    viewModel.changeServingsAmount(it.toString())
                                }
                            }

                            NutritionInputField(
                                label = stringResource(R.string.weight),
                                value = (viewModel.servings.value.weight ?: 0).toString(),
                                placeholder = stringResource(id = R.string.gramm)
                            ) {
                                viewModel.changeServingsWeight(it)
                            }
                        }

                    }
                },
                confirmButton = {
                    OutlinedThemeButton(
                        text = stringResource(R.string.confirm_save),
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
                        text = stringResource(R.string.dialog_before_leave),
                        fontSize = SmallText
                    )
                },
                text = { },
                confirmButton = {
                    OutlinedThemeButton(
                        text = stringResource(R.string.confirm_cancel),
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

@Composable
fun NutritionInputField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DarkText(text = label, fontSize = MediumText)
        OutlinedTextInput(
            modifier = Modifier
                .height(48.dp)
                .width(104.dp),
            text = value,
            fontSize = ExtraSmallText,
            placeholderText = placeholder,
            onValueChange = { input ->
                if (input.all { it.isDigit() || it == '.' }) {
                    onValueChange(input)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun TimerItem(index: Int, timerState: Timer?, viewModel: NewRecipeScreenViewModel) {
    if (timerState != null) {
        var showError by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    if (!showError) {
                        if (index % 2 == 0)
                            colorResource(R.color.light_cyan)
                        else
                            colorResource(R.color.white)
                    } else {
                        colorResource(R.color.red)
                    }
                )
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(horizontal = 2.dp)
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
                    text = AnnotatedString(stringResource(R.string.constant)),
                    style = TextStyle(
                        fontSize = SmallestText,
                        color = colorResource(R.color.black),
                        fontWeight = if (timerState.type == TimerType.CONSTANT.str) FontWeight.Bold else FontWeight.Normal
                    ),
                    onClick = { viewModel.changeTimerType(index) }
                )
            }

            if (timerState.type == TimerType.CONSTANT.str) {
                val totalMinutes = timerState.number
                val hours = totalMinutes?.floorDiv(60) ?: ""
                val minutes = totalMinutes?.rem(60) ?: ""
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    var hoursInput by remember { mutableStateOf(hours.toString().padStart(2, '0')) }
                    var minutesInput by remember {
                        mutableStateOf(
                            minutes.toString().padStart(2, '0')
                        )
                    }

                    val isValid =
                        (hoursInput.toIntOrNull() ?: 0) in 1..23 || (minutesInput.toIntOrNull()
                            ?: 0) in 1..59
                    showError = !isValid

                    TextField(
                        value = hoursInput,
                        textStyle = TextStyle(
                            fontSize = ExtraSmallText,
                            color = colorResource(R.color.black)
                        ),
                        placeholder = {
                            LightText(
                                text = stringResource(R.string.h_hours),
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
                            errorIndicatorColor = Color.Transparent,
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
                            hoursInput = value
                            viewModel.changeTimerNum(
                                index,
                                hoursInput,
                                minutesInput
                            )
                        }
                    )
                    BoldText(text = ":", fontSize = ExtraSmallText)
                    TextField(
                        value = minutesInput,
                        textStyle = TextStyle(
                            fontSize = ExtraSmallText,
                            color = colorResource(R.color.black)
                        ),
                        placeholder = {
                            LightText(
                                text = stringResource(R.string.m_minutes),
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
                            minutesInput = value
                            viewModel.changeTimerNum(
                                index,
                                hoursInput,
                                minutesInput
                            )
                        }
                    )
                }
            } else {
                val totalMinutes1 = timerState.lowerLimit
                val totalMinutes2 = timerState.upperLimit
                val hours1 = totalMinutes1?.floorDiv(60) ?: ""
                val minutes1 = totalMinutes1?.rem(60) ?: ""
                val hours2 = totalMinutes2?.floorDiv(60) ?: ""
                val minutes2 = totalMinutes2?.rem(60) ?: ""
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    var hours1Input by remember {
                        mutableStateOf(
                            hours1.toString().padStart(2, '0')
                        )
                    }
                    var minutes1Input by remember {
                        mutableStateOf(
                            minutes1.toString().padStart(2, '0')
                        )
                    }
                    var hours2Input by remember {
                        mutableStateOf(
                            hours2.toString().padStart(2, '0')
                        )
                    }
                    var minutes2Input by remember {
                        mutableStateOf(
                            minutes2.toString().padStart(2, '0')
                        )
                    }

                    val isValid =
                        ((hours1Input.toIntOrNull() ?: 0) in 1..23
                                || (minutes1Input.toIntOrNull() ?: 0) in 1..59)
                                && ((hours2Input.toIntOrNull() ?: 0) in 1..23
                                || (minutes2Input.toIntOrNull() ?: 0) in 1..59)
                                && ((hours1Input.toIntOrNull() ?: 0) < (hours2Input.toIntOrNull()
                            ?: 0)
                                || (minutes1Input.toIntOrNull() ?: 0) < (minutes2Input.toIntOrNull()
                            ?: 0))
                    showError = !isValid

                    TextField(
                        value = hours1Input,
                        textStyle = TextStyle(
                            fontSize = ExtraSmallText,
                            color = colorResource(R.color.black)
                        ),
                        placeholder = {
                            LightText(
                                text = stringResource(R.string.h_hours),
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
                            hours1Input = value
                            viewModel.changeTimerNum1(
                                index,
                                hours1Input,
                                minutes1Input
                            )
                        }
                    )
                    BoldText(text = ":", fontSize = ExtraSmallText)
                    TextField(
                        value = minutes1Input,
                        textStyle = TextStyle(
                            fontSize = ExtraSmallText,
                            color = colorResource(R.color.black)
                        ),
                        placeholder = {
                            LightText(
                                text = stringResource(R.string.m_minutes),
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
                            minutes1Input = value
                            viewModel.changeTimerNum1(
                                index,
                                hours1Input,
                                minutes1Input
                            )
                        }
                    )

                    BoldText(text = "-", fontSize = ExtraSmallText)

                    TextField(
                        value = hours2Input,
                        textStyle = TextStyle(
                            fontSize = ExtraSmallText,
                            color = colorResource(R.color.black)
                        ),
                        placeholder = {
                            LightText(
                                text = stringResource(R.string.h_hours),
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
                            hours2Input = value
                            viewModel.changeTimerNum2(
                                index,
                                hours2Input,
                                minutes2Input
                            )
                        }
                    )
                    BoldText(text = ":", fontSize = ExtraSmallText)
                    TextField(
                        value = minutes2Input,
                        textStyle = TextStyle(
                            fontSize = ExtraSmallText,
                            color = colorResource(R.color.black)
                        ),
                        placeholder = {
                            LightText(
                                text = stringResource(R.string.m_minutes),
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
                            minutes2Input = value
                            viewModel.changeTimerNum2(
                                index,
                                hours2Input,
                                minutes2Input
                            )
                        }
                    )

                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.ranged)),
                    style = TextStyle(
                        fontSize = SmallestText,
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


@Composable
fun ServingsDropdown(
    selectedServings: Int,
    onServingsSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(104.dp)
            .height(48.dp)
            .clickable { expanded = true }
            .background(
                colorResource(id = R.color.white_cyan),
                RoundedCornerShape(8.dp)
            )
            .border(
                2.dp,
                colorResource(id = R.color.light_cyan),
                RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        DarkText(
            text = stringResource(R.string.amount_servings, selectedServings),
            fontSize = SmallText
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            (1..10).forEach { servings ->
                DropdownMenuItem(
                    text = { LightText(text = servings.toString(), fontSize = MediumText) },
                    onClick = {
                        onServingsSelected(servings)
                        expanded = false
                    }
                )
            }
        }
    }
}
