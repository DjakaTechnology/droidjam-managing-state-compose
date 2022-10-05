package id.djaka.droidjam.common.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun isUseDarkMode(): Boolean {
    return isSystemInDarkTheme()
}

@Composable
fun isUseDynamicColor(): Boolean {
    return true
}

expect fun isSupportDynamicColor(): Boolean