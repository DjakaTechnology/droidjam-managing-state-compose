package id.djaka.droidjam.shared.core.ios.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.native.concurrent.freeze

class SuspendedWrapper<T>(private val suspender: suspend () -> T) {
    init {
        freeze()
    }
    fun subscribe(
        scope: CoroutineScope,
        onSuccess: (item: T) -> Unit,
        onThrow: (error: Throwable) -> Unit
    ): Job = scope.launch {
        try {
            onSuccess(suspender().freeze())
        } catch (error: Throwable) {
            onThrow(error.freeze())
        }
    }.freeze()
}