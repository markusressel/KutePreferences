package de.markusressel.kutepreferences.ui.vm

sealed class KuteUiEvent {
    data object BackPressed : KuteUiEvent()

    data object SearchFieldSelected : KuteUiEvent()
    data class SearchTermChanged(val searchTerm: String) : KuteUiEvent()
    data object CloseSearch : KuteUiEvent()
    data class SearchResultSelected(val searchResult: SearchItemsUseCase.KuteSearchResultItem) : KuteUiEvent()
}