package id.djaka.driodjam.shared.core.molecule

import androidx.compose.runtime.Composable
import id.djaka.driodjam.shared.core.molecule.lib.RecompositionClock
import id.djaka.driodjam.shared.core.molecule.lib.launchMolecule
import id.djaka.droidjam.shared.core.framework.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MoleculePresenter<E, T> : Presenter<E, T> {
    @Composable
    fun present(event: Flow<E>): T

    override fun present(coroutineScope: CoroutineScope, event: Flow<E>): StateFlow<T> {
        return coroutineScope.launchMolecule(RecompositionClock.Immediate) {
            present(event)
        }
    }
}