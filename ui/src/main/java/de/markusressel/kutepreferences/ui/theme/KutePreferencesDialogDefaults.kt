package de.markusressel.kutepreferences.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

object KutePreferencesDialogDefaults {
    val defaultTheme: DialogTheme
        @Composable
        get() {
            return DialogTheme(
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                buttonTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                positiveButtonTextColor = MaterialTheme.colorScheme.onPrimary,
            )
        }
}

@Immutable
data class DialogTheme(
    val backgroundColor: Color = Color.Black,
    val buttonTextColor: Color = Color.Black,
    val positiveButtonTextColor: Color = Color.Black,
)