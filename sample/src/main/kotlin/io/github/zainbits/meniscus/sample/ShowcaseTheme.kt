package io.github.zainbits.meniscus.sample

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowcaseTheme(palette: Palette, dark: Boolean, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val scheme = when (palette) {
        Palette.Dynamic -> if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        Palette.Graphite -> seeded(dark, primary = Color(0xFF9E9E9E), onPrimary = Color(0xFF111111), light = Color(0xFFF1F1F1), darkBg = Color(0xFF121212))
        Palette.Slate -> seeded(
            dark,
            primary = if (dark) Color(0xFF6B7075) else Color(0xFF4A4F54),
            onPrimary = Color.White,
            light = Color(0xFFEDEEEF),
            darkBg = Color(0xFF111315),
        )
        Palette.Ash -> seeded(
            dark,
            primary = if (dark) Color(0xFFC8C8C8) else Color(0xFF3A3A3A),
            onPrimary = if (dark) Color(0xFF1A1A1A) else Color(0xFFF5F5F5),
            light = Color(0xFFE6E6E6),
            darkBg = Color(0xFF1C1C1C),
        )
        Palette.Mono -> seeded(
            dark,
            primary = if (dark) Color(0xFFF2F2F2) else Color(0xFF151515),
            onPrimary = if (dark) Color(0xFF151515) else Color(0xFFF2F2F2),
            light = Color(0xFFF4F4F4),
            darkBg = Color(0xFF0F0F0F),
        )
    }
    MaterialTheme(colorScheme = scheme, content = content)
}

private fun seeded(dark: Boolean, primary: Color, onPrimary: Color, light: Color, darkBg: Color): ColorScheme =
    if (dark) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = darkBg.lift(0.22f),
            onPrimaryContainer = Color(0xFFEDEDED),
            secondary = Color(0xFFBDBDBD),
            onSecondary = Color(0xFF1A1A1A),
            secondaryContainer = darkBg.lift(0.25f),
            onSecondaryContainer = Color(0xFFF0F0F0),
            tertiary = Color(0xFFBDBDBD),
            surfaceVariant = darkBg.lift(0.12f),
            onSurfaceVariant = Color(0xFFB8B8B8),
            outline = darkBg.lift(0.35f),
            background = darkBg,
            surface = darkBg,
            surfaceContainer = darkBg.lift(0.05f),
            surfaceContainerHigh = darkBg.lift(0.09f),
            surfaceContainerHighest = darkBg.lift(0.13f),
            outlineVariant = darkBg.lift(0.2f),
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = light.lift(-0.12f),
            onPrimaryContainer = Color(0xFF1A1A1A),
            secondary = Color(0xFF5C5C5C),
            onSecondary = Color.White,
            secondaryContainer = light.lift(-0.13f),
            onSecondaryContainer = Color(0xFF1A1A1A),
            tertiary = Color(0xFF5C5C5C),
            surfaceVariant = light.lift(-0.06f),
            onSurfaceVariant = Color(0xFF5A5A5A),
            outline = light.lift(-0.35f),
            background = light,
            surface = light,
            surfaceContainer = Color.White,
            surfaceContainerHigh = Color.White,
            surfaceContainerHighest = light.lift(-0.04f),
            outlineVariant = light.lift(-0.12f),
        )
    }

/** Moves a color towards white (positive) or black (negative). */
private fun Color.lift(amount: Float): Color {
    val target = if (amount >= 0) 1f else 0f
    val t = kotlin.math.abs(amount)
    return Color(red + (target - red) * t, green + (target - green) * t, blue + (target - blue) * t, alpha)
}
