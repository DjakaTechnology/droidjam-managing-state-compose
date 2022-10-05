package id.djaka.droidjam.common.framework

import kotlinx.coroutines.Dispatchers

interface CoroutineDispatchers {
    fun main() = Dispatchers.Main
    fun io() = Dispatchers.IO
    fun default() = Dispatchers.Default
}