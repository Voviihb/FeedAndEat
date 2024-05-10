package com.vk_edu.feed_and_eat.features.recipe.domain.models

sealed class TimerTypes(val type: String){
    data object Constant : TimerTypes(type = "constant")
    data object Range : TimerTypes(type = "range")
}
