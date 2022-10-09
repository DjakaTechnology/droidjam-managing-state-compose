package id.djaka.droidjam.shared.booking.app.ui.booking.booking_info_widget

import androidx.compose.runtime.Composable
import id.djaka.driodjam.shared.core.molecule.MoleculePresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_info.BookingInfoEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_info.BookingInfoModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_info.BookingInfoPresenter
import kotlinx.coroutines.flow.Flow

class BookingInfoPresenterImpl : BookingInfoPresenter, MoleculePresenter<BookingInfoEvent, BookingInfoModel> {
    @Composable
    override fun present(event: Flow<BookingInfoEvent>): BookingInfoModel {
        return BookingInfoModel(
            "SG",
            "Djaka Pradana",
            1
        )
    }
}