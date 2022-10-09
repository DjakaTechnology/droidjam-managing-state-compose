package id.djaka.droidjam.shared.booking.app.ui.booking.coupon_widget

import androidx.compose.runtime.*
import id.djaka.driodjam.shared.core.molecule.MoleculePresenter
import id.djaka.droidjam.shared.booking.app.domain.ApplyCouponUseCase
import id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponPresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


class CouponPresenterImpl constructor(
    private val applyCouponUseCase: ApplyCouponUseCase,
) : CouponPresenter, MoleculePresenter<CouponEvent, CouponModel> {
    @Composable
    override fun present(event: Flow<CouponEvent>): CouponModel {
        var coupon by remember { mutableStateOf("") }
        var couponValidation: CouponModel.ValidationResult by remember { mutableStateOf(CouponModel.ValidationResult.Idle) }

        if (coupon.isNotEmpty()) {
            LaunchedEffect(coupon) {
                couponValidation = CouponModel.ValidationResult.Loading
                delay(200) // Debounce
                couponValidation = fetchCouponValidation(coupon)
            }
        } else {
            couponValidation = CouponModel.ValidationResult.Idle
        }

        LaunchedEffect(Unit) {
            event.collect {
                when (it) {
                    is CouponEvent.CouponTextChanged -> coupon = it.text
                    is CouponEvent.ClearCoupon -> coupon = ""
                }
            }
        }

        return CouponModel(
            coupon = coupon,
            couponValidation = couponValidation
        )
    }

    private suspend fun fetchCouponValidation(coupon: String): CouponModel.ValidationResult {
        val couponResult = applyCouponUseCase(coupon)
        val couponModel = couponResult.couponModel
        return if (couponResult.isSuccess && couponModel != null) {
            CouponModel.ValidationResult.Success("You got ${couponModel.discountMessage} discount!")
        } else {
            CouponModel.ValidationResult.Invalid(couponResult.message)
        }
    }
}