package de.markusressel.kutepreferences.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object KutePreferencesSearchBarDefaults {

    val defaultTheme: SearchBarTheme
        @Composable
        get() {
            return SearchBarTheme(
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                textColor = textColor,
                hintColor = MaterialTheme.colorScheme.onSurfaceVariant,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

    val textColor: Color
        @Composable
        get() {
            return when {
                isSystemInDarkTheme() -> Color.White.copy(alpha = 0.87f)
                else -> Color.Black.copy(alpha = 0.87f)
            }
        }
}


@Immutable
data class SearchBarTheme(
    val backgroundColor: Color = Color.White,
    val textColor: Color = Color.Black,
    val hintColor: Color = Color.Black,
    val iconColor: Color = Color.Black,
    val elevation: Dp = 8.dp,
)