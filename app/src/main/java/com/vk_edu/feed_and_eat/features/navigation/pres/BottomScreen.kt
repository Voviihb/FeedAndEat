package com.vk_edu.feed_and_eat.features.navigation.pres

import com.vk_edu.feed_and_eat.R

sealed class BottomScreen(
    val route: String,
    val drawable : Int,
    val name : Int,
) {
    data object HomeScreen : BottomScreen("HomeScreen",  R.drawable.home,  R.string.main)
    data object  SearchScreen : BottomScreen("SearchScreen", R.drawable.search, R.string.search)
    data object InProgressScreen : BottomScreen("inProgressScreen", R.drawable.progress, R.string.inProgress)
    data object ProfileScreen : BottomScreen("ProfileScreen", R.drawable.profile, R.string.profile)
    data object CollectionOverviewScreen : BottomScreen("CollectionOverviewScreen", R.drawable.collection, R.string.collection)
}
