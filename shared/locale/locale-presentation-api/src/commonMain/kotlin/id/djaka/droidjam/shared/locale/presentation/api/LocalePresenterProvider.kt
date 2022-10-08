package id.djaka.droidjam.shared.locale.presentation.api

import id.djaka.droidjam.shared.core.framework.Presenter
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxModel

interface LocalePresenterProvider {
    fun provideCountryPickerPresenter(): Presenter<CountryPickerEvent, CountryPickerModel>

    fun provideCountryPickerPresenterFlow(): Presenter<CountryPickerEvent, CountryPickerModel>

    fun provideCountryPickerFlowLikeRx(): Presenter<CountryPickerEvent, CountryPickerRxModel>
}