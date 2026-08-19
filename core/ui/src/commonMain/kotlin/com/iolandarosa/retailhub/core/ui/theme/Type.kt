package com.iolandarosa.retailhub.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.outfit_bold
import retailhub.core.ui.generated.resources.outfit_medium
import retailhub.core.ui.generated.resources.outfit_regular
import retailhub.core.ui.generated.resources.outfit_semibold

@Composable
fun outfitFontFamily(): FontFamily {
    return FontFamily(
        Font(
            Res.font.outfit_regular,
            weight = FontWeight.Normal
        ),
        Font(
            Res.font.outfit_medium,
            weight = FontWeight.Medium
        ),
        Font(
            Res.font.outfit_semibold,
            weight = FontWeight.SemiBold
        ),
        Font(
            Res.font.outfit_bold,
            weight = FontWeight.Bold
        )
    )
}

@Composable
fun appTypography(): Typography {
    val outfit = outfitFontFamily()

    return Typography(
        displayLarge = TextStyle(
            fontFamily = outfit,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            fontWeight = FontWeight.Normal
        ),

        displayMedium = TextStyle(
            fontFamily = outfit,
            fontSize = 45.sp,
            lineHeight = 52.sp
        ),

        displaySmall = TextStyle(
            fontFamily = outfit,
            fontSize = 36.sp,
            lineHeight = 44.sp
        ),

        headlineLarge = TextStyle(
            fontFamily = outfit,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold
        ),

        headlineMedium = TextStyle(
            fontFamily = outfit,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),

        headlineSmall = TextStyle(
            fontFamily = outfit,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),

        titleLarge = TextStyle(
            fontFamily = outfit,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.SemiBold
        ),

        titleMedium = TextStyle(
            fontFamily = outfit,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium
        ),

        titleSmall = TextStyle(
            fontFamily = outfit,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium
        ),

        bodyLarge = TextStyle(
            fontFamily = outfit,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),

        bodyMedium = TextStyle(
            fontFamily = outfit,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),

        bodySmall = TextStyle(
            fontFamily = outfit,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),

        labelLarge = TextStyle(
            fontFamily = outfit,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium
        ),

        labelMedium = TextStyle(
            fontFamily = outfit,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium
        ),

        labelSmall = TextStyle(
            fontFamily = outfit,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium
        )
    )
}
