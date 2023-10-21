package de.markusressel.kutepreferences.ui.views.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.ui.views.KuteOverview
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import de.markusressel.kutepreferences.ui.vm.KuteUiEvent
import de.markusressel.kutepreferences.ui.vm.UiAction

val dummy = DummyDataProvider()

@Composable
fun KutePreferencesScreen(
    modifier: Modifier = Modifier,
    kuteViewModel: KutePreferencesViewModel
) {
    val uiState by kuteViewModel.preferencesUiState.collectAsState()

    val searchFocusRequester = remember { FocusRequester() }

    Column(modifier) {
        BackHandler(enabled = uiState.isSearchActive || uiState.currentCategory != null) {
            searchFocusRequester.freeFocus()
            kuteViewModel.onUiEvent(KuteUiEvent.BackPressed)
        }

        KuteSearch(
            modifier = Modifier
                .fillMaxWidth(),
            active = uiState.isSearchActive,
            onActiveChange = { newActive ->
                when {
                    newActive -> kuteViewModel.onUiEvent(KuteUiEvent.SearchFieldSelected)
                    else -> kuteViewModel.onUiEvent(KuteUiEvent.CloseSearch)
                }
            },
            searchTerm = uiState.searchTerm,
            onSearchTermChanged = {
                kuteViewModel.onUiEvent(KuteUiEvent.SearchTermChanged(it))
            },
            items = uiState.preferenceItems,
            searchFocusRequester = searchFocusRequester,
            onCancelSearch = { kuteViewModel.onUiEvent(KuteUiEvent.CloseSearch) },
            onSearchResultSelected = { kuteViewModel.onUiEvent(KuteUiEvent.SearchResultSelected(it)) },
        )


        val scrollState = rememberScrollState()

        LaunchedEffect(Unit) {
            kuteViewModel.uiActions.collect { action ->
                when (action) {
                    is UiAction.ScrollToTop -> {
                        if (action.animate) {
                            scrollState.animateScrollTo(0)
                        } else {
                            scrollState.scrollTo(0)
                        }
                    }
                }
            }
        }

        KuteOverview(
            items = uiState.preferenceItems,
            onSearchStarted = { kuteViewModel.onUiEvent(KuteUiEvent.SearchFieldSelected) },
            scrollState = scrollState,
        )
    }

    if (uiState.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun KutePreferenceListItem.Composable() {
    val styleManager = remember { KuteStyleManager }
    KuteStyleManager.renderComposable(this)
}

