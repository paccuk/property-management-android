package org.stkachenko.propertymanagement.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment
import androidx.compose.ui.text.style.LineHeightStyle.Trim
import androidx.compose.ui.unit.sp


internal val PmTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Bottom,
            trim = Trim.None,
        ),
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Bottom,
            trim = Trim.LastLineBottom,
        ),
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    // Default text style
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.None,
        ),
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    // Used for Button
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    ),
    // Used for Navigation items
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    ),
    // Used for Tag
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    ),
)

//import androidx.compose.ui.text.googlefonts.GoogleFont
//import androidx.compose.ui.text.googlefonts.Font
//val provider = GoogleFont.Provider(
//    providerAuthority = "com.google.android.gms.fonts",
//    providerPackage = "com.google.android.gms",
//    certificates = R.array.com_google_android_gms_fonts_certs
//)
//
//val bodyFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Inter"),
//        fontProvider = provider,
//    )
//)
//
//val displayFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Inter"),
//        fontProvider = provider,
//    )
//)
//
//// Default Material 3 typography values
//val baseline = Typography()
//
//val PmaTypography = Typography(
//    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
//    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
//    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
//    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
//    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
//    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
//    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
//    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
//    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
//    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
//    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
//    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
//    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
//    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
//    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
//)