package de.markusressel.kutepreferences.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

object KutePreferencesSearchDefaults {

    val defaultTheme: SearchTheme
        @Composable
        get() {
            return SearchTheme(
                shimmerColor = MaterialTheme.colorScheme.primary,
                searchBar = KutePreferencesSearchBarDefaults.defaultTheme,
            )
        }
}


@Immutable
data class SearchTheme(
    val shimmerColor: Color = Color.Black,
    val searchBar: SearchBarTheme = SearchBarTheme(),
)