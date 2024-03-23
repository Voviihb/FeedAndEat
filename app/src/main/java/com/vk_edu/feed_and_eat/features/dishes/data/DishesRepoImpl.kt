package com.vk_edu.feed_and_eat.features.dishes.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Dish
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.DishesRepository
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishesRepoImpl @Inject constructor(
    private val db: FirebaseFirestore
) : DishesRepository {
    override fun loadDishes(): Flow<Response<List<Dish>>> = repoTryCatchBlock {
        val snapshot = db.collection("dishes").get().await()
        val dishesList = mutableListOf<Dish>()
        for (document in snapshot) {
            val dish = document.toObject<Dish>()
            dish.id = document.id
            dishesList.add(dish)
        }
        return@repoTryCatchBlock dishesList.toList()
    }.flowOn(Dispatchers.IO)

    override fun postDish(
        name: String, ingredientsCount: Int, stepsCount: Int
    ): Flow<Response<DocumentReference>> = repoTryCatchBlock {
        val dish = hashMapOf(
            "name" to name,
            "rating" to 0.0,
            "cookedCount" to 0,
            "ingredientsCount" to ingredientsCount,
            "stepsCount" to stepsCount,
            "published" to Timestamp(Date())
        )
        db.collection("dishes").add(dish).await()
    }.flowOn(Dispatchers.IO)
}