package id.djaka.droidjam.shared.booking.app.domain

import id.djaka.droidjam.shared.booking.app.repository.AddonRepository
import id.djaka.droidjam.shared.booking.app.repository.CouponRepository
import id.djaka.droidjam.shared.booking.app.repository.ProductRepository
import id.djaka.droidjam.shared.booking.presentation.api.model.CouponModel
import id.djaka.droidjam.shared.booking.presentation.api.model.PriceBreakDownDataModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.math.max

class CalculatePriceBreakDownUseCase(
    private val couponRepository: CouponRepository,
    private val addonRepository: AddonRepository,
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(productId: String, couponCode: String, addons: List<String>): PriceBreakDownDataModel {
        val setOfSelectedAddon = addons.toSet()
        val (product, coupon, addonList) = combine(
            flow { emit(productRepository.getProduct(productId)) },
            flow { emit(couponRepository.getCoupon(couponCode)) },
            flow { emit(addonRepository.getList()) }
                .map {
                    it.filter {
                        setOfSelectedAddon.contains(it.id)
                    }
                },
        ) { product, coupon, addonList ->
            Triple(product, coupon, addonList)
        }.first()

        val items = mutableListOf<PriceBreakDownDataModel.Item>()
        items.add(
            PriceBreakDownDataModel.Item(
                product.name,
                product.price,
            )
        )

        addonList.forEach {
            items.add(
                PriceBreakDownDataModel.Item(
                    it.title,
                    it.price,
                )
            )
        }

        val totalPrice = items.sumOf { it.sellingPrice }
        val discount = mutableListOf<PriceBreakDownDataModel.Item>()
        if (coupon != null) {
            discount.add(
                PriceBreakDownDataModel.Item(
                    name = coupon.code,
                    sellingPrice = when (val discountType = coupon.discountType) {
                        is CouponModel.DiscountType.PercentageDiscount -> {
                            totalPrice - max((totalPrice * discountType.percentage), discountType.maximumDiscount ?: 0.0)
                        }

                        is CouponModel.DiscountType.StaticDiscount -> {
                            max(0.0, discountType.discount)
                        }
                    }
                )
            )
        }

        return PriceBreakDownDataModel(
            items = items,
            discount = discount,
            totalSellingPrice = totalPrice - (discount.sumOf { it.sellingPrice }),
            totalStrikethroughPrice = totalPrice
        )
    }
}