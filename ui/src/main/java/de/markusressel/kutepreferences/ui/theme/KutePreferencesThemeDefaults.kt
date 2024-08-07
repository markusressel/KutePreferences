package de.markusressel.kutepreferences.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

object KutePreferencesThemeDefaults {
    val defaultColors: KuteColors
        @Composable
        get() {
            return KuteColors(
                dialog = KutePreferencesDialogDefaults.defaultTheme,
                search = KutePreferencesSearchDefaults.defaultTheme,
                section = KutePreferencesSectionDefaults.defaultTheme,
                category = KutePreferencesCategoryDefaults.defaultTheme,
                defaultItem = KutePreferencesDefaultItemDefaults.defaultTheme,
            )
        }
}


@Immutable
data class KuteColors(
    val dialog: DialogTheme = DialogTheme(),
    val search: SearchTheme = SearchTheme(),
    val section: SectionTheme = SectionTheme(),
    val category: CategoryTheme = CategoryTheme(),
    val defaultItem: DefaultItemTheme = DefaultItemTheme(),
)

val itemShape = RoundedCornerShape(12.dp)
val listItemMinHeight = 64.dp