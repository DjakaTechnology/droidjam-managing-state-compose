package id.djaka.droidjam.common.ui.booking.coupon_widget

import androidx.compose.runtime.*
import id.djaka.driodjam.shared.core.molecule.MoleculePresenter
import id.djaka.droidjam.common.domain.ApplyCouponUseCase
import id.djaka.droidjam.shared.core.framework.Presenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


class CouponPresenter constructor(
    private val applyCouponUseCase: ApplyCouponUseCase,
) : MoleculePresenter<CouponPresenter.Event, CouponPresenter.Model> {
    @Composable
    override fun presentComposable(event: Flow<Event>): Model {
        var coupon by remember { mutableStateOf("") }
        var couponValidation: Model.ValidationResult by remember { mutableStateOf(Model.ValidationResult.Idle) }

        if (coupon.isNotEmpty()) {
            LaunchedEffect(coupon) {
                couponValidation = Model.ValidationResult.Loading
                delay(200) // Debounce
                couponValidation = fetchCouponValidation(coupon)
            }
        } else {
            couponValidation = Model.ValidationResult.Idle
        }

        LaunchedEffect(Unit) {
            event.collect {
                when (it) {
                    is Event.CouponTextChanged -> coupon = it.text
                    is Event.ClearCoupon -> coupon = ""
                }
            }
        }

        return Model(
            coupon = coupon,
            couponValidation = couponValidation
        )
    }

    private suspend fun fetchCouponValidation(coupon: String): Model.ValidationResult {
        val couponResult = applyCouponUseCase(coupon)
        return if (couponResult.isSuccess && couponResult.couponModel != null) {
            Model.ValidationResult.Success("You got ${couponResult.couponModel.discountMessage} discount!")
        } else {
            Model.ValidationResult.Invalid(couponResult.message)
        }
    }

    sealed class Event {
        class CouponTextChanged(val text: String) : Event()
        object ClearCoupon : Event()
    }

    @Immutable
    class Model(
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