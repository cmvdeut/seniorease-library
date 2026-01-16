package com.seniorease.library.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.seniorease.library.utils.SettingsHelper
import com.seniorease.library.ui.theme.rememberAccessibleTypography

private val DarkColorScheme = darkColorScheme(
    primary = SeniorEasePrimaryLight,
    onPrimary = Color(0xFF000000),
    secondary = SeniorEasePrimary,
    onSecondary = Color(0xFF000000),
    tertiary = SeniorEasePrimaryDark,
    onTertiary = Color(0xFFFFFFFF),
    background = SeniorEaseDarkBackground,
    onBackground = SeniorEaseForegroundDark,
    surface = Color(0xFF333333),
    onSurface = SeniorEaseForegroundDark,
    surfaceVariant = Color(0xFF3A3A3A),
    onSurfaceVariant = SeniorEaseForegroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = SeniorEasePrimary,
    onPrimary = Color(0xFFFFFFFF),
    secondary = SeniorEasePrimaryLight,
    onSecondary = Color(0xFFFFFFFF),
    tertiary = SeniorEasePrimaryDark,
    onTertiary = Color(0xFFFFFFFF),
    background = SeniorEaseCream,
    onBackground = SeniorEaseForeground,
    surface = Color(0xFFFFFFFF),
    onSurface = SeniorEaseForeground,
    surfaceVariant = SeniorEaseStone,
    onSurfaceVariant = SeniorEaseForeground
)

// Hoog contrast color scheme (WCAG AAA compliant - 21:1 contrast ratio)
private val HighContrastLightColorScheme = lightColorScheme(
    primary = Color(0xFF000000),      // Zwart
    onPrimary = Color(0xFFFFFFFF),    // Wit
    secondary = Color(0xFF000000),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF000000),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),     // Wit
    onBackground = Color(0xFF000000),   // Zwart
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF000000),
    error = Color(0xFF000000),
    onError = Color(0xFFFFFFFF)
)

private val HighContrastDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),      // Wit
    onPrimary = Color(0xFF000000),     // Zwart
    secondary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    tertiary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFF000000),
    background = Color(0xFF000000),   // Zwart
    onBackground = Color(0xFFFFFFFF),   // Wit
    surface = Color(0xFF000000),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFFFFFFFF),
    error = Color(0xFFFFFFFF),
    onError = Color(0xFF000000)
)

@Composable
fun BiblitoheekTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    isLargeTextEnabled: Boolean = false,
    isHighContrastEnabled: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    
    // Bepaal color scheme
    val colorScheme = when {
        isHighContrastEnabled -> {
            if (darkTheme) HighContrastDarkColorScheme else HighContrastLightColorScheme
        }
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    // Bepaal typography
    val typography = rememberAccessibleTypography(isLargeTextEnabled = isLargeTextEnabled)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}