package id.djaka.droidjam.common.ui.booking.booking_info_widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import id.djaka.driodjam.shared.core.molecule.MoleculePresenter
import id.djaka.droidjam.shared.core.framework.Presenter
import kotlinx.coroutines.flow.Flow

class BookingInfoPresenter : MoleculePresenter<BookingInfoPresenter.Event, BookingInfoPresenter.Model> {
    @Composable
    override fun presentComposable(event: Flow<Event>): Model {
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