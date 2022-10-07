package id.djaka.droidjam.common.ui.booking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import id.djaka.droidjam.common.ui.booking.addon_widget.AddonPresenter
import id.djaka.droidjam.common.ui.booking.booking_info_widget.BookingInfoPresenter
import id.djaka.droidjam.common.ui.booking.coupon_widget.CouponPresenter
import id.djaka.droidjam.common.ui.booking.price_breakdown.PriceBreakDownPresenter
import id.djaka.droidjam.shared.core.framework.Presenter
import id.djaka.droidjam.shared.core.framework.rememberLaunchPresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BookingPresenter(
    private val addonPresenter: AddonPresenter,
    private val bookingInfoPresenter: BookingInfoPresenter,
    private val couponPresenter: CouponPresenter,
    private val priceBreakDownPresenter: PriceBreakDownPresenter,
) : Presenter<BookingPresenter.Event, BookingPresenter.Model> {
    @Composable
    override fun present(event: Flow<Event>): Model {
        val coroutineScope = rememberCoroutineScope()

        val (addonEvent, addonState) = addonPresenter.rememberLaunchPresenter()
        val (bookingInfoEvent, bookingInfoState) = bookingInfoPresenter.rememberLaunchPresenter()
        val (couponEvent, couponState) = couponPresenter.rememberLaunchPresenter()
        val (priceEvent, priceState) = priceBreakDownPresenter.rememberLaunchPresenter()

        var submitResult: Model.SubmitResult by remember { mutableStateOf(Model.SubmitResult.Idle) }

        LaunchedEffect(couponState.couponValidation, bookingInfoState.productId, addonState.selectedAddon) {
            delay(200) // Debounce
            priceEvent.emit(
                PriceBreakDownPresenter.Event.UpdateSpec(
                    addonState.selectedAddon.map { it.id },
                    bookingInfoState.productId,
                    couponState.coupon,
                )
            )
        }

        LaunchedEffect(couponState, addonState, bookingInfoState, priceState) {
            event.collect {
                when (it) {
                    is Event.AddonEvent -> addonEvent.emit(it.event)
                    is Event.BookingInfoEvent -> bookingInfoEvent.emit(it.event)
                    is Event.CouponEvent -> couponEvent.emit(it.event)
                    is Event.PriceBreakdownEvent -> priceEvent.emit(it.event)

                    is Event.Submit -> coroutineScope.launch {
                        submitResult = Model.SubmitResult.Submitting

                        if (!it.forceSubmit) {
                            if (couponState.isValidationFailed) {
                                submitResult = Model.SubmitResult.RequireUserActionDialog(
                                    "Invalid Coupon",
                                    "Your coupon is invalid, do you want to clear and continue the booking?",
                                    "YES"
                                )
                                return@launch
                            }
                        }

                        delay(2000) // Pretend API Call
                        submitResult = Model.SubmitResult.Success
                    }

                    is Event.DismissRequireUserActionDialog -> {
                        submitResult = Model.SubmitResult.Idle
                    }

                    Event.DismissSuccessDialog -> submitResult = Model.SubmitResult.Idle
                }
            }
        }

        return Model(
            addonState = addonState,
            bookingInfoState = bookingInfoState,
            couponInfoState = couponState,
            priceBreakDownState = priceState,
            submitResult = submitResult,
        )
    }

    sealed class Event {
        data class AddonEvent(val event: AddonPresenter.Event) : Event()
        data class BookingInfoEvent(val event: BookingInfoPresenter.Event) : Event()
        data class CouponEvent(val event: CouponPresenter.Event) : Event()
        data class PriceBreakdownEvent(val event: PriceBreakDownPresenter.Event) : Event()
        data class Submit(val forceSubmit: Boolean = false) : Event()
        object DismissRequireUserActionDialog : Event()
        object DismissSuccessDialog : Event()
    }

    @Immutable
    data class Model(
        val addonState: AddonPresenter.Model,
        val bookingInfoState: BookingInfoPresenter.Model,
        val couponInfoState: CouponPresenter.Model,
        val priceBreakDownState: PriceBreakDownPresenter.Model,
        val submitResult: SubmitResult,
    ) {
        sealed class SubmitResult {
            object Idle : SubmitResult()
            object Success : SubmitResult()
            object Submitting : SubmitResult()

            class RequireUserActionDialog(
                val titleText: String,
                val messageText: String,
                val actionPrimary: String?
            ) : SubmitResult()
        }

        val isSubmitting = submitResult is SubmitResult.Submitting

        val userActionSubmitDialog = submitResult as? SubmitResult.RequireUserActionDialog

        val isSubmitEnabled = !couponInfoState.isValidationInProgress && priceBreakDownState.isSuccess

        val isSubmitCompleted = submitResult is SubmitResult.Success
    }
}