package com.vk_edu.feed_and_eat.server

/**
 * Модель одного мок-ответа сервера.
 *
 * @param apiPath   Путь API, например "/auth/token"
 * @param method    HTTP-метод (GET/POST/...)
 * @param code      HTTP-код ответа (200, 401, 500, ...)
 * @param assetFile Путь к JSON-файлу в `androidTest/assets`, например "login_test/auth_login_200.json"
 * @param body      Альтернатива assetFile — тело ответа строкой (если задан, assetFile игнорируется)
 * @param bodyDelayMs Задержка ответа в миллисекундах (для имитации медленной сети)
 */
data class MockResponseSetting(
    val apiPath: String,
    val method: MockServerMethod = MockServerMethod.POST,
    val code: Int,
    val assetFile: String = "",
    val body: String? = null,
    val bodyDelayMs: Long = 0L,
)

/**
 * Построитель мока для конкретной ручки.
 * Позволяет писать: `loginMock.success("login_test/auth_login_200.json")`
 */
data class MockResponseBuilder(
    val apiPath: String,
    val method: MockServerMethod = MockServerMethod.POST,
)

/** Успешный ответ с телом из asset-файла. */
fun MockResponseBuilder.success(assetFile: String, code: Int = 200): MockResponseSetting =
    MockResponseSetting(apiPath = apiPath, method = method, code = code, assetFile = assetFile)

/**
 * Ответ с произвольным кодом и телом из asset-файла.
 * Псевдоним для явного указания кода ответа, например при 401 или 500.
 */
fun MockResponseBuilder.response(code: Int, assetFile: String): MockResponseSetting =
    MockResponseSetting(apiPath = apiPath, method = method, code = code, assetFile = assetFile)

/**
 * Ответ с кодом 200 и пустым JSON-массивом — удобен для эндпоинтов списков.
 */
fun MockResponseBuilder.emptyList(): MockResponseSetting =
    MockResponseSetting(apiPath = apiPath, method = method, code = 200, body = "[]")

/**
 * Ответ с кодом 200 и пустым JSON-объектом.
 */
fun MockResponseBuilder.emptyObject(): MockResponseSetting =
    MockResponseSetting(apiPath = apiPath, method = method, code = 200, body = "{}")

/** Ответ с пустым телом (204 No Content и т.п.). */
fun MockResponseBuilder.empty(code: Int = 204): MockResponseSetting =
    MockResponseSetting(apiPath = apiPath, method = method, code = code, body = "")

/** Ответ с произвольной строкой в теле. */
fun MockResponseBuilder.withBody(code: Int = 200, body: String): MockResponseSetting =
    MockResponseSetting(apiPath = apiPath, method = method, code = code, body = body)
