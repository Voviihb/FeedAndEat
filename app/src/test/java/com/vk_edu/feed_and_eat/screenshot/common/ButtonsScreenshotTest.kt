package com.vk_edu.feed_and_eat.screenshot.common

import com.vk_edu.feed_and_eat.common.graphics.SquareArrowButton
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest
import org.junit.Test

class ButtonsScreenshotTest : BaseScreenshotTest() {

    @Test
    fun squareArrowButton() {
        snapshot("square_arrow_button") {
            SquareArrowButton(onClick = {})
        }
    }
}