package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NaijaGreenAccent,
    onPrimary = NaijaDarkBg,
    primaryContainer = NaijaGreenDark,
    onPrimaryContainer = Color.White,
    secondary = NaijaGold,
    onSecondary = NaijaDarkBg,
    secondaryContainer = NaijaDarkSurfaceVariant,
    onSecondaryContainer = NaijaGoldLight,
    tertiary = NaijaOrange,
    onTertiary = Color.White,
    background = NaijaDarkBg,
    onBackground = TextWhitePrimary,
    surface = NaijaDarkSurface,
    onSurface = TextWhitePrimary,
    surfaceVariant = NaijaDarkSurfaceVariant,
    onSurfaceVariant = TextWhiteSecondary,
    outline = NaijaDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = NaijaGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = NaijaGreenSubtle,
    onPrimaryContainer = NaijaGreenDark,
    secondary = NaijaGoldDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = Color(0xFF5D4037),
    tertiary = NaijaOrange,
    onTertiary = Color.White,
    background = NaijaLightBg,
    onBackground = TextDarkPrimary,
    surface = NaijaLightSurface,
    onSurface = TextDarkPrimary,
    surfaceVariant = NaijaLightSurfaceVariant,
    onSurfaceVariant = TextDarkSecondary,
    outline = NaijaLightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek athletic dark theme to highlight real-time neon charts
    dynamicColor: Boolean = false, // Preserve Nigerian national green & gold colors
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
