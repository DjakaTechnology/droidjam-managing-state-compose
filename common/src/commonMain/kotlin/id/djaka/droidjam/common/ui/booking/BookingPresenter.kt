package id.djaka.droidjam.common.ui.booking

import androidx.compose.runtime.*
import id.djaka.droidjam.common.framework.MoleculePresenter
import id.djaka.droidjam.common.framework.rememberLaunchPresenter
import id.djaka.droidjam.common.ui.booking.addon_widget.AddonPresenter
import id.djaka.droidjam.common.ui.booking.booking_info_widget.BookingInfoPresenter
import id.djaka.droidjam.common.ui.booking.coupon_widget.CouponPresenter
import id.djaka.droidjam.common.ui.booking.price_breakdown.PriceBreakDownPresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BookingPresenter(
    private val addonPresenter: AddonPresenter,
    private val bookingInfoPresenter: BookingInfoPresenter,
    private val couponPresenter: CouponPresenter,
    private val priceBreakDownPresenter: PriceBreakDownPresenter,
) : MoleculePresenter<BookingPresenter.Event, BookingPresenter.UIState> {
    @Composable
    override fun present(event: Flow<Event>): UIState {
        val coroutineScope = rememberCoroutineScope()

        val (addonEvent, addonState) = addonPresenter.rememberLaunchPresenter()
        val (bookingInfoEvent, bookingInfoState) = bookingInfoPresenter.rememberLaunchPresenter()
        val (couponEvent, couponState) = couponPresenter.rememberLaunchPresenter()
        val (priceEvent, priceState) = priceBreakDownPresenter.rememberLaunchPresenter()

        var submitResult: UIState.SubmitResult by remember { mutableStateOf(UIState.SubmitResult.Idle) }

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
                        submitResult = UIState.SubmitResult.Submitting

                        if (!it.forceSubmit) {
                            if (couponState.isValidationFailed) {
                                submitResult = UIState.SubmitResult.RequireUserActionDialog(
                                    "Invalid Coupon",
                                    "Your coupon is invalid, do you want to clear and continue the booking?",
                                    actionPrimary = "YES"
                                )
                                return@launch
                            }
                        }

                        delay(2000) // Pretend API Call
                        submitResult = UIState.SubmitResult.Success
                    }

                    is Event.DismissRequireUserActionDialog -> {
                        submitResult = UIState.SubmitResult.Idle
                    }

                    Event.DismissSuccessDialog -> submitResult = UIState.SubmitResult.Idle
                }
            }
        }

        return UIState(
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
    data class UIState(
        val addonState: AddonPresenter.UIState,
        val bookingInfoState: BookingInfoPresenter.UIState,
        val couponInfoState: CouponPresenter.UIState,
        val priceBreakDownState: PriceBreakDownPresenter.UIState,
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