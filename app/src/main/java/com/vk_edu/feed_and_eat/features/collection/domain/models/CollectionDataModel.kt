package com.vk_edu.feed_and_eat.features.collection.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.vk_edu.feed_and_eat.features.recipe.data.models.RecipeDataModel

@IgnoreExtraProperties
data class Compilation(
    val name : String = "",
    val recipeList : MutableList<RecipeDataModel> = mutableListOf(),
    val picture : Int? = null,
) /*TODO replace after merge*/
