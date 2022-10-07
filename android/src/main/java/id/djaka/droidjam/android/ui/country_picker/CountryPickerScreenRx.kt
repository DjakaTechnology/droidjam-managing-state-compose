package id.djaka.droidjam.android.ui.country_picker

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import id.djaka.droidjam.shared.locale.app.di.LocaleDIManager
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.CountryPickerEvent
import id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant.toGenericModel
import id.djaka.droidjam.shared.locale.ui.country_picker.CountryPickerScreen
import io.reactivex.rxjava3.subjects.ReplaySubject
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asFlow

@Composable
fun CountryPickerScreenRx() {
    val presenter = remember { createPresenter() }
    val event = remember { ReplaySubject.create<CountryPickerEvent>() }
    val state by remember { presenter.presentRx(event).asFlow() }.collectAsState(id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel.empty())

    val coroutineScope = rememberCoroutineScope()
    Surface {
        CountryPickerScreen(state.toGenericModel()) { coroutineScope.launch { event.onNext(it) } }
    }
}

private fun createPresenter(): CountryPickerRxPresenter {
    val dependency = LocaleDIManager.subComponent()
    return CountryPickerRxPresenter(
        searchCountryUseCases = dependency.searchCountryUseCase,
        saveRecentCountryUseCase = dependency.saveRecentCountryUseCase
    )
}
