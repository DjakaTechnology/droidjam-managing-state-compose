package id.djaka.droidjam.common.ui.booking.coupon_widget

import androidx.compose.runtime.*
import id.djaka.droidjam.common.domain.ApplyCouponUseCase
import id.djaka.droidjam.common.framework.MoleculePresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


class CouponPresenter constructor(
    private val applyCouponUseCase: ApplyCouponUseCase,
) : MoleculePresenter<CouponPresenter.Event, CouponPresenter.UIState> {
    @Composable
    override fun present(event: Flow<Event>): UIState {
        var coupon by remember { mutableStateOf("") }
        var couponValidation: UIState.ValidationResult by remember { mutableStateOf(UIState.ValidationResult.Idle) }

        if (coupon.isNotEmpty()) {
            LaunchedEffect(coupon) {
                couponValidation = UIState.ValidationResult.Loading
                delay(200) // Debounce
                couponValidation = fetchCouponValidation(coupon)
            }
        } else {
            couponValidation = UIState.ValidationResult.Idle
        }

        LaunchedEffect(Unit) {
            event.collect {
                when (it) {
                    is Event.CouponTextChanged -> coupon = it.text
                    is Event.ClearCoupon -> coupon = ""
                }
            }
        }

        return UIState(
            coupon = coupon,
            couponValidation = couponValidation
        )
    }

    private suspend fun fetchCouponValidation(coupon: String): UIState.ValidationResult {
        val coupon = applyCouponUseCase(coupon)
        return if (coupon.isSuccess && coupon.couponModel != null) {
            UIState.ValidationResult.Success("You got ${coupon.couponModel.discountMessage} discount!")
        } else {
            UIState.ValidationResult.Invalid(coupon.message)
        }
    }

    sealed class Event {
        class CouponTextChanged(val text: String) : Event()
        object ClearCoupon : Event()
    }

    @Immutable
    class UIState(
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
}