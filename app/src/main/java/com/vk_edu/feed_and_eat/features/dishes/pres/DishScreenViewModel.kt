package com.vk_edu.feed_and_eat.features.dishes.pres

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.dishes.data.DishesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Dish
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishScreenViewModel @Inject constructor(
    private val _dishesRepo: DishesRepoImpl
) : ViewModel() {
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    private val _isPostSuccess = mutableStateOf<Boolean>(false)
    val isPostSuccess: State<Boolean> = _isPostSuccess

    private val _isLoadSuccess = mutableStateOf<Boolean>(false)
    val isLoadSuccess: State<Boolean> = _isLoadSuccess

    private val _dishesList = mutableStateListOf<Dish>()
    val dishesList: List<Dish> = _dishesList

    fun loadDishes() {
        viewModelScope.launch {
            try {
                _dishesRepo.loadDishes().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            if (_dishesList.isNotEmpty()) {
                                _dishesList.clear()
                            }
                            _dishesList += response.data
                            Log.d("taag", _dishesList.toString())
                        }

                        is Response.Failure -> {
                            onError(response.e)
                            Log.d("taag", response.e.toString())
                        }
                    }
                }

            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun postDish(name: String, ingredientsCount: Int, stepsCount: Int) {
        viewModelScope.launch {
            try {
                _dishesRepo.postDish(name, ingredientsCount, stepsCount).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> _isPostSuccess.value = true
                        is Response.Failure -> onError(response.e)
                    }
                }

            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }
}