package com.example.scicalc.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CalculatorTypography = Typography(
    displayLarge = TextStyle(FontFamily.Default, FontWeight.Light, 48.sp, 56.sp, 0.sp),
    displayMedium = TextStyle(FontFamily.Default, FontWeight.Light, 36.sp, 44.sp, 0/sp),
    headlineLarge = TextStyle(FontFamily.Default, FontWeight.Normal, 28.sp, 36.sp, 0/sp),
    titleLarge = TextStyle(FontFamily.Default, FontWeight.Medium, 22.sp, 28.sp, 0/sp),
    titleMedium = TextStyle(FontFamily.Default, FontWeight.Medium, 16.sp, 24.sp, 0.15.sp),
    titleSmall = TextStyle(FontFamily.Default, FontWeight.Medium, 14.sp, 20.sp, 0.1.sp),
    bodyLarge = TextStyle(FontFamily.Default, FontWeight.Normal, 16.sp, 24.sp, 0.5.sp),
    bodyMedium = TextStyle(FontFamily.Default, FontWeight.Normal, 14.sp, 20.sp, 0.25.sp),
    labelLarge = TextStyle(FontFamily.Default, FontWeight.Medium, 18.sp, 24.sp, 0.1.sp),
    labelMedium = TextStyle(FontFamily.Default, FontWeight.Medium, 14.sp, 20.sp, 0.5.sp),
    labelSmall = TextStyle(FontFamily.Default, FontWeight.Medium, 11.sp, 16.sp, 0.5.sp)
)