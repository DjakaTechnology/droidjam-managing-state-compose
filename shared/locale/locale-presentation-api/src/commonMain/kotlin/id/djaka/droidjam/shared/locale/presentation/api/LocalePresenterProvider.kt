package id.djaka.droidjam.shared.locale.presentation.api

import id.djaka.droidjam.shared.core.framework.Presenter
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxModel
import id.djaka.droidjam.shared.locale.presentation.api.presenter.CountryPickerPresenter

interface LocalePresenterProvider {
    fun provideCountryPickerPresenter(): CountryPickerPresenter

    fun provideCountryPickerPresenterFlow(): CountryPickerPresenter

    fun provideCountryPickerFlowLikeRx(): Presenter<CountryPickerEvent, CountryPickerRxModel>
}