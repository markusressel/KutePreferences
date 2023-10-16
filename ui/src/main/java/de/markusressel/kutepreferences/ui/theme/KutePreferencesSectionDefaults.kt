package de.markusressel.kutepreferences.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object KutePreferencesSectionDefaults {

    val defaultTheme: SectionTheme
        @Composable
        get() {
            return SectionTheme(
                titleBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                titleTextColor = MaterialTheme.colorScheme.primary,
                contentBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
}

@Immutable
data class SectionTheme(
    val titleTextColor: Color = Color.White,
    val titleBackgroundColor: Color = Color.Black,
    val contentBackgroundColor: Color = Color.Black,
    val elevation: Dp = 8.dp,
)
