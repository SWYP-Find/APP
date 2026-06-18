package com.picke.app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.picke.app.ui.theme.tokens.BrandColorTokens
import com.picke.app.ui.theme.tokens.ComponentColorTokens
import com.picke.app.ui.theme.tokens.SemanticColorTokens

data class SwypColors(
    // Text
    val textPrimary: Color = SemanticColorTokens.textPrimary,
    val textSecondary: Color = SemanticColorTokens.textSecondary,
    val textTertiary: Color = SemanticColorTokens.textTertiary,
    val textMuted: Color = SemanticColorTokens.textMuted,
    val textInverse: Color = SemanticColorTokens.textInverse,
    val textBrand: Color = SemanticColorTokens.textBrand,

    // Border
    val borderDefault: Color = SemanticColorTokens.borderDefault,
    val borderSubtle: Color = SemanticColorTokens.borderSubtle,
    val borderDisabled: Color = SemanticColorTokens.borderDisabled,
    val borderSelected: Color = SemanticColorTokens.borderSelected,
    val borderStrong: Color = SemanticColorTokens.borderStrong,
    val borderFocus: Color = SemanticColorTokens.borderFocus,
    val borderError: Color = SemanticColorTokens.borderError,

    // Surface
    val surfaceDefault: Color = SemanticColorTokens.surfaceDefault,
    val surfaceSubtle: Color = SemanticColorTokens.surfaceSubtle,
    val surfaceTertiary: Color = SemanticColorTokens.surfaceTertiary,
    val surfaceSelected: Color = SemanticColorTokens.surfaceSelected,
    val surfaceDisabled: Color = SemanticColorTokens.surfaceDisabled,

    // Background
    val backgroundDefault: Color = SemanticColorTokens.backgroundDefault,
    val backgroundSubtle: Color = SemanticColorTokens.backgroundSubtle,
    val backgroundTertiary: Color = SemanticColorTokens.backgroundTertiary,
    val backgroundBrand: Color = SemanticColorTokens.backgroundBrand,
    val backgroundInverse: Color = SemanticColorTokens.backgroundInverse,
    val backgroundOverlay: Color = SemanticColorTokens.backgroundOverlay,

    // Status
    val statusError: Color = SemanticColorTokens.statusErrorError,
    val statusWarning: Color = SemanticColorTokens.statusWarningWarning,

    // Component — Button
    val buttonPrimaryBackground: Color = ComponentColorTokens.buttonPrimaryBackgroundDefault,
    val buttonPrimaryBackgroundPressed: Color = ComponentColorTokens.buttonPrimaryBackgroundPressed,
    val buttonPrimaryBackgroundDisabled: Color = ComponentColorTokens.buttonPrimaryBackgroundDisabled,
    val buttonPrimaryText: Color = ComponentColorTokens.buttonPrimaryTextDefault,
    val buttonSecondaryBackground: Color = ComponentColorTokens.buttonSecondaryBackgroundDefault,
    val buttonSecondaryBackgroundPressed: Color = ComponentColorTokens.buttonSecondaryBackgroundPressed,
    val buttonSecondaryBorder: Color = ComponentColorTokens.buttonSecondaryBorderDefault,
    val buttonSecondaryBorderPressed: Color = ComponentColorTokens.buttonSecondaryBorderPressed,
    val buttonSecondaryText: Color = ComponentColorTokens.buttonSecondaryTextDefault,
    val buttonSecondaryTextDisabled: Color = ComponentColorTokens.buttonSecondaryTextDisabled,

    // Component — Input
    val inputBorderDefault: Color = ComponentColorTokens.inputBorderDefault,
    val inputBorderActive: Color = ComponentColorTokens.inputBorderActive,
    val inputBorderError: Color = ComponentColorTokens.inputBorderError,
    val inputSurface: Color = ComponentColorTokens.inputSurfaceDefault,
    val inputSurfaceDisabled: Color = ComponentColorTokens.inputSurfaceDisabled,
    val inputTextDefault: Color = ComponentColorTokens.inputTextDefault,
    val inputTextActive: Color = ComponentColorTokens.inputTextActive,
    val inputTextError: Color = ComponentColorTokens.inputTextError,

    // Component — Badge
    val badgeBackground: Color = ComponentColorTokens.bedgeFilledBackgroundDefault,
    val badgeBackgroundInverse: Color = ComponentColorTokens.bedgeFilledBackgroundInverse,
    val badgeText: Color = ComponentColorTokens.bedgeFilledTextDefault,
    val badgeTextInverse: Color = ComponentColorTokens.bedgeFilledTextInverse,
    val badgeOutlineText: Color = ComponentColorTokens.bedgeOutlineText,
    val badgeOutlineBackground: Color = ComponentColorTokens.bedgeOutlineBackround,
    val badgeOutlineBorder: Color = ComponentColorTokens.bedgeOutlineBorder,

    // Brand palette — colors not covered by semantic tokens
    val primary: Color = BrandColorTokens.primary500,
    val primaryPressed: Color = BrandColorTokens.primary600,
    val primaryDark: Color = BrandColorTokens.primary800,
    val primaryDarkest: Color = BrandColorTokens.primary900,
    val primaryDisabled: Color = BrandColorTokens.primary300,
    val primaryLight: Color = BrandColorTokens.primary50,
    val secondary: Color = BrandColorTokens.secondary500,
    val secondaryLight: Color = BrandColorTokens.secondary200,
    val secondary50: Color = BrandColorTokens.secondary50,
    val secondary100: Color = BrandColorTokens.secondary100,
    val secondary300: Color = BrandColorTokens.secondary300,
    val secondary700: Color = BrandColorTokens.secondary700,
    val neutral200: Color = BrandColorTokens.neutral200,
    val neutral400: Color = BrandColorTokens.neutral400,
    val neutral600: Color = BrandColorTokens.neutral600,
    val beige100: Color = BrandColorTokens.beige100,
    val beige800: Color = BrandColorTokens.beige800,
    val beige900: Color = BrandColorTokens.beige900,

    // Backward-compat aliases for existing SwypTheme.colors usages
    val surface: Color = SemanticColorTokens.surfaceDefault,
    val outline: Color = SemanticColorTokens.textMuted,
)

val LocalSwypColors = staticCompositionLocalOf { SwypColors() }
