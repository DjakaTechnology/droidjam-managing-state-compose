package id.djaka.droidjam.common.ui.booking.coupon_widget

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
import id.djaka.droidjam.common.ui.theme.CardClickableShape
import id.djaka.droidjam.common.ui.theme.SpacerVertical
import id.djaka.droidjam.common.ui.theme.SpacingM
import id.djaka.droidjam.common.ui.theme.SpacingXS

@Composable
fun CouponWidget(
    modifier: Modifier = Modifier,
    state: CouponPresenter.Model,
    action: (CouponPresenter.Event) -> Unit
) {
    Column(modifier) {
        Text("Coupon Code")
        SpacerVertical(SpacingXS)
        Box(modifier = Modifier.width(IntrinsicSize.Max)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                value = state.coupon,
                onValueChange = { action(CouponPresenter.Event.CouponTextChanged(it)) },
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

        when (state.couponValidation) {
            is CouponPresenter.Model.ValidationResult.Invalid -> {
                Text(state.couponValidation.message, color = MaterialTheme.colorScheme.error)
            }

            is CouponPresenter.Model.ValidationResult.Success -> {
                Text(state.couponValidation.discountMessage, color = MaterialTheme.colorScheme.primary)
            }

            CouponPresenter.Model.ValidationResult.Loading -> {
                Text("Checking...")
            }

            CouponPresenter.Model.ValidationResult.Idle -> {}
        }
    }
}