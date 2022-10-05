package id.djaka.droidjam.common.domain

import id.djaka.droidjam.common.model.ApplyCouponResult
import id.djaka.droidjam.common.repository.CouponRepository
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