package com.voxly.shared.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VoxlyColorScheme = darkColorScheme(
    primary         = VoxlyColors.Coral,
    onPrimary       = VoxlyColors.TextPrimary,
    secondary       = VoxlyColors.Teal,
    onSecondary     = VoxlyColors.Background,
    tertiary        = VoxlyColors.Gold,
    background      = VoxlyColors.Background,
    onBackground    = VoxlyColors.TextPrimary,
    surface         = VoxlyColors.Surface,
    onSurface       = VoxlyColors.TextPrimary,
    surfaceVariant  = VoxlyColors.SurfaceElevated,
    onSurfaceVariant = VoxlyColors.TextSecondary,
    error           = VoxlyColors.Error,
)

@Composable
fun VoxlyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VoxlyColorScheme,
        typography  = VoxlyTypography,
        content     = content,
    )
}
