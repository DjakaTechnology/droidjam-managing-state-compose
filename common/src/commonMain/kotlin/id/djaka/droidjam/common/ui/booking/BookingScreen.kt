@file:OptIn(ExperimentalMaterial3Api::class)

package id.djaka.droidjam.common.ui.booking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import id.djaka.droidjam.common.di.CoreDIManager
import id.djaka.droidjam.common.framework.rememberLaunchPresenter
import id.djaka.droidjam.common.ui.booking.addon_widget.AddonWidget
import id.djaka.droidjam.common.ui.booking.booking_info_widget.BookingInfoPresenter
import id.djaka.droidjam.common.ui.booking.booking_info_widget.BookingInfoWidget
import id.djaka.droidjam.common.ui.booking.coupon_widget.CouponWidget
import id.djaka.droidjam.common.ui.booking.price_breakdown.PriceBreakDown
import id.djaka.droidjam.common.ui.booking.price_breakdown.PriceBreakDownPresenter
import id.djaka.droidjam.common.ui.dialog.CoreDialog
import id.djaka.droidjam.common.ui.theme.CoreTheme
import id.djaka.droidjam.common.ui.theme.SpacerVertical
import id.djaka.droidjam.common.ui.theme.SpacingM
import id.djaka.droidjam.common.ui.theme.SpacingXL
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BookingScreen() {
    CoreTheme {
        val presenter = remember { createPresenter() }
        val (event, state) = presenter.rememberLaunchPresenter()
        val coroutineScope = rememberCoroutineScope()

        BookingScreen(state) { coroutineScope.launch { event.emit(it) } }
    }
}

fun createPresenter(): BookingPresenter {
    val subComponent = CoreDIManager.subComponent()
    return BookingPresenter(
        addonPresenter = subComponent.addonPresenter,
        bookingInfoPresenter = BookingInfoPresenter(),
        couponPresenter = subComponent.couponPresenter,
        priceBreakDownPresenter = subComponent.priceBreakDownPresenter
    )
}

@Composable
fun BookingScreen(state: BookingPresenter.UIState, action: (BookingPresenter.Event) -> Unit) {
    if (state.isSubmitting) {
        SubmittingDialog()
    }

    if (state.isSubmitCompleted) {
        SubmitSuccessDialog(action)
    }

    if (state.userActionSubmitDialog != null) {
        UserActionDialog(action, state.userActionSubmitDialog)
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
            state = state.addonState, action = { action(BookingPresenter.Event.AddonEvent(it)) })
        SpacerVertical(SpacingM)

        CouponWidget(
            modifier = Modifier
                .padding(horizontal = SpacingM)
                .fillMaxWidth(),
            state = state.couponInfoState,
            action = { action(BookingPresenter.Event.CouponEvent(it)) }
        )
        SpacerVertical(SpacingM)

        PriceBreakDown(
            modifier = Modifier
                .padding(horizontal = SpacingM)
                .fillMaxWidth(),
            state = state.priceBreakDownState,
            action = { action(BookingPresenter.Event.PriceBreakdownEvent(it)) }
        )

        SpacerVertical(SpacingM)

        Button(
            modifier = Modifier.padding(horizontal = SpacingM).fillMaxWidth(),
            onClick = { action(BookingPresenter.Event.Submit()) },
            enabled = state.isSubmitEnabled
        ) {
            Text("Submit")
        }

        SpacerVertical(SpacingXL)
    }
}

@Composable
private fun UserActionDialog(
    action: (BookingPresenter.Event) -> Unit,
    state: BookingPresenter.UIState.SubmitResult.RequireUserActionDialog
) {
    CoreDialog(
        onDismissRequest = { action(BookingPresenter.Event.DismissRequireUserActionDialog) },
    ) {
        Card(Modifier.padding(horizontal = SpacingM).fillMaxWidth()) {
            Column(Modifier.padding(SpacingM)) {
                Text(state.titleText, style = MaterialTheme.typography.titleLarge)
                SpacerVertical(SpacingM)
                Text(state.messageText)
                SpacerVertical(SpacingM)
                if (state.actionPrimary != null) {
                    Button({ action(BookingPresenter.Event.Submit(forceSubmit = true)) }, Modifier.align(Alignment.End)) {
                        Text(state.actionPrimary)
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
    action: (BookingPresenter.Event) -> Unit,
) {
    CoreDialog(
        onDismissRequest = { action(BookingPresenter.Event.DismissSuccessDialog) },
    ) {
        Card(Modifier.padding(horizontal = SpacingM).fillMaxWidth()) {
            Column(Modifier.padding(SpacingM)) {
                Text("Submit Finished", style = MaterialTheme.typography.headlineSmall)
                SpacerVertical(SpacingM)
                Text("Thank you for your order, have a good trip! 😊")
                SpacerVertical(SpacingM)
                Button(onClick = { action(BookingPresenter.Event.DismissSuccessDialog) }, Modifier.align(Alignment.End)) {
                    Text("Yay")
                }
            }
        }
    }
}