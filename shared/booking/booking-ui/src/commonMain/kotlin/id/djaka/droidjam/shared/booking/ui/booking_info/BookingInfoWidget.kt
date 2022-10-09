package id.djaka.droidjam.shared.booking.ui.booking_info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_info.BookingInfoModel
import id.djaka.droidjam.shared.core_ui.theme.SpacingM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingInfoWidget(
    modifier: Modifier = Modifier,
    state: BookingInfoModel
) {
    Card(modifier) {
        Column(Modifier.padding(SpacingM)) {
            Text("Product Id: " + state.productId)
            Text("Name: " + state.mainTraveler)
            Text("Pax: " + state.pax + " Pax(s)")
        }
    }
}