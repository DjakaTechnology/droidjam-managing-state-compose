package id.djaka.droidjam.android.ui.country_picker

import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import id.djaka.droidjam.common.di.CoreDIManager
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter
import id.djaka.droidjam.common.ui.country_picker.CountryPickerScreen
import io.reactivex.rxjava3.subjects.ReplaySubject
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asFlow

@Composable
fun CountryPickerScreenRx() {
    val presenter = remember { createPresenter() }
    val event = remember { ReplaySubject.create<CountryPickerPresenter.Event>() }
    val state by remember { presenter.presentRx(event).asFlow() }.collectAsState(CountryPickerPresenter.UIState.empty())

    val coroutineScope = rememberCoroutineScope()
    Surface {
        CountryPickerScreen(state) { coroutineScope.launch { event.onNext(it) } }
    }
}

private fun createPresenter(): CountryPickerRxPresenter {
    val dependency = CoreDIManager.subComponent()
    return CountryPickerRxPresenter(
        searchCountryUseCases = dependency.searchCountryUseCase,
        saveRecentCountryUseCase = dependency.saveRecentCountryUseCase
    )
}
