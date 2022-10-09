package id.djaka.droidjam.shared.booking.app.ui.booking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import id.djaka.driodjam.shared.core.molecule.MoleculePresenter
import id.djaka.driodjam.shared.core.molecule.rememberLaunchPresenter
import id.djaka.droidjam.shared.booking.app.ui.booking.addon_widget.AddonPresenterImpl
import id.djaka.droidjam.shared.booking.app.ui.booking.booking_info_widget.BookingInfoPresenterImpl
import id.djaka.droidjam.shared.booking.app.ui.booking.coupon_widget.CouponPresenterImpl
import id.djaka.droidjam.shared.booking.app.ui.booking.price_breakdown.PriceBreakDownPresenterImpl
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen.BookingEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen.BookingModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen.BookingPresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown.PriceBreakDownEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BookingPresenterImpl(
    private val addonPresenter: AddonPresenterImpl,
    private val bookingInfoPresenter: BookingInfoPresenterImpl,
    private val couponPresenter: CouponPresenterImpl,
    private val priceBreakDownPresenter: PriceBreakDownPresenterImpl,
) : BookingPresenter, MoleculePresenter<BookingEvent, BookingModel> {
    @Composable
    override fun present(event: Flow<BookingEvent>): BookingModel {
        val coroutineScope = rememberCoroutineScope()

        val (addonEvent, addonState) = addonPresenter.rememberLaunchPresenter()
        val (bookingInfoEvent, bookingInfoState) = bookingInfoPresenter.rememberLaunchPresenter()
        val (couponEvent, couponState) = couponPresenter.rememberLaunchPresenter()
        val (priceEvent, priceState) = priceBreakDownPresenter.rememberLaunchPresenter()

        var submitResult: BookingModel.SubmitResult by remember { mutableStateOf(BookingModel.SubmitResult.Idle) }

        LaunchedEffect(couponState.couponValidation, bookingInfoState.productId, addonState.selectedAddon) {
            delay(200) // Debounce
            priceEvent.emit(
                PriceBreakDownEvent.UpdateSpec(
                    addonState.selectedAddon.map { it.id },
                    bookingInfoState.productId,
                    couponState.coupon,
                )
            )
        }

        LaunchedEffect(couponState, addonState, bookingInfoState, priceState) {
            event.collect {
                when (it) {
                    is BookingEvent.SendAddonEvent -> addonEvent.emit(it.event)
                    is BookingEvent.SendBookingInfoEvent -> bookingInfoEvent.emit(it.event)
                    is BookingEvent.SendCouponEvent -> couponEvent.emit(it.event)
                    is BookingEvent.SendPriceBreakdownEvent -> priceEvent.emit(it.event)

                    is BookingEvent.Submit -> coroutineScope.launch {
                        submitResult = BookingModel.SubmitResult.Submitting

                        if (!it.forceSubmit) {
                            if (couponState.isValidationFailed) {
                                submitResult = BookingModel.SubmitResult.RequireUserActionDialog(
                                    "Invalid Coupon",
                                    "Your coupon is invalid, do you want to clear and continue the booking?",
                                    "YES"
                                )
                                return@launch
                            }
                        }

                        delay(2000) // Pretend API Call
                        submitResult = BookingModel.SubmitResult.Success
                    }

                    is BookingEvent.DismissRequireUserActionDialog -> {
                        submitResult = BookingModel.SubmitResult.Idle
                    }

                    BookingEvent.DismissSuccessDialog -> submitResult = BookingModel.SubmitResult.Idle
                }
            }
        }

        return BookingModel(
            addonState = addonState,
            bookingInfoState = bookingInfoState,
            couponInfoState = couponState,
            priceBreakDownState = priceState,
            submitResult = submitResult,
        )
    }


}