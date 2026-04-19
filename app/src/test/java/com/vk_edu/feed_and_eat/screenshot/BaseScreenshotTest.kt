package com.vk_edu.feed_and_eat.screenshot

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule

/**
 * Базовый класс для скриншот-тестов Compose-компонентов.
 *
 * Использует Paparazzi для рендеринга composable на JVM без эмулятора.
 * Все composable оборачиваются в [MaterialTheme].
 *
 * Использование:
 * ```kotlin
 * class MyScreenshotTest : BaseScreenshotTest() {
 *     @Test
 *     fun myComponent() {
 *         snapshot("my_component") {
 *             MyComponent(text = "Hello")
 *         }
 *     }
 * }
 * ```
 *
 * Запуск тестов: `./gradlew :app:recordPaparazziDebug` — запись эталонов
 * Проверка:      `./gradlew :app:verifyPaparazziDebug` — сравнение со снимками
 */
abstract class BaseScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    /**
     * Делает скриншот переданного composable, оборачивая его в [MaterialTheme].
     *
     * @param name Имя снимка — используется как суффикс в имени файла.
     * @param composable Composable-контент для снятия скриншота.
     */
    protected fun snapshot(
        name: String,
        composable: @Composable () -> Unit,
    ) {
        paparazzi.snapshot(name = name) {
            MaterialTheme {
                composable()
            }
        }
    }
}
