package com.vk_edu.feed_and_eat.screenshot.navigation

import com.android.ide.common.rendering.api.SessionParams
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest
import org.junit.Test

class GlobalNavigationBarScreenshotTest : BaseScreenshotTest(
    renderingMode = SessionParams.RenderingMode.NORMAL,
) {

    @Test
    fun homeTabActive() = snapshot("home_tab_active") {
        GlobalNavigationBar(
            navigateToRoute = {},
            navigateNoState = {},
            currentDestination = "HomeScreen"
        )
    }

    @Test
    fun searchTabActive() = snapshot("search_tab_active") {
        GlobalNavigationBar(
            navigateToRoute = {},
            navigateNoState = {},
            currentDestination = "SearchScreen"
        )
    }

    @Test
    fun collectionsTabActive() = snapshot("collections_tab_active") {
        GlobalNavigationBar(
            navigateToRoute = {},
            navigateNoState = {},
            currentDestination = "CollectionOverviewScreen"
        )
    }

    @Test
    fun inProgressTabActive() = snapshot("in_progress_tab_active") {
        GlobalNavigationBar(
            navigateToRoute = {},
            navigateNoState = {},
            currentDestination = "inProgressScreen"
        )
    }

    @Test
    fun profileTabActive() = snapshot("profile_tab_active") {
        GlobalNavigationBar(
            navigateToRoute = {},
            navigateNoState = {},
            currentDestination = "ProfileScreen"
        )
    }

    @Test
    fun noTabActive() = snapshot("no_tab_active") {
        GlobalNavigationBar(
            navigateToRoute = {},
            navigateNoState = {},
            currentDestination = "unknown"
        )
    }
}