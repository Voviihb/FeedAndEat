package com.vk_edu.feed_and_eat.screenshot.profile

import com.vk_edu.feed_and_eat.features.profile.pres.Profile
import com.vk_edu.feed_and_eat.features.profile.pres.ProfileScreenContent
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest
import org.junit.Test

class ProfileScreenshotTest : BaseScreenshotTest() {
    @Test
    fun profileScreen_emptyProfile() {
        snapshot("profile_screen_empty") {
            ProfileScreenContent(
                profile = Profile(
                    email = null,
                    nickname = null,
                    avatar = null,
                    aboutMe = "",
                ),
                loading = false,
                imagePath = null,
                onImagePickerRequest = {},
                onAboutMeChange = {},
                onSave = {},
                onLogout = {},
            )
        }
    }

    @Test
    fun profileScreen_filledProfile() {
        snapshot("profile_screen_filled") {
            ProfileScreenContent(
                profile = Profile(
                    email = "user@example.com",
                    nickname = "chef_master",
                    avatar = null,
                    aboutMe = "Люблю готовить итальянскую кухню. Особенно пасту и пиццу!",
                ),
                loading = false,
                imagePath = null,
                onImagePickerRequest = {},
                onAboutMeChange = {},
                onSave = {},
                onLogout = {},
            )
        }
    }

    @Test
    fun profileScreen_longAboutMe() {
        snapshot("profile_screen_long_about_me") {
            ProfileScreenContent(
                profile = Profile(
                    email = "longuser@mail.ru",
                    nickname = "username",
                    avatar = null,
                    aboutMe = "Я профессиональный шеф-повар с 10-летним опытом работы в ресторанах высокой кухни. " +
                        "Специализируюсь на французской и итальянской кухне. " +
                        "Люблю экспериментировать с новыми ингредиентами и техниками приготовления." +
                        "И вообще я самый лучший кулинар на свете.",
                ),
                loading = false,
                imagePath = null,
                onImagePickerRequest = {},
                onAboutMeChange = {},
                onSave = {},
                onLogout = {},
            )
        }
    }
}