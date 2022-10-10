package id.djaka.droidjam.shared.booking.app.ui.booking.price_breakdown

import androidx.compose.runtime.*
import id.djaka.driodjam.shared.core.molecule.MoleculePresenter
import id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown.PriceBreakDownEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown.PriceBreakDownModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown.PriceBreakdownPresenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PriceBreakDownPresenterImpl constructor(
    private val calculatePriceBreakDownUseCase: id.djaka.droidjam.shared.booking.app.domain.CalculatePriceBreakDownUseCase
) : PriceBreakdownPresenter, MoleculePresenter<PriceBreakDownEvent, PriceBreakDownModel> {
    @Composable
    override fun present(event: Flow<PriceBreakDownEvent>): PriceBreakDownModel {
        var isExpanded by remember { mutableStateOf(false) }
        var totalPriceState: PriceBreakDownModel.TotalPriceState by remember { mutableStateOf(PriceBreakDownModel.TotalPriceState.Loading) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            event.collect {
                when (it) {
                    is PriceBreakDownEvent.ToggleExpandCollapse -> isExpanded = it.isExpanded
                    is PriceBreakDownEvent.UpdateSpec -> coroutineScope.launch {
                        totalPriceState = PriceBreakDownModel.TotalPriceState.Loading
                        totalPriceState = PriceBreakDownModel.TotalPriceState.Success(
                            calculatePriceBreakDownUseCase(it.productId, it.couponCode, it.addOnItemId)
                        )
                    }
                }
            }
        }

        return PriceBreakDownModel(
            isExpanded = isExpanded,
            totalPriceState = totalPriceState
        )
    }

}