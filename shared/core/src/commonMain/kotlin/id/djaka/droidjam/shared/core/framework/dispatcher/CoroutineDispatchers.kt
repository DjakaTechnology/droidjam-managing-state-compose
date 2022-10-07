package id.djaka.droidjam.shared.core.framework.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

interface CoroutineDispatchers {
    fun main(): MainCoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}