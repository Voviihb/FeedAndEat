package com.vk_edu.feed_and_eat.features.collection.data.repository

import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.collection.data.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.collection.data.models.Compilation
import com.vk_edu.feed_and_eat.features.recipe.data.repository.RecipeRepo

class CollectionRepository {
    private val repo = RecipeRepo()

    private suspend fun getCompilation(num : Int) : Compilation{
        val name = "Любимое"
        val recipes = List(num){ repo.getRecipe() }
        return Compilation(name, recipes, R.drawable.recipe)
    }
    suspend fun getCollection(): CollectionDataModel {
        val compilations = List(20) { getCompilation((0..10).random()) }
        return CollectionDataModel(compilations)
    }
}