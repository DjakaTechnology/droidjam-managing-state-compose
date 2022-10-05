package id.djaka.droidjam.common.ui.booking.price_breakdown

import androidx.compose.runtime.*
import id.djaka.droidjam.common.domain.CalculatePriceBreakDownUseCase
import id.djaka.droidjam.common.framework.MoleculePresenter
import id.djaka.droidjam.common.model.PriceBreakDownModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PriceBreakDownPresenter constructor(
    private val calculatePriceBreakDownUseCase: CalculatePriceBreakDownUseCase
) : MoleculePresenter<PriceBreakDownPresenter.Event, PriceBreakDownPresenter.UIState> {
    @Composable
    override fun present(event: Flow<Event>): UIState {
        var isExpanded by remember { mutableStateOf(false) }
        var totalPriceState: UIState.TotalPriceState by remember { mutableStateOf(UIState.TotalPriceState.Loading) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            event.collect {
                when (it) {
                    is Event.ToggleExpandCollapse -> isExpanded = it.isExpanded
                    is Event.UpdateSpec -> coroutineScope.launch {
                        totalPriceState = UIState.TotalPriceState.Loading
                        totalPriceState = UIState.TotalPriceState.Success(
                            calculatePriceBreakDownUseCase(it.productId, it.couponCode, it.addOnItemId)
                        )
                    }
                }
            }
        }

        return UIState(
            isExpanded = isExpanded,
            totalPriceState = totalPriceState
        )
    }

    sealed class Event {
        class ToggleExpandCollapse(val isExpanded: Boolean) : Event()
        class UpdateSpec(
            val addOnItemId: List<String>,
            val productId: String,
            val couponCode: String
        ) : Event()
    }

    @Immutable
    data class UIState(
        val isExpanded: Boolean,
        val totalPriceState: TotalPriceState,
    ) {
        sealed class TotalPriceState {
            class Success(val totalPrice: PriceBreakDownModel) : TotalPriceState()
            object Loading : TotalPriceState()
        }

        val successState = totalPriceState as? TotalPriceState.Success

        val isSuccess = successState != null
    }
}