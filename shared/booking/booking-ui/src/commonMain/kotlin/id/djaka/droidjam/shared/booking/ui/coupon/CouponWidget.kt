package id.djaka.droidjam.shared.booking.ui.coupon

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponModel
import id.djaka.droidjam.shared.core_ui.theme.CardClickableShape
import id.djaka.droidjam.shared.core_ui.theme.SpacerVertical
import id.djaka.droidjam.shared.core_ui.theme.SpacingM
import id.djaka.droidjam.shared.core_ui.theme.SpacingXS

@Composable
fun CouponWidget(
    modifier: Modifier = Modifier,
    state: CouponModel,
    action: (CouponEvent) -> Unit
) {
    Column(modifier) {
        Text("Coupon Code")
        SpacerVertical(SpacingXS)
        Box(modifier = Modifier.width(IntrinsicSize.Max)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                value = state.coupon,
                onValueChange = { action(CouponEvent.CouponTextChanged(it)) },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(0.25f),
                        text = "Your promo code",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                shape = CardClickableShape()
            )

            Box(Modifier.padding(end = SpacingM).align(Alignment.CenterEnd)) {
                if (state.isValidationInProgress) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                }
            }
        }

        when (val couponValidation = state.couponValidation) {
            is CouponModel.ValidationResult.Invalid -> {
                Text(couponValidation.message, color = MaterialTheme.colorScheme.error)
            }

            is CouponModel.ValidationResult.Success -> {
                Text(couponValidation.discountMessage, color = MaterialTheme.colorScheme.primary)
            }

            CouponModel.ValidationResult.Loading -> {
                Text("Checking...")
            }

            CouponModel.ValidationResult.Idle -> {}
        }
    }
}