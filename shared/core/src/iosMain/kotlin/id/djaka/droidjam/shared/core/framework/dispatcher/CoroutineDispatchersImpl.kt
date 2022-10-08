package id.djaka.droidjam.shared.core.framework.dispatcher

import id.djaka.droidjam.shared.core.framework.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

actual class CoroutineDispatchersImpl: CoroutineDispatchers {
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