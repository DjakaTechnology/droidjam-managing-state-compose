package id.djaka.droidjam.shared.booking.presentation.api.model

class CouponModel(
    val code: String,
    val discountMessage: String,
    val isAvailable: Boolean,
    val discountType: DiscountType,
) {
    sealed class DiscountType {
        class PercentageDiscount(
            val percentage: Float,
            val maximumDiscount: Double? = null
        ) : DiscountType()

        class StaticDiscount(
            val discount: Double,
        ) : DiscountType()
    }
}