@file:OptIn(ExperimentalMaterial3Api::class)

package id.djaka.droidjam.shared.booking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import id.djaka.driodjam.shared.core.molecule.rememberLaunchPresenter
import id.djaka.droidjam.shared.booking.presentation.api.di.BookingPresentationApiDIManager
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen.BookingEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen.BookingModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen.BookingPresenter
import id.djaka.droidjam.shared.booking.ui.addon.AddonWidget
import id.djaka.droidjam.shared.booking.ui.booking_info.BookingInfoWidget
import id.djaka.droidjam.shared.booking.ui.coupon.CouponWidget
import id.djaka.droidjam.shared.booking.ui.price_breakdown.PriceBreakDown
import id.djaka.droidjam.shared.core_ui.dialog.CoreDialog
import id.djaka.droidjam.shared.core_ui.theme.CoreTheme
import id.djaka.droidjam.shared.core_ui.theme.SpacerVertical
import id.djaka.droidjam.shared.core_ui.theme.SpacingM
import id.djaka.droidjam.shared.core_ui.theme.SpacingXL
import kotlinx.coroutines.launch

@Composable
fun BookingScreen() {
    CoreTheme {
        val presenter = remember { createPresenter() }
        val (event, state) = presenter.rememberLaunchPresenter()
        val coroutineScope = rememberCoroutineScope()

        Surface {
            BookingScreen(state) { coroutineScope.launch { event.emit(it) } }
        }
    }
}

fun createPresenter(): BookingPresenter {
    return BookingPresentationApiDIManager.presenterProvider.provideBookingPresenter()
}

@Composable
fun BookingScreen(state: BookingModel, action: (BookingEvent) -> Unit) {
    if (state.isSubmitting) {
        SubmittingDialog()
    }

    if (state.isSubmitCompleted) {
        SubmitSuccessDialog(action)
    }

    state.userActionSubmitDialog?.let {
        UserActionDialog(action, it)
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        SpacerVertical(SpacingM)
        Text(
            "Booking Screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = SpacingM)
        )
        SpacerVertical(SpacingM)

        BookingInfoWidget(
            modifier = Modifier
                .padding(horizontal = SpacingM)
                .fillMaxWidth(),
            state = state.bookingInfoState
        )
        SpacerVertical(SpacingM)

        AddonWidget(
            modifier = Modifier.padding(horizontal = SpacingM).fillMaxWidth(),
            state = state.addonState, action = { action(BookingEvent.SendAddonEvent(it)) })
        SpacerVertical(SpacingM)

        CouponWidget(
            modifier = Modifier
                .padding(horizontal = SpacingM)
                .fillMaxWidth(),
            state = state.couponInfoState,
            action = { action(BookingEvent.SendCouponEvent(it)) }
        )
        SpacerVertical(SpacingM)

        PriceBreakDown(
            modifier = Modifier
                .padding(horizontal = SpacingM)
                .fillMaxWidth(),
            state = state.priceBreakDownState,
            action = { action(BookingEvent.SendPriceBreakdownEvent(it)) }
        )

        SpacerVertical(SpacingM)

        Button(
            modifier = Modifier.padding(horizontal = SpacingM).fillMaxWidth(),
            onClick = { action(BookingEvent.Submit()) },
            enabled = state.isSubmitEnabled
        ) {
            Text("Submit")
        }

        SpacerVertical(SpacingXL)
    }
}

@Composable
private fun UserActionDialog(
    action: (BookingEvent) -> Unit,
    state: BookingModel.SubmitResult.RequireUserActionDialog
) {
    CoreDialog(
        onDismissRequest = { action(BookingEvent.DismissRequireUserActionDialog) },
    ) {
        Card(Modifier.padding(horizontal = SpacingM).fillMaxWidth()) {
            Column(Modifier.padding(SpacingM)) {
                Text(state.titleText, style = MaterialTheme.typography.titleLarge)
                SpacerVertical(SpacingM)
                Text(state.messageText)
                SpacerVertical(SpacingM)

                state.actionPrimary?.let { actionPrimary ->
                    Button({ action(BookingEvent.Submit(forceSubmit = true)) }, Modifier.align(Alignment.End)) {
                        Text(actionPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun SubmittingDialog() {
    CoreDialog(
        onDismissRequest = { },
    ) {
        Card(Modifier.padding(horizontal = SpacingM).fillMaxWidth()) {
            Column(Modifier.padding(SpacingM)) {
                Text("Submitting")
                SpacerVertical(SpacingM)
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun SubmitSuccessDialog(
    action: (BookingEvent) -> Unit,
) {
    CoreDialog(
        onDismissRequest = { action(BookingEvent.DismissSuccessDialog) },
    ) {
        Card(Modifier.padding(horizontal = SpacingM).fillMaxWidth()) {
            Column(Modifier.padding(SpacingM)) {
                Text("Submit Finished", style = MaterialTheme.typography.headlineSmall)
                SpacerVertical(SpacingM)
                Text("Thank you for your order, have a good trip! 😊")
                SpacerVertical(SpacingM)
                Button(onClick = { action(BookingEvent.DismissSuccessDialog) }, Modifier.align(Alignment.End)) {
                    Text("Yay")
                }
            }
        }
    }
}