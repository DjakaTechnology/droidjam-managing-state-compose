package id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon

class CouponModel(
    val coupon: String,
    val couponValidation: ValidationResult,
) {
    sealed class ValidationResult {
        object Idle : ValidationResult()
        object Loading : ValidationResult()
        data class Success(val discountMessage: String) : ValidationResult()
        data class Invalid(val message: String) : ValidationResult()
    }

    val isValidationInProgress = couponValidation is ValidationResult.Loading

    val isValidationFailed = couponValidation is ValidationResult.Invalid
}