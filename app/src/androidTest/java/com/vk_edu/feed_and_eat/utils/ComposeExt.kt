package com.vk_edu.feed_and_eat.utils

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import io.github.kakaocup.compose.node.assertion.NodeAssertions

/** Дефолтный таймаут ожидания видимости элемента, мс. */
const val TIMEOUT_DEFAULT = 10_000L

/** Дефолтный интервал между попытками проверки, мс. */
const val INTERVAL_DEFAULT = 500L

/**
 * Ожидает появления элемента на экране с повторными попытками.
 *
 * ⚠️ НЕ вызывать внутри `onComposeScreen { ... }` — это вызовет deadlock.
 * Вызывать после закрытия блока onComposeScreen.
 *
 * Использование:
 * ```kotlin
 * // ✅ Правильно — снаружи onComposeScreen
 * loginScreen.errorText.shouldBeVisible()
 *
 * // ❌ Неправильно — внутри onComposeScreen
 * onComposeScreen<LoginScreen>(composeRule) {
 *     errorText.shouldBeVisible()  // deadlock!
 * }
 * ```
 */
fun <T : NodeAssertions> T.shouldBeVisible(
    timeoutMs: Long = TIMEOUT_DEFAULT,
    intervalMs: Long = INTERVAL_DEFAULT,
    assert: T.() -> Unit = {},
): T {
    val deadline = System.currentTimeMillis() + timeoutMs
    var lastError: Throwable? = null
    while (System.currentTimeMillis() < deadline) {
        try {
            assertIsDisplayed()
            assert()
            return this
        } catch (e: Throwable) {
            lastError = e
            Thread.sleep(intervalMs)
        }
    }
    throw lastError
        ?: AssertionError("Element not displayed after ${timeoutMs}ms timeout")
}

/**
 * Ожидает появления ноды по testTag с повторными попытками.
 *
 * Безопасно вызывать напрямую из тела теста — не использует блоки onComposeScreen.
 * Использует composeRule.waitUntil(), который корректно работает на тест-треде.
 *
 * @param useUnmergedTree если true — ищет в unmerged дереве семантики.
 * Нужно для нод внутри TextField/supportingText, где Material3 сливает семантику дочерних узлов.
 */
fun waitForNodeWithTag(
    composeRule: ComposeTestRule,
    tag: String,
    timeoutMs: Long = TIMEOUT_DEFAULT,
    useUnmergedTree: Boolean = true,
) {
    composeRule.waitUntil(timeoutMillis = timeoutMs) {
        composeRule.onAllNodes(hasTestTag(tag), useUnmergedTree = useUnmergedTree)
            .fetchSemanticsNodes()
            .isNotEmpty()
    }
}
