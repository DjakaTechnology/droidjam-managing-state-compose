package id.djaka.droidjam.shared.core_ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import id.djaka.droidjam.shared.core_ui.R

actual suspend fun defaultFontFamily(): FontFamily? {
    return FontFamily(
        Font(
            R.font.inter
        )
    )
}