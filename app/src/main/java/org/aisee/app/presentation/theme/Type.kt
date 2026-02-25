package org.aisee.app.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.aisee.app.R

val DmSansFontFamily = FontFamily(
    Font(R.font.dmsans_regular, FontWeight.Normal),
    Font(R.font.dmsans_extrabold, FontWeight.ExtraBold),
)

private val defaultTypography = Typography()

val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = DmSansFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = DmSansFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = DmSansFontFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = DmSansFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = DmSansFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = DmSansFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = DmSansFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = DmSansFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = DmSansFontFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = DmSansFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = DmSansFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = DmSansFontFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = DmSansFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = DmSansFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = DmSansFontFamily),
)
