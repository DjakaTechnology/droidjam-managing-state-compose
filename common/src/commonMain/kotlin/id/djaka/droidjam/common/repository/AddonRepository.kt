package id.djaka.droidjam.common.repository

import id.djaka.droidjam.common.model.AddonModel
import id.djaka.droidjam.shared.core.framework.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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