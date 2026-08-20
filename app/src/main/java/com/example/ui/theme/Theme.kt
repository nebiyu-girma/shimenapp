package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
  primary = ShimenaCotton,
  onPrimary = ShimenaCharcoalDark,
  primaryContainer = ShimenaCharcoalSurface,
  onPrimaryContainer = ShimenaCottonLight,
  secondary = ShimenaEarthLight,
  onSecondary = ShimenaCharcoalDark,
  secondaryContainer = ShimenaCharcoalBorder,
  onSecondaryContainer = ShimenaWarmSand,
  tertiary = ShimenaIndigo,
  onTertiary = ShimenaCottonLight,
  background = ShimenaCharcoalDark,
  onBackground = TextPrimaryDark,
  surface = ShimenaCharcoal,
  onSurface = TextPrimaryDark,
  surfaceVariant = ShimenaCharcoalSurface,
  onSurfaceVariant = TextSecondaryDark,
  outline = ShimenaCharcoalBorder,
  outlineVariant = ShimenaEarthDark
)

private val LightColorScheme = lightColorScheme(
  primary = ShimenaCharcoal,
  onPrimary = ShimenaCottonLight,
  primaryContainer = ShimenaCottonDark,
  onPrimaryContainer = ShimenaCharcoalDark,
  secondary = ShimenaEarth,
  onSecondary = ShimenaCottonLight,
  secondaryContainer = ShimenaWarmSand,
  onSecondaryContainer = ShimenaEarthDark,
  tertiary = ShimenaIndigo,
  onTertiary = ShimenaCottonLight,
  background = ShimenaCottonLight,
  onBackground = TextPrimaryLight,
  surface = ShimenaCotton,
  onSurface = TextPrimaryLight,
  surfaceVariant = ShimenaCottonDark,
  onSurfaceVariant = TextSecondaryLight,
  outline = ShimenaEarthLight,
  outlineVariant = ShimenaWarmSand
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Default to Shimena's signature light cotton canvas
  dynamicColor: Boolean = false, // Keep pure artisanal palette
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
