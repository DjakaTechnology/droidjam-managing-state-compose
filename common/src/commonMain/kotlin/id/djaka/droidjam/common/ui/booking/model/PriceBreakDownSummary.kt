package id.djaka.droidjam.common.ui.booking.model

class PriceBreakDownSummary(
    val items: List<Item> = listOf()
) {
    class Item(
        val name: String,
        val price: Double,
    )
}