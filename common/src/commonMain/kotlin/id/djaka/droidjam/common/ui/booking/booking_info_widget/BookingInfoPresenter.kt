package id.djaka.droidjam.common.ui.booking.booking_info_widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import id.djaka.droidjam.shared.core.framework.Presenter
import kotlinx.coroutines.flow.Flow

class BookingInfoPresenter : Presenter<BookingInfoPresenter.Event, BookingInfoPresenter.Model> {
    @Composable
    override fun present(event: Flow<Event>): Model {
        return Model(
            "SG",
            "Djaka Pradana",
            1
        )
    }

    sealed class Event

    @Immutable
    class Model(
        val productId: String,
        val mainTraveler: String,
        val pax: Int,
    )
}