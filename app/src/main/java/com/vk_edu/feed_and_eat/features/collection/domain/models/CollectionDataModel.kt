package com.vk_edu.feed_and_eat.features.collection.domain.models

import com.vk_edu.feed_and_eat.features.recipe.data.models.RecipeDataModel

data class Compilation(
    val name : String = "",
    val recipeList : List<RecipeDataModel> = listOf(),
    val picture : Int? = null,
)

data class CollectionDataModel(
    val compilations : List<Compilation> = listOf(),
)