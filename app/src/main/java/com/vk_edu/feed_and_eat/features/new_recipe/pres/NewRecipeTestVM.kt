package com.vk_edu.feed_and_eat.features.new_recipe.pres

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.new_recipe.data.NewRecipeRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRecipeTestVM @Inject constructor(
    private val _authRepo: AuthRepoImpl,
    private val _newRecipeRepo: NewRecipeRepoImpl
) : ViewModel() {
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    private val _imagePath = mutableStateOf<Uri?>(null)
    val imagePath: State<Uri?> = _imagePath

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
                            Log.d("Taag", response.toString())
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

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}