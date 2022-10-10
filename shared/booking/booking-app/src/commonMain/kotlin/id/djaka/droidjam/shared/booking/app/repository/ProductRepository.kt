package id.djaka.droidjam.shared.booking.app.repository

import id.djaka.droidjam.shared.booking.presentation.api.model.ProductDataModel

class ProductRepository {
    fun getProduct() = listOf(
        ProductDataModel(
            id = "SG",
            name = "Universal Studios Japan",
            price = 200.0
        )
    )

    fun getProduct(id: String): ProductDataModel {
        return getProduct().first { it.id == id }
    }
}