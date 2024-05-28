package com.vk_edu.feed_and_eat.features.new_recipe.pres

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.new_recipe.data.NewRecipeRepoImpl
import com.vk_edu.feed_and_eat.features.search.pres.TagChecking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRecipeScreenViewModel @Inject constructor(
    private val _recipesRepo: RecipesRepoImpl,
    private val _authRepo: AuthRepoImpl,
    private val _newRecipeRepo: NewRecipeRepoImpl
) : ViewModel() {
    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _imagePath = mutableStateOf<Uri?>(null)
    val imagePath: State<Uri?> = _imagePath

    private val _steps = mutableStateOf(listOf(Instruction()))
    val steps: State<List<Instruction>> = _steps

    private val _currentStep = mutableStateOf(Instruction())
    val currentStep: State<Instruction> = _currentStep

    private val _currentStepIndex = mutableStateOf(0)
    val currentStepIndex: State<Int> = _currentStepIndex

    private val _tags = mutableStateOf(listOf<TagChecking>())
    val tags: State<List<TagChecking>> = _tags

    private val _activeWindowDialog = mutableStateOf(false)
    val activeWindowDialog: State<Boolean> = _activeWindowDialog

    private val _activeWindowCancelDialog = mutableStateOf(false)
    val activeWindowCancelDialog: State<Boolean> = _activeWindowCancelDialog

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    private val collectionId = "DhsIL0qrXWufKSTVDvx3"

    init {
        viewModelScope.launch {
            try {
                _recipesRepo.loadTags().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            _tags.value = response.data.map { TagChecking(it.name, false) }
                        }

                        is Response.Failure -> {
                            onError(response.e)
                        }
                    }
                }
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun saveRecipe() {
        viewModelScope.launch {
            try {
                val newSteps = _steps.value.map { step ->
                    step.copy(
                        timers = step.timers?.map { timer ->
                            if (timer.type == RANGE) {
                                if (timer.lowerLimit!! > timer.upperLimit!!)
                                    Timer(
                                        type = RANGE,
                                        lowerLimit = timer.upperLimit,
                                        upperLimit = timer.lowerLimit
                                    )
                                else if (timer.lowerLimit == timer.upperLimit)
                                    Timer(type = CONSTANT, number = timer.upperLimit)
                                else
                                    Timer(
                                        type = RANGE,
                                        lowerLimit = timer.lowerLimit,
                                        upperLimit = timer.upperLimit
                                    )
                            }
                            else
                                Timer(type = CONSTANT, number = timer.number)
                        }
                    )
                }
                val user = _authRepo.getUserId()
                if (user != null) {
                    _newRecipeRepo.addNewRecipe(
                        user,
                        _name.value,
                        _imagePath.value,
                        newSteps,
                        _tags.value.filter { it.ckecked }.map { it.name }
                    ).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                val recipeId = response.data
                                if (recipeId != null) {
                                    _recipesRepo.loadRecipeById(recipeId).collect { response ->
                                        when (response) {
                                            is Response.Loading -> _loading.value = true
                                            is Response.Success -> {
                                                _recipesRepo.addRecipeToUserCollection(
                                                    user,
                                                    collectionId,
                                                    recipeId,
                                                    response.data?.image
                                                ).collect { response ->
                                                    when (response) {
                                                        is Response.Loading -> { }
                                                        is Response.Success -> {

                                                        }

                                                        is Response.Failure -> {
                                                            onError(response.e)
                                                        }
                                                    }
                                                }
                                            }

                                            is Response.Failure -> {
                                                onError(response.e)
                                            }
                                        }
                                    }
                                }
                            }

                            is Response.Failure -> {
                                onError(response.e)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun changeName(newName: String) {
        _name.value = newName
    }

    fun changeImagePath(newImagePath:Uri?) {
        _imagePath.value = newImagePath
    }

    fun goToPreviousStep() {
        val actualSteps = _steps.value.toMutableList()
        actualSteps[_currentStepIndex.value] = _currentStep.value
        _steps.value = actualSteps
        _currentStepIndex.value -= 1
        _currentStep.value = actualSteps[_currentStepIndex.value]
    }

    fun goToNextStep() {
        val actualSteps = _steps.value.toMutableList()
        actualSteps[_currentStepIndex.value] = _currentStep.value
        _steps.value = actualSteps
        _currentStepIndex.value += 1
        _currentStep.value = actualSteps[_currentStepIndex.value]
    }

    fun createNewStep() {
        val actualSteps = _steps.value.toMutableList()
        actualSteps[_currentStepIndex.value] = _currentStep.value
        actualSteps.add(Instruction(timers = listOf()))
        _steps.value = actualSteps
        _currentStepIndex.value += 1
        _currentStep.value = actualSteps[_currentStepIndex.value]
    }

    fun deleteCurrentStep() {
        val actualSteps = _steps.value.toMutableList()
        actualSteps.removeAt(_currentStepIndex.value)
        _steps.value = actualSteps
        if (_currentStepIndex.value > 0)
            _currentStepIndex.value -= 1
        _currentStep.value = actualSteps[_currentStepIndex.value]
    }

    fun changeInstruction(text: String) {
        _currentStep.value = _currentStep.value.copy(paragraph = text)
    }

    fun tagCheckingChanged(tag: Int) {
        val actualTags = _tags.value.toMutableList()
        actualTags[tag] = TagChecking(tags.value[tag].name, !tags.value[tag].ckecked)
        _tags.value = actualTags
    }

    fun closeDialogs() {
        _activeWindowDialog.value = false
        _activeWindowCancelDialog.value = false
    }

    fun openWindowDialog() {
        _activeWindowDialog.value = true
    }

    fun openWindowCancelDialog() {
        _activeWindowCancelDialog.value = true
    }

    fun addTimer() {
        val actualTimers = _currentStep.value.timers?.toMutableList() ?: mutableListOf()
        actualTimers.add(Timer(
            type = CONSTANT,
            number = 0,
            lowerLimit = 0,
            upperLimit = 0
        ))
        _currentStep.value = _currentStep.value.copy(
            timers = actualTimers
        )
    }

    fun changeTimerType(index: Int) {
        val actualTimers = _currentStep.value.timers?.toMutableList() ?: mutableListOf()
        actualTimers[index] = actualTimers[index].copy(
            type = if (actualTimers[index].type == CONSTANT) RANGE else CONSTANT
        )
        _currentStep.value = _currentStep.value.copy(
            timers = actualTimers
        )
    }

    fun changeTimerNum(index: Int, numHour: String, numMinute: String) {
        val numHourInt = numHour.filter { it.isDigit() }.ifEmpty { null }?.toInt() ?: 0
        val numMinuteInt = numMinute.filter { it.isDigit() }.ifEmpty { null }?.toInt() ?: 0
        val numInt = numHourInt * 60 + numMinuteInt

        val actualTimers = _currentStep.value.timers?.toMutableList() ?: mutableListOf()
        actualTimers[index] = actualTimers[index].copy(
            number = numInt
        )
        _currentStep.value = _currentStep.value.copy(
            timers = actualTimers
        )
    }

    fun changeTimerNum1(index: Int, num1Hour: String, num1Minute: String) {
        val num1HourInt = num1Hour.filter { it.isDigit() }.ifEmpty { null }?.toInt() ?: 0
        val num1MinuteInt = num1Minute.filter { it.isDigit() }.ifEmpty { null }?.toInt() ?: 0
        val num1Int = num1HourInt * 60 + num1MinuteInt

        val actualTimers = _currentStep.value.timers?.toMutableList() ?: mutableListOf()
        actualTimers[index] = actualTimers[index].copy(
            lowerLimit = num1Int
        )
        _currentStep.value = _currentStep.value.copy(
            timers = actualTimers
        )
    }

    fun changeTimerNum2(index: Int, num2Hour: String, num2Minute: String) {
        val num2HourInt = num2Hour.filter { it.isDigit() }.ifEmpty { null }?.toInt() ?: 0
        val num2MinuteInt = num2Minute.filter { it.isDigit() }.ifEmpty { null }?.toInt() ?: 0
        val num2Int = num2HourInt * 60 + num2MinuteInt

        val actualTimers = _currentStep.value.timers?.toMutableList() ?: mutableListOf()
        actualTimers[index] = actualTimers[index].copy(
            upperLimit = num2Int
        )
        _currentStep.value = _currentStep.value.copy(
            timers = actualTimers
        )
    }

    fun deleteTimer(index: Int) {
        val actualTimers = _currentStep.value.timers?.toMutableList() ?: mutableListOf()
        actualTimers.removeAt(index)
        _currentStep.value =  _currentStep.value.copy(
            timers = actualTimers
        )
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    companion object {
        private const val CONSTANT = "constant"
        private const val RANGE = "range"
    }
}