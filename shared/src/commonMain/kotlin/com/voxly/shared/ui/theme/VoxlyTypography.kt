package com.voxly.shared.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

val VoxlyTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = VoxlyColors.TextPrimary,
        lineHeight = 40.sp,
    ),
    titleLarge = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = VoxlyColors.TextPrimary,
        lineHeight = 32.sp,
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = VoxlyColors.TextPrimary,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = VoxlyColors.TextPrimary,
        lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = VoxlyColors.TextSecondary,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        color = VoxlyColors.TextTertiary,
        lineHeight = 16.sp,
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = VoxlyColors.TextSecondary,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        color = VoxlyColors.TextTertiary,
        lineHeight = 14.sp,
        letterSpacing = 0.06.em,
    ),
)
