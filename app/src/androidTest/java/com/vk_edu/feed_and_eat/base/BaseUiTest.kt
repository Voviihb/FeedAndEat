package com.vk_edu.feed_and_eat.base

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.vk_edu.feed_and_eat.network.TokenStorage
import com.vk_edu.feed_and_eat.rules.FeedAndEatTestRule
import com.vk_edu.feed_and_eat.server.MockResponseSetting
import com.vk_edu.feed_and_eat.server.MockServerManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Базовый класс для функциональных UI-тестов на Compose.
 *
 * Предоставляет:
 * - [composeRule] — правило для взаимодействия с Compose-UI
 * - [testRule] — правило для запуска [com.vk_edu.feed_and_eat.MainActivity]
 * - [setMocks] — смена дефолтных мок-ответов в любой момент теста
 * - [enqueueMocks] — добавление одноразовых ответов в очередь
 * - [start] — запуск сценария с преднастроенными моками
 *
 * Пример использования:
 * ```kotlin
 * @HiltAndroidTest
 * class LoginUiTest : BaseUiTest() {
 *
 *     @Test
 *     fun loginError_thenSuccess() = start(
 *         defaultResponses = listOf(
 *             AuthMockResponse.loginMock.response(401, "login_test/auth_login_401.json"),
 *         )
 *     ) {
 *         onComposeScreen<LoginScreen>(composeRule) {
 *             emailField.performTextInput("user@test.com")
 *         }
 *         setMocks(AuthMockResponse.loginMock.success("login_test/auth_login_200.json"))
 *         // ...
 *     }
 * }
 * ```
 */
@HiltAndroidTest
abstract class BaseUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    @get:Rule(order = 2)
    val testRule = FeedAndEatTestRule()

    /**
     * Хранилище токенов — инжектируется Hilt после [hiltRule.inject()].
     * Используется в [start] для предварительной авторизации (без прохождения UI логина).
     */
    @Inject
    lateinit var tokenStorage: TokenStorage

    private val assets by lazy {
        InstrumentationRegistry.getInstrumentation().context.assets
    }

    protected val server: MockServerManager by lazy {
        MockServerManager(assets)
    }

    @Before
    fun baseSetUp() {
        hiltRule.inject()
    }

    @After
    fun baseTearDown() {
        server.shutdown()
        // Очищаем токены между тестами, чтобы следующий тест не наследовал
        // авторизованное состояние от предыдущего.
        // Без этого NavBarViewModel видит старый токен в DataStore и сразу
        // навигируется на HomeScreen, даже если тест ожидает LoginScreen.
        runBlocking { tokenStorage.clear() }
    }

    /**
     * Устанавливает дефолтные мок-ответы прямо внутри теста.
     *
     * Можно вызывать в любой момент сценария, чтобы изменить поведение ручки.
     * Например, сначала возвращать 401, а после взаимодействия пользователя — 200.
     */
    fun setMocks(vararg mocks: MockResponseSetting) {
        server.setMocks(*mocks)
    }

    /**
     * Добавляет одноразовые ответы в очередь для указанных ручек.
     * Каждый ответ из очереди используется один раз, затем используется дефолтный.
     */
    fun enqueueMocks(vararg mocks: MockResponseSetting) {
        server.enqueueMocks(*mocks)
    }

    /**
     * Запускает сценарий UI-теста.
     *
     * Порядок действий:
     * 1. Запускает MockWebServer
     * 2. Устанавливает [defaultResponses] как дефолтные ответы
     * 3. Запускает Activity
     * 4. Выполняет тестовый блок [block]
     *
     * @param defaultResponses Список мок-ответов, устанавливаемых до запуска Activity.
     * @param block Тестовый сценарий.
     */
    /**
     * Запускает сценарий UI-теста.
     *
     * Порядок действий:
     * 1. Запускает MockWebServer
     * 2. Устанавливает [defaultResponses] как дефолтные ответы
     * 3. Если [isAuthorized] = true — записывает фиктивные токены в DataStore,
     *    чтобы [com.vk_edu.feed_and_eat.features.navigation.pres.NavBarViewModel]
     *    при старте сразу навигировался на HomeScreen (без прохождения LoginScreen).
     * 4. Запускает Activity
     * 5. Выполняет тестовый блок [block]
     *
     * @param defaultResponses Список мок-ответов, устанавливаемых до запуска Activity.
     * @param isAuthorized Если true — токены записываются до запуска Activity,
     *   и приложение открывается сразу на HomeScreen.
     * @param block Тестовый сценарий.
     */
    protected fun start(
        defaultResponses: List<MockResponseSetting> = emptyList(),
        isAuthorized: Boolean = false,
        block: () -> Unit,
    ) {
        server.start()
        server.setMocks(*defaultResponses.toTypedArray())
        if (isAuthorized) {
            runBlocking {
                tokenStorage.saveTokens(
                    access = "test_access_token",
                    refresh = "test_refresh_token",
                )
            }
        }
        testRule.launch()
        block()
    }
}
