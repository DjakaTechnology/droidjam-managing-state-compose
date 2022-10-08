package id.djaka.droidjam.shared.core.ios.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlin.native.concurrent.freeze

class FlowWrapper<T>(private val flow: Flow<T>) {
    init {
        freeze()
    }

    fun subscribe(
        scope: CoroutineScope,
        onEach: (item: T) -> Unit,
        onComplete: () -> Unit,
        onThrow: (error: Throwable) -> Unit
    ): Job = flow
        .onEach { onEach(it.freeze()) }
        .catch { onThrow(it.freeze()) }
        .onCompletion { onComplete() }
        .launchIn(scope)
        .freeze()

    internal fun getFlow() = flow
}