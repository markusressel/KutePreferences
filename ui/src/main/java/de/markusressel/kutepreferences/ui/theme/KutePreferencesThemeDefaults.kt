package de.markusressel.kutepreferences.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object KutePreferencesThemeDefaults {
    val defaultColors: KuteColors
        @Composable
        get() {
            return KuteColors(
                searchBar = KutePreferencesSearchBarDefaults.defaultTheme,
                section = KutePreferencesSectionDefaults.defaultTheme,
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
        }
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
data class DialogTheme(
    val backgroundColor: Color = Color.Black,
    val buttonTextColor: Color = Color.Black,
)


val itemShape = RoundedCornerShape(12.dp)
val listItemMinHeight = 64.dp