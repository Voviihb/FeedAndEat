package com.vk_edu.feed_and_eat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule : TestWatcher() {
    override fun starting(description: Description?) {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}