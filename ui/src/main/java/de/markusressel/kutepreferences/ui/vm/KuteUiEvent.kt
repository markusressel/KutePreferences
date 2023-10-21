package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem

sealed class KuteUiEvent {
    data object BackPressed : KuteUiEvent()

    data object SearchFieldSelected : KuteUiEvent()
    data class SearchTermChanged(val searchTerm: String) : KuteUiEvent()
    data object CloseSearch : KuteUiEvent()
    data class SearchResultSelected(val item: KutePreferenceListItem) : KuteUiEvent()
}