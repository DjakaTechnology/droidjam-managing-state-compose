package id.djaka.droidjam.shared.booking.app.repository

import id.djaka.droidjam.shared.booking.presentation.api.model.CouponModel

class CouponRepository {
    fun getCoupon(): List<CouponModel> {
        return listOf(
            CouponModel(
                code = "P50",
                discountMessage = "50% Off",
                isAvailable = true,
                discountType = CouponModel.DiscountType.PercentageDiscount(0.5f)
            ),
            CouponModel(
                code = "S5",
                discountMessage = "$5 off",
                isAvailable = true,
                discountType = CouponModel.DiscountType.StaticDiscount(5.0)
            ),
            CouponModel(
                code = "OOS",
                discountMessage = "$5 off",
                isAvailable = false,
                discountType = CouponModel.DiscountType.StaticDiscount(5.0)
            )
        )
    }

    fun getCoupon(code: String): CouponModel? {
        return getCoupon().firstOrNull { it.code.equals(code, ignoreCase = true) }
    }
}