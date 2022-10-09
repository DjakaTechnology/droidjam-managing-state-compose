package id.djaka.droidjam.shared.booking.app.di

import id.djaka.droidjam.shared.booking.app.domain.ApplyCouponUseCase
import id.djaka.droidjam.shared.booking.app.domain.CalculatePriceBreakDownUseCase
import id.djaka.droidjam.shared.booking.app.domain.GetAddOnSelectorUseCase
import id.djaka.droidjam.shared.booking.app.repository.AddonRepository
import id.djaka.droidjam.shared.booking.app.repository.CouponRepository
import id.djaka.droidjam.shared.booking.app.repository.ProductRepository
import id.djaka.droidjam.shared.booking.app.ui.booking.BookingPresenterImpl
import id.djaka.droidjam.shared.booking.app.ui.booking.addon_widget.AddonPresenterImpl
import id.djaka.droidjam.shared.booking.app.ui.booking.booking_info_widget.BookingInfoPresenterImpl
import id.djaka.droidjam.shared.booking.app.ui.booking.coupon_widget.CouponPresenterImpl
import id.djaka.droidjam.shared.booking.app.ui.booking.price_breakdown.PriceBreakDownPresenterImpl
import id.djaka.droidjam.shared.booking.presentation.api.BookingPresenterProvider
import id.djaka.droidjam.shared.booking.presentation.api.di.BookingPresentationApiDIManager
import id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonPresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_info.BookingInfoPresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen.BookingPresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponPresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown.PriceBreakdownPresenter
import id.djaka.droidjam.shared.core.di.CoreDIManager

object BookingDIManager {
    lateinit var appComponent: BookingAppComponent
        private set

    fun init() {
        if (!this::appComponent.isInitialized) {
            appComponent = BookingAppComponent(
                BookingPresenterProvider()
            )
            BookingPresentationApiDIManager.presenterProvider = appComponent.bookingPresenterProvider
        }
    }

    fun subComponent() = SubComponent(appComponent)
}

class BookingAppComponent(
    val bookingPresenterProvider: BookingPresenterProvider,
) {
    val coroutineDispatchers = CoreDIManager.appComponent.coroutineDispatchers

    val addonRepository by lazy {
        AddonRepository(coroutineDispatchers)
    }

    val getAddOnSelectorUseCase by lazy {
        GetAddOnSelectorUseCase(addonRepository)
    }

    val couponRepository by lazy {
        CouponRepository()
    }

    val applyCouponUseCase by lazy {
        ApplyCouponUseCase(couponRepository)
    }

    val productRepository by lazy {
        ProductRepository()
    }

    val calculatePriceBreakDownUseCase by lazy {
        CalculatePriceBreakDownUseCase(couponRepository, addonRepository, productRepository)
    }
}

class SubComponent(private val appComponent: BookingAppComponent) {
    val addonPresenter by lazy {
        AddonPresenterImpl(appComponent.getAddOnSelectorUseCase)
    }

    val priceBreakDownPresenter by lazy {
        PriceBreakDownPresenterImpl(appComponent.calculatePriceBreakDownUseCase)
    }

    val bookingInfoPresenter by lazy {
        BookingInfoPresenterImpl()
    }

    val couponPresenter by lazy {
        CouponPresenterImpl(appComponent.applyCouponUseCase)
    }
}

class BookingPresenterProvider() : BookingPresenterProvider {
    override fun provideBookingPresenter(): BookingPresenter {
        val subComponent = BookingDIManager.subComponent()
        return BookingPresenterImpl(
            addonPresenter = subComponent.addonPresenter,
            bookingInfoPresenter = subComponent.bookingInfoPresenter,
            couponPresenter = subComponent.couponPresenter,
            priceBreakDownPresenter = subComponent.priceBreakDownPresenter,
        )
    }

    override fun provideAddonPresenter(): AddonPresenter {
        val subComponent = BookingDIManager.subComponent()
        return subComponent.addonPresenter
    }

    override fun provideBookingInfoPresenter(): BookingInfoPresenter {
        val subComponent = BookingDIManager.subComponent()
        return subComponent.bookingInfoPresenter
    }

    override fun provideCouponPresenter(): CouponPresenter {
        val subComponent = BookingDIManager.subComponent()
        return subComponent.couponPresenter
    }

    override fun providePriceBreakdownPresenter(): PriceBreakdownPresenter {
        val subComponent = BookingDIManager.subComponent()
        return subComponent.priceBreakDownPresenter
    }

}