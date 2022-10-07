package id.djaka.droidjam.shared.core_ui.theme

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val RadiusCommon = 4.dp
val RadiusRounded = 8.dp
val RadiusXRounded = 20.dp

val SpacingXXXS = 2.dp
val SpacingXXS = 4.dp
val SpacingXS = 8.dp
val SpacingS = 12.dp
val SpacingM = 16.dp
val SpacingML = 20.dp
val SpacingL = 24.dp
val SpacingXL = 32.dp
val SpacingXXL = 40.dp
val SpacingXXXL = 48.dp
val SpacingXXXXL = 56.dp

val IconSizeS = 12.dp
val IconSizeM = 16.dp
val IconSizeML = 18.dp
val IconSizeL = 24.dp
val IconSizeXL = 32.dp

@Composable
fun Spacer(size: Dp) = androidx.compose.foundation.layout.Spacer(Modifier.size(size))

@Composable
fun SpacerHorizontal(size: Dp) = androidx.compose.foundation.layout.Spacer(Modifier.width(size))

@Composable
fun SpacerVertical(size: Dp) = androidx.compose.foundation.layout.Spacer(Modifier.height(size))