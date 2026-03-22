package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AllCollectionsScreenViewModel @Inject constructor(
    private val _usersRepo: UsersRepository
) : ViewModel() {
    private val _activeWindowDialog = mutableStateOf(false)
    val activeWindowDialog : State<Boolean> = _activeWindowDialog

    private val _collectionsData = mutableStateOf(listOf<CollectionDataModel>())
    val collectionsData: State<List<CollectionDataModel>> = _collectionsData

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun loadAllUserCollections() {
        viewModelScope.launch {
            try {
                android.util.Log.d("AllCollectionsViewModel", "Loading user collections...")
                _usersRepo.getUserCollections().collect { response ->
                        when (response) {
                            is Response.Loading -> {
                                android.util.Log.d("AllCollectionsViewModel", "Loading...")
                                _loading.value = true
                            }
                            is Response.Success -> {
                                android.util.Log.d("AllCollectionsViewModel", "Collections loaded: ${response.data?.size ?: 0}")
                                if (response.data != null) {
                                    _collectionsData.value = response.data
                                }
                            }

                            is Response.Failure -> {
                                android.util.Log.e("AllCollectionsViewModel", "Failed to load collections", response.e)
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

    fun createNewUserCollection(name: String) {
        viewModelScope.launch {
            try {
                android.util.Log.d("AllCollectionsViewModel", "Creating collection: $name")
                _usersRepo.addNewUserCollection(
                    collection = CollectionDataModel(
                        name = name,
                        picture = null
                    )
                ).collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            android.util.Log.d("AllCollectionsViewModel", "Creating collection - Loading...")
                            _loading.value = true
                        }
                        is Response.Success -> {
                            android.util.Log.d("AllCollectionsViewModel", "Collection created successfully!")
                            loadAllUserCollections() // Перезагружаем список
                        }
                        is Response.Failure -> {
                            android.util.Log.e("AllCollectionsViewModel", "Failed to create collection", response.e)
                            onError(response.e)
                        }
                    }
                }

            } catch (e: Exception) {
                android.util.Log.e("AllCollectionsViewModel", "Exception in createNewUserCollection", e)
                onError(e)
            }
            _loading.value = false
        }
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun openWindowDialog(){
        _activeWindowDialog.value = !_activeWindowDialog.value
    }

    init {
        loadAllUserCollections()
    }
}