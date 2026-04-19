package com.vk_edu.feed_and_eat.screenshot.recipe

import com.vk_edu.feed_and_eat.features.recipe.pres.preview.BoxWithCards
import com.vk_edu.feed_and_eat.features.recipe.pres.preview.TextBox
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest
import org.junit.Test

class RecipePreviewScreenshotTest : BaseScreenshotTest() {

    // region TextBox

    @Test
    fun textBox_shortText() {
        snapshot("text_box_short_text") {
            TextBox(text = "Паста")
        }
    }

    @Test
    fun textBox_longText() {
        snapshot("text_box_long_text") {
            TextBox(text = "Куриное филе в сливочно-чесночном соусе с прованскими травами")
        }
    }

    // endregion

    // region BoxWithCards

    @Test
    fun boxWithCards_fewTags() {
        snapshot("box_with_cards_few_tags") {
            BoxWithCards(
                bigText = listOf("Завтрак", "Быстро", "Вегетарианское")
            )
        }
    }

    @Test
    fun boxWithCards_manyTagsWithNull() {
        snapshot("box_with_cards_many_tags_with_null") {
            BoxWithCards(
                bigText = listOf(
                    "Завтрак",
                    null,
                    "Обед",
                    "Ужин",
                    "Быстро",
                    "Вегетарианское",
                    "Без глютена",
                    "Низкокалорийное",
                    null,
                    "Острое",
                    "Супы"
                )
            )
        }
    }

    @Test
    fun boxWithCards_emptyList() {
        snapshot("box_with_cards_empty") {
            BoxWithCards(bigText = emptyList())
        }
    }

    // endregion
}