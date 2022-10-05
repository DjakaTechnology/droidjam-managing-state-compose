package id.djaka.droidjam.common.framework

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MoleculePresenter<T, E> {
    @Composable
    fun present(event: Flow<T>): E
}

fun <T, E> MoleculePresenter<T, E>.launchMolecule(
    event: Flow<T>,
    coroutineScope: CoroutineScope,
    clock: RecompositionClock
) = coroutineScope.launchMolecule(clock) { present(event) }

@Composable
fun <T, E> MoleculePresenter<T, E>.rememberLaunchPresenter(): Pair<MutableSharedFlow<T>, E> {
    val event = remember { MutableSharedFlow<T>() }
    val presenter = present(event)
    return remember(presenter) { Pair(event, presenter) }
}

@Composable
fun <T, E> MoleculePresenter<T, E>.rememberLaunchMoleculePresenter(): Pair<MutableSharedFlow<T>, StateFlow<E>> {
    val coroutineScope = rememberCoroutineScope()
    return remember {
        val event = MutableSharedFlow<T>()
        val presenter = launchMolecule(event, coroutineScope, RecompositionClock.ContextClock)
        Pair(event, presenter)
    }
}

@Composable
@Suppress("UnusedReceiverParameter")
fun <T, E> MoleculePresenter<T, E>.rememberEvent() = remember { MutableSharedFlow<T>() }

fun <T, E> MoleculePresenter<T, E>.launchMolecule(
    coroutineScope: CoroutineScope,
    clock: RecompositionClock
): Pair<MutableSharedFlow<T>, StateFlow<E>> {
    val event = MutableSharedFlow<T>()
    val presenter = coroutineScope.launchMolecule(clock) { present(event) }
    return Pair(event, presenter)
}