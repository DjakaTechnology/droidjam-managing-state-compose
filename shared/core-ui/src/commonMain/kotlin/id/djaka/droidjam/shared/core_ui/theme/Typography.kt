package id.djaka.droidjam.shared.core_ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalTextApi::class)
//private val googleFontProvider = GoogleFont.Provider(
//    providerAuthority = "com.google.android.gms.fonts",
//    providerPackage = "com.google.android.gms",
//    certificates = R.array.com_google_android_gms_fonts_certs
//)

//@OptIn(ExperimentalTextApi::class)
//val Inter = GoogleFont("Inter")

//@OptIn(ExperimentalTextApi::class)
//val DefaultFontFamily = FontFamily(
//    Font(googleFont = Inter, fontProvider = googleFontProvider, weight = FontWeight.Black),
//    Font(googleFont = Inter, fontProvider = googleFontProvider),
//    Font(googleFont = Inter, fontProvider = googleFontProvider, weight = FontWeight.Medium),
//    Font(googleFont = Inter, fontProvider = googleFontProvider, weight = FontWeight.SemiBold),
//    Font(googleFont = Inter, fontProvider = googleFontProvider, weight = FontWeight.Bold),
//)

val DefaultFontFamily = null

val AppTypography = Typography(
    labelLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
        fontSize = 14.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    labelMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.10000000149011612.sp,
        lineHeight = 16.sp,
        fontSize = 12.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    labelSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.10000000149011612.sp,
        lineHeight = 16.sp,
        fontSize = 11.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    bodyLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 24.sp,
        fontSize = 16.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    bodyMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
        fontSize = 14.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    bodySmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.10000000149011612.sp,
        lineHeight = 16.sp,
        fontSize = 12.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    headlineLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 40.sp,
        fontSize = 32.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    headlineMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 36.sp,
        fontSize = 28.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    headlineSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 32.sp,
        fontSize = 24.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    displayLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 64.sp,
        fontSize = 57.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    displayMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 52.sp,
        fontSize = 45.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    displaySmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 44.sp,
        fontSize = 36.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    titleLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 28.sp,
        fontSize = 22.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    titleMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        lineHeight = 24.sp,
        fontSize = 16.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
    titleSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
        fontSize = 14.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = true
//        )
    ),
)