package com.vk_edu.feed_and_eat.rules

import android.Manifest
import android.os.Build
import androidx.test.core.app.ActivityScenario
import androidx.test.rule.GrantPermissionRule
import com.vk_edu.feed_and_eat.MainActivity
import org.junit.rules.ExternalResource
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * JUnit-правило для запуска [MainActivity] в инструментальных UI-тестах.
 *
 * Применяет:
 * - выдачу всех runtime-разрешений (POST_NOTIFICATIONS, READ_MEDIA_IMAGES и др.),
 *   чтобы системные диалоги не блокировали тест
 * - запуск [ActivityScenario] с [MainActivity]
 *
 * Использование:
 * ```kotlin
 * @get:Rule val testRule = FeedAndEatTestRule()
 *
 * fun myTest() {
 *     testRule.launch()
 *     // ... взаимодействие с UI ...
 * }
 * ```
 */
class FeedAndEatTestRule : ExternalResource() {

    private var scenario: ActivityScenario<MainActivity>? = null

    override fun apply(base: Statement, description: Description): Statement {
        var statement = super.apply(base, description)

        // Собираем все необходимые разрешения в зависимости от версии Android,
        // чтобы системные диалоги не показывались во время теста.
        val permissions = buildList {
            // Уведомления (Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
                // Медиафайлы (Android 13+): READ_MEDIA_IMAGES
                add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                // Медиафайлы (Android 12 и ниже): READ_EXTERNAL_STORAGE
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            // Частичный доступ к медиа (Android 14+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            }
        }

        if (permissions.isNotEmpty()) {
            statement = GrantPermissionRule
                .grant(*permissions.toTypedArray())
                .apply(statement, description)
        }

        return statement
    }

    override fun after() {
        scenario?.close()
        scenario = null
    }

    /**
     * Запускает [MainActivity].
     * Вызывается из [com.vk_edu.feed_and_eat.base.BaseUiTest.start] после настройки мок-сервера.
     */
    fun launch() {
        check(scenario == null) { "Activity already launched. Call close() first." }
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    /** Закрывает текущий сценарий (для перезапуска между шагами). */
    fun close() {
        scenario?.close()
        scenario = null
    }

    fun getScenario(): ActivityScenario<MainActivity> =
        checkNotNull(scenario) { "Activity not launched yet. Call launch() first." }
}
