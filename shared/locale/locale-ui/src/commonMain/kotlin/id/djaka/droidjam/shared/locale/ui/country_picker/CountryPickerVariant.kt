package id.djaka.droidjam.shared.locale.ui.country_picker

import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import id.djaka.driodjam.shared.core.molecule.rememberLaunchMoleculePresenter
import id.djaka.driodjam.shared.core.molecule.rememberLaunchPresenter
import id.djaka.droidjam.shared.core.framework.Presenter
import id.djaka.droidjam.shared.locale.presentation.api.di.LocalePresentationApiDIManager
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.toGenericModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun CountryPickerScreen() {
    val presenter = remember { createPresenter() }
    val (event, state) = presenter.rememberLaunchPresenter()

    val coroutineScope = rememberCoroutineScope()
    Surface {
        CountryPickerScreen(state) { coroutineScope.launch { event.emit(it) } }
    }
}

@Composable
fun CountryPickerScreenMolecule() {
    val presenter = remember { createPresenter() }
    val (event, stateFlow) = presenter.rememberLaunchMoleculePresenter()
    val state = stateFlow.collectAsState().value

    val coroutineScope = rememberCoroutineScope()
    Surface {
        CountryPickerScreen(state) { coroutineScope.launch { event.emit(it) } }
    }
}

@Composable
fun CountryPickerScreenFlow() {
    val coroutineScope = rememberCoroutineScope()
    val presenter = remember { createFlowPresenter() }
    val event = remember { MutableSharedFlow<CountryPickerEvent>() }
    val state by remember { presenter.present(coroutineScope, event) }.collectAsState()
    Surface {
        CountryPickerScreen(state) { coroutineScope.launch { event.emit(it) } }
    }
}

@Composable
fun CountryPickerScreenFlowLikeRx() {
    val coroutineScope = rememberCoroutineScope()
    val presenter = remember { createFlowLikeRx() }
    val event = remember { MutableSharedFlow<CountryPickerEvent>() }
    val state by remember { presenter.present(coroutineScope, event) }.collectAsState()
    Surface {
        CountryPickerScreen(state.toGenericModel()) { coroutineScope.launch { event.emit(it) } }
    }
}

private fun createPresenter(): Presenter<CountryPickerEvent, CountryPickerModel> {
    return LocalePresentationApiDIManager.presenterProvider.provideCountryPickerPresenter()
}

private fun createFlowPresenter(): Presenter<CountryPickerEvent, CountryPickerModel> {
    return LocalePresentationApiDIManager.presenterProvider.provideCountryPickerPresenterFlow()
}

private fun createFlowLikeRx(): Presenter<CountryPickerEvent, CountryPickerRxModel> {
    return LocalePresentationApiDIManager.presenterProvider.provideCountryPickerFlowLikeRx()
}