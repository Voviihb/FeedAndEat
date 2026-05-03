package com.vk_edu.feed_and_eat.server

import android.content.res.AssetManager
import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Dispatcher для MockWebServer, который маршрутизирует запросы по path + метод.
 *
 * Для каждой пары (path, method) хранится:
 * - очередь одноразовых ответов ([enqueueMock]) — берётся первым
 * - дефолтный ответ ([setDefaultMock]) — используется когда очередь пуста
 *
 * Если для пути нет ни очереди, ни дефолта — возвращается 404.
 */
class PathMapDispatcher(private val assets: AssetManager) : Dispatcher() {

    private val pathMap: MutableMap<String, ApiPathQueue> = ConcurrentHashMap()

    override fun dispatch(request: RecordedRequest): MockResponse {
        val fullPath = request.path ?: ""
        val path = fullPath.split("?")[0]
        val method = request.method ?: "?"
        val key = path + method

        Log.d(TAG, ">>> REQUEST: $method $fullPath  |  key=$key")
        Log.d(TAG, "    Registered keys: ${pathMap.keys.joinToString()}")

        val result = pathMap[key]?.let { queue ->
            val queueSize = queue.queue.size
            val hasDefault = queue.defaultResponse != null
            Log.d(TAG, "    Found queue for key=$key  queueSize=$queueSize  hasDefault=$hasDefault")

            val queued = queue.queue.poll()
            val response = if (queued != null) {
                Log.d(TAG, "    Using queued response: code=${queued.code}  file=${queued.assetFile}")
                queued
            } else {
                queue.defaultResponse?.also { def ->
                    Log.d(TAG, "    Using default response: code=${def.code}  file=${def.assetFile}")
                }
            }
            response?.toMockResponse(assets) ?: run {
                Log.w(TAG, "    Queue exists but no response available for key=$key — 404")
                notFound(path, method)
            }
        } ?: run {
            Log.w(TAG, "    No entry in pathMap for key=$key — 404")
            notFound(path, method)
        }

        Log.d(TAG, "<<< RESPONSE: ${result.status}")
        return result
    }

    /** Установить дефолтный ответ для ручки (используется когда очередь пуста). */
    fun setDefaultMock(setting: MockResponseSetting) {
        val key = setting.apiPath + setting.method.name
        Log.d(TAG, "setDefaultMock: key=$key  code=${setting.code}  file=${setting.assetFile}")
        getQueue(key).defaultResponse = setting
    }

    /** Поставить в очередь одноразовый ответ для ручки. */
    fun enqueueMock(setting: MockResponseSetting) {
        val key = setting.apiPath + setting.method.name
        Log.d(TAG, "enqueueMock: key=$key  code=${setting.code}  file=${setting.assetFile}")
        getQueue(key).queue.add(setting)
    }

    private fun getQueue(key: String): ApiPathQueue =
        pathMap.getOrPut(key) { ApiPathQueue() }

    private fun notFound(path: String, method: String): MockResponse {
        Log.w(TAG, "No mock found for $method $path — returning 404")
        return MockResponse().setResponseCode(404).setBody("""{"error":"no mock for $path"}""")
    }

    companion object {
        private const val TAG = "PathMapDispatcher"
    }
}

private data class ApiPathQueue(
    val queue: Queue<MockResponseSetting> = LinkedList(),
    var defaultResponse: MockResponseSetting? = null,
)

private fun MockResponseSetting.toMockResponse(assets: AssetManager): MockResponse {
    val responseBody = body ?: if (assetFile.isNotEmpty()) {
        assets.open(assetFile).bufferedReader().use { it.readText() }
    } else {
        ""
    }
    return MockResponse()
        .setResponseCode(code)
        .setBody(responseBody)
        .setBodyDelay(bodyDelayMs, TimeUnit.MILLISECONDS)
        .addHeader("Content-Type", "application/json")
}
