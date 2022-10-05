package id.djaka.droidjam.common.di

import id.djaka.droidjam.common.domain.*
import id.djaka.droidjam.common.domain.converter.GetSearchCountryPickerInitialStateUseCase
import id.djaka.droidjam.common.framework.CoroutineDispatchers
import id.djaka.droidjam.common.repository.*
import id.djaka.droidjam.common.ui.booking.addon_widget.AddonPresenter
import id.djaka.droidjam.common.ui.booking.coupon_widget.CouponPresenter
import id.djaka.droidjam.common.ui.booking.price_breakdown.PriceBreakDownPresenter
import id.djaka.droidjam.common.util.ResourceReader
import id.djaka.droidjam.common.util.createDBDriver
import id.djaka.droidjam.database.DroidJamDB

object CoreDIManager {
    val appComponent = AppComponent()

    fun subComponent() = SubComponent(appComponent)
}

class SubComponent(appComponent: AppComponent) {
    val preferenceRepository = appComponent.preferenceRepository
    val coroutineDispatchers = appComponent.coroutineDispatchers

    val countryCodeRepository by lazy {
        CountryCodeRepository(
            preferenceRepository,
            appComponent.droidJamDB
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

    val addonRepository by lazy {
        AddonRepository()
    }

    val getAddOnSelectorUseCase by lazy {
        GetAddOnSelectorUseCase(addonRepository)
    }

    val addonPresenter by lazy {
        AddonPresenter(getAddOnSelectorUseCase)
    }

    val couponRepository by lazy {
        CouponRepository()
    }

    val applyCouponUseCase by lazy {
        ApplyCouponUseCase(couponRepository)
    }

    val couponPresenter by lazy {
        CouponPresenter(applyCouponUseCase)
    }

    val productRepository by lazy {
        ProductRepository()
    }

    val calculatePriceBreakDownUseCase by lazy {
        CalculatePriceBreakDownUseCase(couponRepository, addonRepository, productRepository)
    }

    val priceBreakDownPresenter by lazy {
        PriceBreakDownPresenter(calculatePriceBreakDownUseCase)
    }
}

class AppComponent {
    val preferenceRepository = PreferenceRepository()
    val resourceReader = ResourceReader()

    val sqliteDriver by lazy { createDBDriver() }

    val droidJamDB by lazy { DroidJamDB(sqliteDriver.createDriver()) }

    val coroutineDispatchers by lazy { object : CoroutineDispatchers {} }
}