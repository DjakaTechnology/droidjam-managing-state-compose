package id.djaka.droidjam.common.domain

import id.djaka.droidjam.common.model.CouponModel
import id.djaka.droidjam.common.model.PriceBreakDownModel
import id.djaka.droidjam.common.repository.AddonRepository
import id.djaka.droidjam.common.repository.CouponRepository
import id.djaka.droidjam.common.repository.ProductRepository
import kotlinx.coroutines.flow.*
import kotlin.math.max

class CalculatePriceBreakDownUseCase(
    private val couponRepository: CouponRepository,
    private val addonRepository: AddonRepository,
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(productId: String, couponCode: String, addons: List<String>): PriceBreakDownModel {
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

        val items = mutableListOf<PriceBreakDownModel.Item>()
        items.add(
            PriceBreakDownModel.Item(
                product.name,
                product.price,
            )
        )

        addonList.forEach {
            items.add(
                PriceBreakDownModel.Item(
                    it.title,
                    it.price,
                )
            )
        }

        val totalPrice = items.sumOf { it.sellingPrice }
        val discount = mutableListOf<PriceBreakDownModel.Item>()
        if (coupon != null) {
            discount.add(
                PriceBreakDownModel.Item(
                    name = coupon.code,
                    sellingPrice = when (coupon.discountType) {
                        is CouponModel.DiscountType.PercentageDiscount -> {
                            totalPrice - max((totalPrice * coupon.discountType.percentage), coupon.discountType.maximumDiscount ?: 0.0)
                        }

                        is CouponModel.DiscountType.StaticDiscount -> {
                            max(0.0, coupon.discountType.discount)
                        }
                    }
                )
            )
        }

        return PriceBreakDownModel(
            items = items,
            discount = discount,
            totalSellingPrice = totalPrice - (discount.sumOf { it.sellingPrice }),
            totalStrikethroughPrice = totalPrice
        )
    }
}