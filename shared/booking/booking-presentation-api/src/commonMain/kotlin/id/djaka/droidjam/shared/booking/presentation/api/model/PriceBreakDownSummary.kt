package id.djaka.droidjam.shared.booking.presentation.api.model

class PriceBreakDownSummary(
    val items: List<Item> = listOf()
) {
    class Item(
        val name: String,
        val price: Double,
    )
}