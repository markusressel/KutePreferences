package de.markusressel.kutepreferences.ui.views

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import de.markusressel.kutepreferences.ui.vm.KuteUiEvent

val dummy = DummyDataProvider()

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KutePreferencesScreen(
    modifier: Modifier = Modifier,
    kuteViewModel: KutePreferencesViewModel
) {
    val uiState by kuteViewModel.preferencesUiState.collectAsState()

    val searchFocusRequester = remember { FocusRequester() }

    BackHandler(enabled = uiState.searching) {
        searchFocusRequester.freeFocus()
        kuteViewModel.onUiEvent(KuteUiEvent.CloseSearch)
    }

    AnimatedContent(
        modifier = modifier,
        targetState = uiState.searching,
    ) { isSearching ->
        if (isSearching) {
            KuteSearch(
                searchTerm = uiState.searchTerm,
                onSearchTermChanged = { kuteViewModel.onUiEvent(KuteUiEvent.SearchTermChanged(it)) },
                items = uiState.preferenceItems,
                searchFocusRequester = searchFocusRequester,
                onClearSearchTerm = { kuteViewModel.onUiEvent(KuteUiEvent.SearchTermChanged("")) },
                onCancelSearch = { kuteViewModel.onUiEvent(KuteUiEvent.CloseSearch) },
            )
        } else {
            KuteOverview(
                items = uiState.preferenceItems,
                onSearchStarted = { kuteViewModel.onUiEvent(KuteUiEvent.StartSearch) },
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KutePreferencesScreenPreview() {
    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    KuteOverview(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
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
        onSearchStarted = { },
    )
}

@Composable
fun KuteItemList(
    modifier: Modifier = Modifier,
    items: List<KutePreferenceListItem>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items.forEach {
            it.Composable()
        }
    }
}

@Composable
fun KutePreferenceListItem.Composable() {
    val styleManager = remember { KuteStyleManager }
    styleManager.renderComposable(this)
}

