package de.markusressel.kutepreferences.ui.views.search

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.dummy
import de.markusressel.kutepreferences.ui.views.KuteRegistryForPreview
import de.markusressel.kutepreferences.ui.vm.SearchItemsUseCase

@Composable
fun KuteSearch(
    searchTerm: String,
    searchFocusRequester: FocusRequester,
    items: List<SearchItemsUseCase.KuteSearchResultItem>,
    onCancelSearch: () -> Unit,
    onSearchTermChanged: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onSearchResultSelected: (SearchItemsUseCase.KuteSearchResultItem) -> Unit,
    modifier: Modifier = Modifier,
    maxContentWidth: Dp = Dp.Infinity,
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
            when {
                items.isEmpty() && searchTerm.isNotBlank() -> KuteNoSearchResults(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    searchTerm = searchTerm,
                )

                else -> Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    KutePreferenceSearchingContent(
                        modifier = Modifier
                            .widthIn(max = maxContentWidth)
                            .fillMaxWidth()
                            .imePadding(),
                        items = items,
                        onSearchResultSelected = onSearchResultSelected,
                    )
                }
            }
        },
    )
}


@CombinedPreview
@Composable
private fun KuteSearchPreview() {
    KuteRegistryForPreview {
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
                    SearchItemsUseCase.KuteSearchResultItem(
                        key = "0",
                        item = KuteSection(
                            key = "0",
                            title = "Section",
                            children = listOf(
                                KuteBooleanPreference(
                                    key = "1",
                                    icon = icon,
                                    title = "A KuteBooleanPreference",
                                    defaultValue = true,
                                    dataProvider = dummy
                                ),
                                KuteTextPreference(
                                    key = "1",
                                    icon = icon,
                                    title = "A KuteTextPreference",
                                    defaultValue = "Default",
                                    dataProvider = dummy
                                ),
                                KuteNumberPreference(
                                    key = "1",
                                    icon = icon,
                                    title = "A KuteNumberPreference",
                                    defaultValue = 1337,
                                    dataProvider = dummy
                                ),
                            ),
                        ),
                        searchTerm = "search term",
                    ),
                    SearchItemsUseCase.KuteSearchResultItem(
                        key = "1",
                        item = KuteCategory(
                            key = "1",
                            title = "Category Title",
                            description = "This is a description of a category.",
                            children = listOf(
                                KuteTextPreference(
                                    key = "1",
                                    icon = icon,
                                    title = "Some Text Preference",
                                    defaultValue = "Default",
                                    dataProvider = dummy
                                )
                            )
                        ),
                        searchTerm = "search term",
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
}