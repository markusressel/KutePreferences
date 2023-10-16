package de.markusressel.kutepreferences.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalKuteColors = staticCompositionLocalOf { KuteColors() }

@Composable
fun KutePreferencesTheme(
    colors: KuteColors = KutePreferencesThemeDefaults.defaultColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalKuteColors provides colors,
        content = content,
    )
}