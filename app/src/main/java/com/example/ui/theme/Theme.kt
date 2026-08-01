package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StudyvoColorScheme = darkColorScheme(
    primary = CoralAccent,
    onPrimary = Color.White,
    primaryContainer = RubySurfaceElevated,
    onPrimaryContainer = TextWhite,
    secondary = CoralAccentLight,
    onSecondary = Color.White,
    secondaryContainer = ChipBrown,
    onSecondaryContainer = TextWhite,
    tertiary = CorrectGreenCard,
    background = RubyBackgroundDark,
    onBackground = TextWhite,
    surface = RubySurfaceCard,
    onSurface = TextWhite,
    surfaceVariant = RubySurfaceElevated,
    onSurfaceVariant = TextWhiteSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force warm Studyvo ruby burgundy theme
    dynamicColor: Boolean = false, // Keep Studyvo brand identity
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StudyvoColorScheme,
        typography = Typography,
        content = content
    )
}
