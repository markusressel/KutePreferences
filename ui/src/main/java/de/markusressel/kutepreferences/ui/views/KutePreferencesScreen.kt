package de.markusressel.kutepreferences.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import de.markusressel.kutepreferences.ui.vm.KuteUiEvent
import de.markusressel.kutepreferences.ui.vm.UiAction
import de.markusressel.kutepreferences.ui.vm.UiState

val dummy = DummyDataProvider()

@Composable
fun KutePreferencesScreen(
    modifier: Modifier = Modifier,
    kuteViewModel: KutePreferencesViewModel
) {
    val uiState by kuteViewModel.preferencesUiState.collectAsState()

    val searchFocusRequester = remember { FocusRequester() }

    Column(modifier) {
        when (val currentUiState = uiState) {
            is UiState.Searching -> {
                BackHandler {
                    searchFocusRequester.freeFocus()
                    kuteViewModel.onUiEvent(KuteUiEvent.CloseSearch)
                }

                LaunchedEffect(Unit) {
                    searchFocusRequester.requestFocus()
                }

                KuteSearch(
                    searchTerm = currentUiState.searchTerm,
                    onSearchTermChanged = { kuteViewModel.onUiEvent(KuteUiEvent.SearchTermChanged(it)) },
                    items = currentUiState.preferenceItems,
                    searchFocusRequester = searchFocusRequester,
                    onClearSearchTerm = { kuteViewModel.onUiEvent(KuteUiEvent.SearchTermChanged("")) },
                    onCancelSearch = { kuteViewModel.onUiEvent(KuteUiEvent.CloseSearch) },
                )
            }
            is UiState.Overview -> {
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
                    items = currentUiState.preferenceItems,
                    onSearchStarted = { kuteViewModel.onUiEvent(KuteUiEvent.StartSearch) },
                    scrollState = scrollState,
                )
            }
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun KutePreferenceListItem.Composable() {
    val styleManager = remember { KuteStyleManager }
    styleManager.renderComposable(this)
}

