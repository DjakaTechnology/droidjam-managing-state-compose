package id.djaka.droidjam.android.ui.country_picker

import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import id.djaka.droidjam.common.di.CoreDIManager
import id.djaka.droidjam.common.ui.country_picker.CountryPickerEvent
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter
import id.djaka.droidjam.common.ui.country_picker.CountryPickerScreen
import id.djaka.droidjam.common.ui.country_picker.variant.CountryPickerRxModel
import id.djaka.droidjam.common.ui.country_picker.variant.toGenericModel
import io.reactivex.rxjava3.subjects.ReplaySubject
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asFlow

@Composable
fun CountryPickerScreenRx() {
    val presenter = remember { createPresenter() }
    val event = remember { ReplaySubject.create<CountryPickerEvent>() }
    val state by remember { presenter.presentRx(event).asFlow() }.collectAsState(CountryPickerRxModel.empty())

    val coroutineScope = rememberCoroutineScope()
    Surface {
        CountryPickerScreen(state.toGenericModel()) { coroutineScope.launch { event.onNext(it) } }
    }
}

private fun createPresenter(): CountryPickerRxPresenter {
    val dependency = CoreDIManager.subComponent()
    return CountryPickerRxPresenter(
        searchCountryUseCases = dependency.searchCountryUseCase,
        saveRecentCountryUseCase = dependency.saveRecentCountryUseCase
    )
}
