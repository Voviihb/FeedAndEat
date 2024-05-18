package com.vk_edu.feed_and_eat.features.new_recipe.pres

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.new_recipe.data.NewRecipeRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRecipeScreenViewModel @Inject constructor(
    private val _authRepo: AuthRepoImpl,
    private val _newRecipeRepo: NewRecipeRepoImpl
) : ViewModel() {

    private val _newInstruction = mutableStateOf(Instruction())
    val newInstruction: State<Instruction> = _newInstruction

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _imagePath = mutableStateOf<Uri?>(null)
    val imagePath: State<Uri?> = _imagePath

    private val _instructions = mutableStateOf(listOf<Instruction>())
    val instructions: State<List<Instruction>> = _instructions

    private val _tags = mutableStateOf(listOf<String>())
    val tags: State<List<String>> = _tags

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun addNewRecipe(
        name: String,
        instructions: List<Instruction>,
        tags: List<String>?
    ) {
        viewModelScope.launch {
            try {
                val user = _authRepo.getUserId()
                if (user != null) {
                    _newRecipeRepo.addNewRecipe(user, name, _imagePath.value, instructions, tags)
                        .collect { response ->
                            when (response) {
                                is Response.Loading -> _loading.value = true
                                is Response.Success -> {

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

    fun imageChanged(value: Uri?) {
        _imagePath.value = value
    }

    fun changeName(newName: String) {
        _name.value = newName
    }


    fun selectImagePath() {

    }

    fun changeParagraph(newParagraph: String) {
        _newInstruction.value = _newInstruction.value.copy(
            paragraph = newParagraph
        )
    }

    fun addTimer(timerType: TimerType, num1: String, num2: String) {
        val actualTimers = _newInstruction.value.timers?.toMutableList() ?: mutableListOf()
        actualTimers.add(
            Timer(
                type = timerType.str,
                lowerLimit = if (timerType == TimerType.RANGE) num1.toInt() else null,
                upperLimit = if (timerType == TimerType.RANGE) num2.toInt() else null,
                number = if (timerType == TimerType.CONSTANT) num1.toInt() else null
            )
        )
        _newInstruction.value = _newInstruction.value.copy(
            timers = actualTimers
        )
    }

    fun deleteTimer(index: Int) {
        val actualTimers = _newInstruction.value.timers?.toMutableList()
        actualTimers?.removeAt(index)
        _newInstruction.value = _newInstruction.value.copy(
            timers = actualTimers
        )
    }

    fun addInstruction() {
        val actualInstructions = _instructions.value.toMutableList()
        actualInstructions.add(_newInstruction.value)
        _instructions.value = actualInstructions
        _newInstruction.value = Instruction()
    }

    fun addTag(tag: String) {
        val actualTags = _tags.value.toMutableList()
        actualTags.add(tag)
        _tags.value = actualTags
    }

    fun deleteTag(tag: String) {
        val actualTags = _tags.value.toMutableList()
        actualTags.remove(tag)
        _tags.value = actualTags
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}