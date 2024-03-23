package com.vk_edu.feed_and_eat.features.dishes.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Dish
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface DishesRepository {
    fun loadDishes(): Flow<Response<List<Dish>>>

    fun postDish(
        name: String,
        ingredientsCount: Int,
        stepsCount: Int
    ): Flow<Response<DocumentReference>>
}