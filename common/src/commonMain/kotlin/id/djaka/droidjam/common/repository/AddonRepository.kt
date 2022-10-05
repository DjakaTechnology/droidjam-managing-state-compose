package id.djaka.droidjam.common.repository

import id.djaka.droidjam.common.model.AddonModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AddonRepository {
    suspend fun getList(): List<AddonModel> {
        return withContext(Dispatchers.IO) {
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