package id.djaka.droidjam.shared.core.framework.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

actual class CoroutineDispatchersImpl actual constructor() : CoroutineDispatchers {
    override fun main(): MainCoroutineDispatcher {
        return Dispatchers.Main
    }

    override fun io(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    override fun default(): CoroutineDispatcher {
        return Dispatchers.Default
    }
}