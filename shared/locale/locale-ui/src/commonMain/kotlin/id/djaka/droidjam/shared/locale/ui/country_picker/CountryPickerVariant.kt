package id.djaka.droidjam.shared.locale.ui.country_picker

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import id.djaka.droidjam.shared.core.framework.rememberLaunchPresenter
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant.CountryPickerFlowLikeRxPresenter
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant.CountryPickerFlowPresenter
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant.toGenericModel
import id.djaka.droidjam.shared.core.framework.rememberLaunchMoleculePresenter
import id.djaka.droidjam.shared.locale.app.di.LocaleDIManager
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.CountryPickerPresenter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun CountryPickerScreen() {
    CountryPickerScreenComposable()
//    CountryPickerScreenMolecule()
//    CountryPickerScreenFlow()
}

@Composable
fun CountryPickerScreenComposable() {
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
    val state by remember { presenter.presentFlow(coroutineScope, event) }.collectAsState()
    Surface {
        CountryPickerScreen(state) { coroutineScope.launch { event.emit(it) } }
    }
}

@Composable
fun CountryPickerScreenFlowLikeRx() {
    val coroutineScope = rememberCoroutineScope()
    val presenter = remember { createFlowLikeRx() }
    val event = remember { MutableSharedFlow<CountryPickerEvent>() }
    val state by remember { presenter.presentFlow(coroutineScope, event) }.collectAsState()
    Surface {
        CountryPickerScreen(state.toGenericModel()) { coroutineScope.launch { event.emit(it) } }
    }
}

private fun createPresenter(): CountryPickerPresenter {
    val dependency = LocaleDIManager.subComponent()
    return CountryPickerPresenter(
        searchCountryUseCases = dependency.searchCountryUseCase,
        saveRecentCountryUseCase = dependency.saveRecentCountryUseCase
    )
}

private fun createFlowPresenter(): CountryPickerFlowPresenter {
    val dependency = LocaleDIManager.subComponent()
    return CountryPickerFlowPresenter(
        searchCountryUseCases = dependency.searchCountryUseCase,
        saveRecentCountryUseCase = dependency.saveRecentCountryUseCase
    )
}

private fun createFlowLikeRx(): CountryPickerFlowLikeRxPresenter {
    val dependency = LocaleDIManager.subComponent()
    return CountryPickerFlowLikeRxPresenter(
        searchCountryUseCases = dependency.searchCountryUseCase,
        saveRecentCountryUseCase = dependency.saveRecentCountryUseCase
    )
}