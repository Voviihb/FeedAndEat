package com.vk_edu.feed_and_eat.screenshot.common

import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.common.graphics.RatingBar
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest
import org.junit.Test

class RatingBarScreenshotTest : BaseScreenshotTest() {

    // region 1 star (display mode — used in DishCard header)

    @Test
    fun ratingBar_oneStar_fullRating() {
        snapshot("rating_bar_1star_full") {
            RatingBar(
                rating = 5f,
                stars = 1,
                modifier = Modifier.Companion.height(24.dp),
            )
        }
    }

    @Test
    fun ratingBar_oneStar_fractionalRating() {
        snapshot("rating_bar_1star_fractional") {
            RatingBar(
                rating = 3.7f,
                stars = 1,
                modifier = Modifier.Companion.height(24.dp),
            )
        }
    }

    @Test
    fun ratingBar_oneStar_halfRating() {
        snapshot("rating_bar_1star_half") {
            RatingBar(
                rating = 2.5f,
                stars = 1,
                modifier = Modifier.Companion.height(24.dp),
            )
        }
    }

    @Test
    fun ratingBar_oneStar_zeroRating() {
        snapshot("rating_bar_1star_zero") {
            RatingBar(
                rating = 0f,
                stars = 1,
                modifier = Modifier.Companion.height(24.dp),
            )
        }
    }

    // endregion

    // region 5 stars (interactive mode — used in CongratulationsScreen)

    @Test
    fun ratingBar_fiveStars_fullRating() {
        snapshot("rating_bar_5stars_full") {
            RatingBar(
                rating = 5f,
                stars = 5,
                modifier = Modifier.Companion.height(32.dp),
            )
        }
    }

    @Test
    fun ratingBar_fiveStars_threeStars() {
        snapshot("rating_bar_5stars_three") {
            RatingBar(
                rating = 3f,
                stars = 5,
                modifier = Modifier.Companion.height(32.dp),
            )
        }
    }

    @Test
    fun ratingBar_fiveStars_zeroRating() {
        snapshot("rating_bar_5stars_zero") {
            RatingBar(
                rating = 0f,
                stars = 5,
                modifier = Modifier.Companion.height(32.dp),
            )
        }
    }

    @Test
    fun ratingBar_fiveStars_fractional() {
        snapshot("rating_bar_5stars_fractional") {
            RatingBar(
                rating = 3.7f,
                stars = 5,
                modifier = Modifier.Companion.height(32.dp),
            )
        }
    }

    // endregion
}