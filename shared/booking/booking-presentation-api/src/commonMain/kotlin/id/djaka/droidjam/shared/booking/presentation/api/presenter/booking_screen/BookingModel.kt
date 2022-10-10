package id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen

import id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_info.BookingInfoModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown.PriceBreakDownModel

data class BookingModel(
    val addonState: AddonModel,
    val bookingInfoState: BookingInfoModel,
    val couponInfoState: CouponModel,
    val priceBreakDownState: PriceBreakDownModel,
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