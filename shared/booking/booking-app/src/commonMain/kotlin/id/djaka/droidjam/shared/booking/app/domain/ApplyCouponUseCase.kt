package id.djaka.droidjam.shared.booking.app.domain

import id.djaka.droidjam.shared.booking.app.repository.CouponRepository
import id.djaka.droidjam.shared.booking.presentation.api.model.ApplyCouponResult
import kotlinx.coroutines.delay

class ApplyCouponUseCase(
    private val repository: CouponRepository
) {
    suspend operator fun invoke(code: String): ApplyCouponResult {
        delay(500) // Pretend API Call

        val coupon = repository.getCoupon(code)
        var isSuccess = false
        val message = when {
            coupon?.isAvailable == true -> {
                isSuccess = true
                "Success"
            }

            coupon?.isAvailable == false -> "Coupon us out of stock"
            else -> "Coupon not available"
        }
        return ApplyCouponResult(
            couponModel = coupon,
            message = message,
            isSuccess = isSuccess
        )
    }
}