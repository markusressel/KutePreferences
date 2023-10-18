package de.markusressel.kutepreferences.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

object KutePreferencesDefaultItemDefaults {
    val defaultTheme: DefaultItemTheme
        @Composable
        get() {
            return DefaultItemTheme(
                titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                subtitleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
}


@Immutable
data class DefaultItemTheme(
    val titleColor: Color = Color.Black,
    val subtitleColor: Color = Color.Black,
    val iconColor: Color = Color.Black,
)
