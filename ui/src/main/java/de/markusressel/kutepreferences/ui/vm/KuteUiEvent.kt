package de.markusressel.kutepreferences.ui.vm

sealed class KuteUiEvent {
    object StartSearch : KuteUiEvent()
    data class SearchTermChanged(val searchTerm: String) : KuteUiEvent()
    object CloseSearch : KuteUiEvent()
}