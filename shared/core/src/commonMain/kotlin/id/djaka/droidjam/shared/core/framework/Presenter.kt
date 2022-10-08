package id.djaka.droidjam.shared.core.framework

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Presenter<T, E> {
    fun present(coroutineScope: CoroutineScope, event: Flow<T>): StateFlow<E>
}