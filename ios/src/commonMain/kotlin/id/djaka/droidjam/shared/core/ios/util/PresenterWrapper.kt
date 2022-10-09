package id.djaka.droidjam.shared.core.ios.util

import id.djaka.droidjam.shared.core.framework.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class PresenterWrapper<E, T>(
    val presenter: Presenter<E, T>,
) {
    val coroutineScope: CoroutineScope = MainScope()
    private val events: MutableSharedFlow<E> = MutableSharedFlow()

    fun listen(): FlowWrapper<T> {
        return FlowWrapper(presenter.present(coroutineScope, events))
    }

    fun event(value: E) {
        coroutineScope.launch {
            events.emit(value)
        }
    }

    fun destroy() {
        coroutineScope.cancel()
    }
}

