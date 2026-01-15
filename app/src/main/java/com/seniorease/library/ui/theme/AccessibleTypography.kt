package com.seniorease.library.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Toegankelijke typografie met grote tekst ondersteuning
@Composable
fun rememberAccessibleTypography(
    isLargeTextEnabled: Boolean = false
): Typography {
    val scaleFactor = if (isLargeTextEnabled) 1.25f else 1.0f
    
    return remember(isLargeTextEnabled) {
        Typography(
            // Headlines - grote, duidelijke titels
            displayLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = (28 * scaleFactor).sp,
                lineHeight = (36 * scaleFactor).sp,
                letterSpacing = 0.sp
            ),
            displayMedium = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = (24 * scaleFactor).sp,
                lineHeight = (32 * scaleFactor).sp,
                letterSpacing = 0.sp
            ),
            displaySmall = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = (20 * scaleFactor).sp,
                lineHeight = (28 * scaleFactor).sp,
                letterSpacing = 0.sp
            ),
            
            // Headlines - sectie titels
            headlineLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = (22 * scaleFactor).sp,
                lineHeight = (30 * scaleFactor).sp,
                letterSpacing = 0.sp
            ),
            headlineMedium = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = (20 * scaleFactor).sp,
                lineHeight = (28 * scaleFactor).sp,
                letterSpacing = 0.sp
            ),
            headlineSmall = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = (18 * scaleFactor).sp,
                lineHeight = (26 * scaleFactor).sp,
                letterSpacing = 0.sp
            ),
            
            // Titles - item titels en labels
            titleLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = (20 * scaleFactor).sp,
                lineHeight = (28 * scaleFactor).sp,
                letterSpacing = 0.sp
            ),
            titleMedium = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = (18 * scaleFactor).sp,
                lineHeight = (26 * scaleFactor).sp,
                letterSpacing = 0.sp
            ),
            titleSmall = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = (16 * scaleFactor).sp,
                lineHeight = (24 * scaleFactor).sp,
                letterSpacing = 0.sp
            ),
            
            // Body text - hoofdinhoud (minimaal 18sp voor toegankelijkheid)
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = (18 * scaleFactor).sp,
                lineHeight = (28 * scaleFactor).sp,
                letterSpacing = 0.5.sp
            ),
            bodyMedium = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = (16 * scaleFactor).sp,
                lineHeight = (24 * scaleFactor).sp,
                letterSpacing = 0.5.sp
            ),
            bodySmall = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = (14 * scaleFactor).sp,
                lineHeight = (20 * scaleFactor).sp,
                letterSpacing = 0.5.sp
            ),
            
            // Labels - knoppen en kleine tekst
            labelLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = (16 * scaleFactor).sp,
                lineHeight = (24 * scaleFactor).sp,
                letterSpacing = 0.5.sp
            ),
            labelMedium = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = (14 * scaleFactor).sp,
                lineHeight = (20 * scaleFactor).sp,
                letterSpacing = 0.5.sp
            ),
            labelSmall = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = (12 * scaleFactor).sp,
                lineHeight = (18 * scaleFactor).sp,
                letterSpacing = 0.5.sp
            )
        )
    }
}

