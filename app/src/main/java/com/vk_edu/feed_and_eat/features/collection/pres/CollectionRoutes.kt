package com.vk_edu.feed_and_eat.features.collection.pres

sealed class CollectionRoutes(val route : String) {
    data object AllCollections : CollectionRoutes("AllCollections")
    data object Collection : CollectionRoutes("CollectionScreen")
    data object Id : CollectionRoutes("/{id}")
}