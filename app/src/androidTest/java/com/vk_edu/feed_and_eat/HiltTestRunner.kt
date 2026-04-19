package com.vk_edu.feed_and_eat

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Кастомный TestRunner, необходимый для работы Hilt в инструментальных тестах.
 *
 * Указывается в build.gradle.kts:
 * ```
 * testInstrumentationRunner = "com.vk_edu.feed_and_eat.HiltTestRunner"
 * ```
 */
class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application = super.newApplication(cl, HiltTestApplication::class.java.name, context)
}
