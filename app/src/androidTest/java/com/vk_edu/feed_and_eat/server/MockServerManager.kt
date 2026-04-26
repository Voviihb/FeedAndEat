package com.vk_edu.feed_and_eat.server

import android.content.res.AssetManager
import okhttp3.mockwebserver.MockWebServer

/**
 * Обёртка над [MockWebServer] с [PathMapDispatcher].
 *
 * Использование:
 * ```kotlin
 * val server = MockServerManager(assets)
 * server.start()
 * server.setMocks(loginMock.success("login_test/auth_login_200.json"))
 * // ... запускаем Activity, делаем запросы ...
 * server.shutdown()
 * ```
 *
 * Метод [setMocks] перезаписывает дефолтный ответ для каждой из переданных ручек.
 * Метод [enqueueMocks] добавляет одноразовые ответы в очередь.
 */
class MockServerManager(private val assets: AssetManager) {

    private val mockServer = MockWebServer()
    private lateinit var dispatcher: PathMapDispatcher

    /** Запускает MockWebServer на порту 8080. */
    fun start() {
        dispatcher = PathMapDispatcher(assets)
        mockServer.dispatcher = dispatcher
        mockServer.start(PORT)
    }

    /** Останавливает MockWebServer. */
    fun shutdown() {
        runCatching { mockServer.shutdown() }
    }

    /**
     * Устанавливает дефолтные ответы для указанных ручек.
     * Если для ручки уже был дефолтный ответ — он заменяется.
     * Очередь при этом не затрагивается.
     *
     * Можно вызывать как до запуска Activity (в setUp), так и прямо внутри теста
     * для смены поведения в процессе сценария.
     */
    fun setMocks(vararg mocks: MockResponseSetting) {
        mocks.forEach { dispatcher.setDefaultMock(it) }
    }

    /**
     * Добавляет одноразовые ответы в очередь для указанных ручек.
     * Ответ из очереди используется один раз, после чего возвращается к дефолтному.
     */
    fun enqueueMocks(vararg mocks: MockResponseSetting) {
        mocks.forEach { dispatcher.enqueueMock(it) }
    }

    companion object {
        const val PORT = 8080
        const val BASE_URL = "http://localhost:$PORT/"
    }
}
