package id.djaka.droidjam.common.ui.booking.booking_info_widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import id.djaka.droidjam.common.framework.MoleculePresenter
import kotlinx.coroutines.flow.Flow

class BookingInfoPresenter : MoleculePresenter<BookingInfoPresenter.Event, BookingInfoPresenter.UIState> {
    @Composable
    override fun present(event: Flow<Event>): UIState {
        return UIState(
            "SG",
            "Djaka Pradana",
            1
        )
    }

    sealed class Event

    @Immutable
    class UIState(
        val productId: String,
        val mainTraveler: String,
        val pax: Int,
    )
}