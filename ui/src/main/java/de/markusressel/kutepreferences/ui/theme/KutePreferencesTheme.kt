package de.markusressel.kutepreferences.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun defaultColors() = KuteColors(
    searchBar = SearchBarTheme(
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        textColor = KutePreferencesSearchBarDefaults.textColor,
        hintColor = MaterialTheme.colorScheme.onSurfaceVariant,
        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    section = SectionTheme(
        titleBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        titleTextColor = MaterialTheme.colorScheme.primary,
        contentBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
    ),
    category = CategoryTheme(
        cardBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        titleColor = MaterialTheme.colorScheme.primary,
        subtitleColor = MaterialTheme.colorScheme.onSurfaceVariant,
        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    defaultItem = DefaultItemTheme(
        titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
        subtitleColor = MaterialTheme.colorScheme.onSurfaceVariant,
        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    dialog = DialogTheme(
        backgroundColor = MaterialTheme.colorScheme.background,
        buttonTextColor = MaterialTheme.colorScheme.onBackground,
    )
)

object KutePreferencesSearchBarDefaults {

    val textColor: Color
        @Composable
        get() {
            return when {
                isSystemInDarkTheme() -> Color.White.copy(alpha = 0.87f)
                else -> Color.Black.copy(alpha = 0.87f)
            }
        }
}

val LocalKuteColors = staticCompositionLocalOf { KuteColors() }

@Composable
fun KutePreferencesTheme(
    colors: KuteColors = defaultColors(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalKuteColors provides colors,
        content = content,
    )
}


@Immutable
data class KuteColors(
    val dialog: DialogTheme = DialogTheme(),
    val searchBar: SearchBarTheme = SearchBarTheme(),
    val section: SectionTheme = SectionTheme(),
    val category: CategoryTheme = CategoryTheme(),
    val defaultItem: DefaultItemTheme = DefaultItemTheme(),
)

@Immutable
data class DefaultItemTheme(
    val titleColor: Color = Color.Black,
    val subtitleColor: Color = Color.Black,
    val iconColor: Color = Color.Black,
)

@Immutable
data class CategoryTheme(
    val cardBackgroundColor: Color = Color.White,
    val titleColor: Color = Color.Black,
    val subtitleColor: Color = Color.Black,
    val iconColor: Color = Color.Black,
    val elevation: Dp = 8.dp,
)

@Immutable
data class SectionTheme(
    val titleTextColor: Color = Color.White,
    val titleBackgroundColor: Color = Color.Black,
    val contentBackgroundColor: Color = Color.Black,
    val elevation: Dp = 8.dp,
)

@Immutable
data class SearchBarTheme(
    val backgroundColor: Color = Color.White,
    val textColor: Color = Color.Black,
    val hintColor: Color = Color.Black,
    val iconColor: Color = Color.Black,
    val elevation: Dp = 8.dp,
)

@Immutable
data class DialogTheme(
    val backgroundColor: Color = Color.Black,
    val buttonTextColor: Color = Color.Black,
)


val itemShape = RoundedCornerShape(12.dp)
val listItemMinHeight = 64.dp