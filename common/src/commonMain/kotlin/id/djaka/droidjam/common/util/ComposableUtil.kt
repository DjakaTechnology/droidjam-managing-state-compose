package id.djaka.droidjam.common.util

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@NonRestartableComposable
@Composable
fun <T> CollectEffect(flow: Flow<T>, action: suspend (T) -> Unit) {
    LaunchedEffect(flow) {
        flow.collect {
            action(it)
        }
    }
}