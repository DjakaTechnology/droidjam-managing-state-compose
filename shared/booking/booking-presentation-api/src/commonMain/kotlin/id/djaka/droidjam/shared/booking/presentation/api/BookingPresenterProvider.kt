package id.djaka.droidjam.shared.booking.presentation.api

import id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonPresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_info.BookingInfoPresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen.BookingPresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponPresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown.PriceBreakdownPresenter

interface BookingPresenterProvider {
    fun provideBookingPresenter(): BookingPresenter

    fun provideAddonPresenter(): AddonPresenter

    fun provideBookingInfoPresenter(): BookingInfoPresenter

    fun provideCouponPresenter(): CouponPresenter

    fun providePriceBreakdownPresenter(): PriceBreakdownPresenter
}