package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.collection.data.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.collection.data.repository.CollectionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CollectionScreenViewModel @Inject constructor() : ViewModel(){
    private val repo = CollectionRepository()
    val mutableCollection = mutableStateOf(CollectionDataModel())
    private val collection : State<CollectionDataModel> = mutableCollection

    fun getCollectionViewModel(){
        viewModelScope.launch{
            mutableCollection.value = repo.getCollection()
        }
    }
}