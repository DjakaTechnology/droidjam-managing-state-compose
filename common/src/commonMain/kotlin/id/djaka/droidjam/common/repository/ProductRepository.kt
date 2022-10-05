package id.djaka.droidjam.common.repository

import id.djaka.droidjam.common.model.ProductModel

class ProductRepository {
    fun getProduct() = listOf(
        ProductModel(
            id = "SG",
            name = "Universal Studios Japan",
            price = 200.0
        )
    )

    fun getProduct(id: String): ProductModel {
        return getProduct().first { it.id == id }
    }
}