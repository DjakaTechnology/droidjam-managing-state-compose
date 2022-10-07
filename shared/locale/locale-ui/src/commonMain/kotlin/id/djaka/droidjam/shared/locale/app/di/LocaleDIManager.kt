package id.djaka.droidjam.shared.locale.app.di

import id.djaka.droidjam.shared.core.di.CoreComponent
import id.djaka.droidjam.shared.core.di.CoreDIManager
import id.djaka.droidjam.shared.locale.app.domain.CountryCodeRepository
import id.djaka.droidjam.shared.locale.app.domain.CountryPickerConverter
import id.djaka.droidjam.shared.locale.app.domain.GetSearchCountryPickerInitialStateUseCase
import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryCodeFilterUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases

object LocaleDIManager {
    val coreAppComponent = CoreDIManager.appComponent
    fun subComponent() = LocaleSubComponent(coreAppComponent)
}

class LocaleSubComponent(private val coreAppComponent: CoreComponent) {
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
}