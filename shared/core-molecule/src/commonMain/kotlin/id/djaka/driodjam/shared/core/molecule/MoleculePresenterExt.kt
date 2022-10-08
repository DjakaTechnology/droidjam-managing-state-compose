package id.djaka.driodjam.shared.core.molecule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import id.djaka.driodjam.shared.core.molecule.lib.RecompositionClock
import id.djaka.droidjam.shared.core.framework.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

fun <T, E> Presenter<T, E>.launchMolecule(
    event: Flow<T>,
    coroutineScope: CoroutineScope,
    clock: RecompositionClock
) = present(coroutineScope, event)

@Composable
fun <T, E> Presenter<T, E>.rememberLaunchPresenter(): Pair<MutableSharedFlow<T>, E> {
    val event = remember { MutableSharedFlow<T>() }
    val coroutineScope = rememberCoroutineScope()
    val presenter by remember { present(coroutineScope, event) }.collectAsState()
    return remember(presenter) { Pair(event, presenter) }
}

@Composable
fun <T, E> Presenter<T, E>.rememberLaunchMoleculePresenter(): Pair<MutableSharedFlow<T>, StateFlow<E>> {
    val coroutineScope = rememberCoroutineScope()
    return remember {
        val event = MutableSharedFlow<T>()
        val presenter = launchMolecule(event, coroutineScope, RecompositionClock.ContextClock)
        Pair(event, presenter)
    }
}

@Composable
@Suppress("UnusedReceiverParameter")
fun <T, E> Presenter<T, E>.rememberEvent() = remember { MutableSharedFlow<T>() }