package id.djaka.droidjam.common.di

import id.djaka.droidjam.common.domain.ApplyCouponUseCase
import id.djaka.droidjam.common.domain.CalculatePriceBreakDownUseCase
import id.djaka.droidjam.common.domain.GetAddOnSelectorUseCase
import id.djaka.droidjam.common.repository.AddonRepository
import id.djaka.droidjam.common.repository.CouponRepository
import id.djaka.droidjam.common.repository.ProductRepository
import id.djaka.droidjam.common.ui.booking.addon_widget.AddonPresenter
import id.djaka.droidjam.common.ui.booking.coupon_widget.CouponPresenter
import id.djaka.droidjam.common.ui.booking.price_breakdown.PriceBreakDownPresenter
import id.djaka.droidjam.shared.core.di.CoreComponent
import id.djaka.droidjam.shared.core.di.CoreDIManager
import id.djaka.droidjam.shared.locale.app.domain.CountryCodeRepository
import id.djaka.droidjam.shared.locale.app.domain.CountryPickerConverter
import id.djaka.droidjam.shared.locale.app.domain.GetSearchCountryPickerInitialStateUseCase
import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryCodeFilterUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases

object AppDIManager {
    val appComponent = CoreDIManager.appComponent

    fun subComponent() = SubComponent(appComponent)
}

class SubComponent(appComponent: CoreComponent) {
    val preferenceRepository = appComponent.preferenceRepository
    val coroutineDispatchers = appComponent.coroutineDispatchers

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