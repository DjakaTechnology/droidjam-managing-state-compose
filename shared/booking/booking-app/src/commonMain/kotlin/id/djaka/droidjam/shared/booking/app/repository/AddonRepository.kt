package id.djaka.droidjam.shared.booking.app.repository

import id.djaka.droidjam.shared.booking.presentation.api.model.addon.AddonModel
import id.djaka.droidjam.shared.core.framework.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AddonRepository(
    private val dispatcher: CoroutineDispatchers
) {
    suspend fun getList(): List<AddonModel> {
        return withContext(dispatcher.io()) {
            delay(1000) // Pretend API call
            listOf(
                AddonModel(
                    "1",
                    "Insurance",
                    "Insurance up to $5000",
                    price = 10.0
                ),
                AddonModel(
                    "2",
                    "Gift Voucher",
                    "Get voucher worth up to $50",
                    price = 5.0,
                )
            )
        }
    }
}