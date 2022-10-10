@file:OptIn(ExperimentalCoroutinesApi::class)

package common.framework

import id.djaka.droidjam.shared.core.framework.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.setMain

fun mockCoroutineDispatcher(testDispatcher: TestDispatcher): CoroutineDispatchers {
    Dispatchers.setMain(testDispatcher)
    return object : CoroutineDispatchers {
        override fun default(): CoroutineDispatcher {
            return testDispatcher
        }

        override fun main(): MainCoroutineDispatcher {
            return Dispatchers.Main
        }

        override fun io(): CoroutineDispatcher {
            return testDispatcher
        }
    }
}