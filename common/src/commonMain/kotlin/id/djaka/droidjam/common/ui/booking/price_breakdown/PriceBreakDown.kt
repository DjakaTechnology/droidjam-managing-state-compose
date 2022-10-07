@file:OptIn(ExperimentalMaterial3Api::class)

package id.djaka.droidjam.common.ui.booking.price_breakdown

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import id.djaka.droidjam.common.model.PriceBreakDownModel
import id.djaka.droidjam.common.ui.theme.*

@Composable
fun PriceBreakDown(
    modifier: Modifier = Modifier,
    state: PriceBreakDownPresenter.Model,
    action: (PriceBreakDownPresenter.Event) -> Unit
) {
    Column(modifier) {
        Card(
            onClick = { action(PriceBreakDownPresenter.Event.ToggleExpandCollapse(!state.isExpanded)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(SpacingM),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total Price")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    when (val it = state.totalPriceState) {
                        PriceBreakDownPresenter.Model.TotalPriceState.Loading -> {
                            CircularProgressIndicator(Modifier.size(14.dp))
                        }

                        is PriceBreakDownPresenter.Model.TotalPriceState.Success -> {
                            TotalPriceSummary(
                                sellingPrice = it.totalPrice.totalSellingPrice,
                                strikeTroughPrice = it.totalPrice.totalStrikethroughPrice
                            )
                        }
                    }
                    SpacerHorizontal(SpacingXS)
                    Text("V", modifier = Modifier.rotate(if (state.isExpanded) 180f else 0f))
                }

            }
        }

        if (state.isExpanded) {
            Card(
                modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small.copy(
                    topEnd = CornerSize(0.dp),
                    topStart = CornerSize(0.dp)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                when (val it = state.totalPriceState) {
                    PriceBreakDownPresenter.Model.TotalPriceState.Loading -> {
                        Column {
                            SpacerVertical(SpacingM)
                            LinearProgressIndicator(modifier.fillMaxWidth())
                            SpacerVertical(SpacingM)
                        }
                    }

                    is PriceBreakDownPresenter.Model.TotalPriceState.Success -> {
                        Column(Modifier.padding(SpacingXS)) {
                            it.totalPrice.items.forEach {
                                PriceBreakDownItems(Modifier.fillMaxWidth(), it)
                            }
                            it.totalPrice.discount.forEach {
                                PriceBreakDownItems(Modifier.fillMaxWidth(), it, sellingPricePrefix = "-")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceBreakDownItems(
    modifier: Modifier = Modifier,
    it: PriceBreakDownModel.Item,
    sellingPricePrefix: String = ""
) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(it.name)
        Row {
            TotalPriceSummary(
                sellingPrice = it.sellingPrice,
                strikeTroughPrice = it.strikethroughPrice,
                sellingPricePrefix = sellingPricePrefix
            )
        }
    }
}

@Composable
private fun TotalPriceSummary(
    modifier: Modifier = Modifier,
    sellingPrice: Double,
    strikeTroughPrice: Double? = null,
    sellingPricePrefix: String = "",
) {
    Row(modifier) {
        if (strikeTroughPrice != null) {
            Text(
                "$${strikeTroughPrice}",
                style = LocalTextStyle.current.copy(
                    textDecoration = TextDecoration.LineThrough
                ),
                modifier = Modifier.alpha(0.5f)
            )
        }

        Text(
            "$sellingPricePrefix$${sellingPrice}",
            color = MaterialTheme.colorScheme.primary
        )
    }
}