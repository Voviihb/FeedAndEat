package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.collection.domain.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionScreenViewModel @Inject constructor() : ViewModel(){
    private val repo = CollectionRepository()
    private val mutableCollection = mutableStateOf(CollectionDataModel())
    val collection : State<CollectionDataModel> = mutableCollection

    fun getCollectionViewModel(){
        viewModelScope.launch{
            mutableCollection.value = repo.getCollection()
        }
    }
}