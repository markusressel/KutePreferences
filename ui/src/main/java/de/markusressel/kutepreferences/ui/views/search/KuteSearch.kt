package de.markusressel.kutepreferences.ui.views.search

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.KuteStyleManager

@Composable
fun KuteSearch(
    modifier: Modifier = Modifier,
    searchTerm: String,
    searchFocusRequester: FocusRequester,
    items: List<KutePreferenceListItem>,
    onCancelSearch: () -> Unit,
    onSearchTermChanged: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onSearchResultSelected: (KutePreferenceListItem) -> Unit,
) {
    KutePreferenceSearch(
        modifier = modifier,
        active = active,
        searchTerm = searchTerm,
        onActiveChange = onActiveChange,
        onSearchTermChanged = onSearchTermChanged,
        onCloseSearch = {
            onCancelSearch()
        },
        focusRequester = searchFocusRequester,
        searchContent = {
            KutePreferenceSearchingContent(
                modifier = Modifier.fillMaxWidth(),
                items = items,
                onSearchResultSelected = onSearchResultSelected,
            )
        },
    )
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KuteSearchPreview() {
    remember {
        val nav = DefaultKuteNavigator()
        KuteStyleManager.registerDefaultStyles(nav)
        false
    }


    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    KutePreferencesTheme {
        KuteSearch(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            searchTerm = "some search term",
            searchFocusRequester = remember { FocusRequester() },
            items = listOf(
                KuteSection(
                    key = 0,
                    title = "Section",
                    children = listOf(
                        KuteBooleanPreference(
                            key = 1,
                            icon = icon,
                            title = "A KuteBooleanPreference",
                            defaultValue = true,
                            dataProvider = dummy
                        ),
                        KuteTextPreference(
                            key = 1,
                            icon = icon,
                            title = "A KuteTextPreference",
                            defaultValue = "Default",
                            dataProvider = dummy
                        ),
                        KuteNumberPreference(
                            key = 1,
                            icon = icon,
                            title = "A KuteNumberPreference",
                            defaultValue = 1337,
                            dataProvider = dummy
                        ),
                    ),

                    ),
                KuteCategory(
                    key = 1,
                    title = "Category Title",
                    description = "This is a description of a category.",
                    children = listOf(
                        KuteTextPreference(
                            key = 1,
                            icon = icon,
                            title = "Some Text Preference",
                            defaultValue = "Default",
                            dataProvider = dummy
                        )
                    )
                ),
            ),
            onCancelSearch = { },
            onSearchTermChanged = { },
            active = true,
            onActiveChange = { },
            onSearchResultSelected = { },
        )
    }
}