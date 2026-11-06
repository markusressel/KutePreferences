package de.markusressel.kutepreferences.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object KutePreferencesCategoryDefaults {
    val defaultTheme: CategoryTheme
        @Composable
        get() {
            return CategoryTheme(
                cardBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                titleColor = MaterialTheme.colorScheme.primary,
                subtitleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedCardBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        }
}

/**
 * @param selectedCardBackgroundColor the card of the category that is open next to the list, on a large screen.
 * [Color.Unspecified] uses the `secondaryContainer` color of the [MaterialTheme].
 * @param shape the shape of the card
 */
@Immutable
data class CategoryTheme(
    val cardBackgroundColor: Color = Color.White,
    val titleColor: Color = Color.Black,
    val subtitleColor: Color = Color.Black,
    val iconColor: Color = Color.Black,
    val elevation: Dp = 8.dp,
    val selectedCardBackgroundColor: Color = Color.Unspecified,
    val shape: Shape = itemShape,
)
