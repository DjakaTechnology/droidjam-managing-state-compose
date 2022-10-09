package id.djaka.droidjam.shared.locale.app.di

import id.djaka.droidjam.shared.core.di.CoreComponent
import id.djaka.droidjam.shared.core.di.CoreDIManager
import id.djaka.droidjam.shared.core.framework.Presenter
import id.djaka.droidjam.shared.locale.app.domain.CountryCodeRepository
import id.djaka.droidjam.shared.locale.app.domain.CountryPickerConverter
import id.djaka.droidjam.shared.locale.app.domain.GetSearchCountryPickerInitialStateUseCase
import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryCodeFilterUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.CountryPickerPresenterImpl
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant.CountryPickerFlowLikeRxPresenter
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant.CountryPickerFlowPresenter
import id.djaka.droidjam.shared.locale.presentation.api.LocalePresenterProvider
import id.djaka.droidjam.shared.locale.presentation.api.di.LocalePresentationApiDIManager
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxModel
import id.djaka.droidjam.shared.locale.presentation.api.presenter.CountryPickerPresenter

object LocaleDIManager {
    val api = LocalePresentationApiDIManager
    val coreAppComponent = CoreDIManager.appComponent

    fun subComponent() = LocaleSubComponent(coreAppComponent, api)

    fun init() {
        api.presenterProvider = LocalePresenterProviderImpl(this)
    }
}

class LocaleSubComponent(
    private val coreAppComponent: CoreComponent,
    private val api: LocalePresentationApiDIManager
) {
    val preferenceRepository = coreAppComponent.preferenceRepository
    val coroutineDispatchers = coreAppComponent.coroutineDispatchers

    val countryCodeRepository by lazy {
        CountryCodeRepository(
            preferenceRepository,
            coreAppComponent.droidJamDB
        )
    }

    val convertCountryCodeModelToCountryPickerItemUseCase by lazy {
        CountryPickerConverter()
    }

    val searchCountryCodeFilterUseCase by lazy {
        SearchCountryCodeFilterUseCase(
            countryCodeRepository,
            convertCountryCodeModelToCountryPickerItemUseCase,
            coroutineDispatchers
        )
    }

    val getSearchCountryCodeInitialStateUseCase by lazy {
        GetSearchCountryPickerInitialStateUseCase(
            countryCodeRepository,
            convertCountryCodeModelToCountryPickerItemUseCase
        )
    }

    val searchCountryUseCase by lazy {
        SearchCountryUseCases(searchCountryCodeFilterUseCase, getSearchCountryCodeInitialStateUseCase)
    }

    val saveRecentCountryUseCase by lazy {
        SaveRecentCountryUseCase(countryCodeRepository)
    }

    val presentationProvider by lazy {
        api.presenterProvider
    }
}

class LocalePresenterProviderImpl(
    private val localeDIManager: LocaleDIManager,
) : LocalePresenterProvider {
    override fun provideCountryPickerPresenter(): CountryPickerPresenter {
        val component = localeDIManager.subComponent()
        return CountryPickerPresenterImpl(
            searchCountryUseCases = component.searchCountryUseCase,
            saveRecentCountryUseCase = component.saveRecentCountryUseCase
        )
    }

    override fun provideCountryPickerPresenterFlow(): CountryPickerPresenter {
        val component = localeDIManager.subComponent()
        return CountryPickerFlowPresenter(
            searchCountryUseCases = component.searchCountryUseCase,
            saveRecentCountryUseCase = component.saveRecentCountryUseCase
        )
    }

    override fun provideCountryPickerFlowLikeRx(): Presenter<CountryPickerEvent, CountryPickerRxModel> {
        val component = localeDIManager.subComponent()
        return CountryPickerFlowLikeRxPresenter(
            searchCountryUseCases = component.searchCountryUseCase,
            saveRecentCountryUseCase = component.saveRecentCountryUseCase
        )
    }

}