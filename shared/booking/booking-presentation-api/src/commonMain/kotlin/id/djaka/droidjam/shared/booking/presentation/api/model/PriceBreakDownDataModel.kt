package id.djaka.droidjam.shared.booking.presentation.api.model

class PriceBreakDownDataModel(
    val items: List<Item>,
    val discount: List<Item>,
    val totalSellingPrice: Double,
    val totalStrikethroughPrice: Double? = null,
) {
    class Item(
        val name: String,
        val sellingPrice: Double,
        val strikethroughPrice: Double? = null,
    )
}