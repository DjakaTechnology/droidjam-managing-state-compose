package id.djaka.droidjam.shared.core_ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import org.jetbrains.skiko.loadBytesFromPath

actual suspend fun defaultFontFamily(): FontFamily? {
    val font = loadBytesFromPath("Inter.ttf")
    return FontFamily(
        Font(
            identity = "inter",
            data = font
        )
    )
}