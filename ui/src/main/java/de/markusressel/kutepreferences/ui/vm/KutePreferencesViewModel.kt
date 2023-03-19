package de.markusressel.kutepreferences.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.filterRecursive
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class KutePreferencesViewModel(
    val navigator: KuteNavigator,
) : ViewModel() {

    private val findCategoryByKeyUseCase: FindCategoryByKeyUseCase = FindCategoryByKeyUseCase()

    val preferencesUiState = MutableStateFlow(UiState())

    private val preferenceItems = MutableStateFlow<List<KutePreferenceListItem>>(emptyList())

    init {
        KuteStyleManager.registerDefaultStyles(navigator)

        viewModelScope.launch {
            navigator.currentCategory.collectLatest {
                updateCurrentlyDisplayedItems()
            }
        }

        viewModelScope.launch {
            preferenceItems.collectLatest {
                updateCurrentlyDisplayedItems(newPreferencesList = it)
            }
        }

        viewModelScope.launch {
            preferencesUiState.collectLatest {
                updateCurrentlyDisplayedItems(newUiState = it)
            }
        }
    }


    /**
     * Set the full tree of [KutePreferenceListItem] entries
     */
    open fun initPreferencesTree(items: List<KutePreferenceListItem>) {
        preferenceItems.value = items
    }

    open fun onUiEvent(event: KuteUiEvent) {
        when (event) {
            is KuteUiEvent.StartSearch -> {
                preferencesUiState.update { oldState ->
                    oldState.copy(
                        searching = true,
                        searchTerm = ""
                    )
                }
            }
            is KuteUiEvent.SearchTermChanged -> {
                preferencesUiState.update { oldState ->
                    oldState.copy(searchTerm = event.searchTerm)
                }
            }
            is KuteUiEvent.CloseSearch -> {
                preferencesUiState.update { oldState ->
                    oldState.copy(
                        searching = false,
                        searchTerm = ""
                    )
                }
            }
        }
    }

    private fun updateCurrentlyDisplayedItems(
        newUiState: UiState? = null,
        newPreferencesList: List<KutePreferenceListItem>? = null
    ) {
        val currentCategoryKey = navigator.currentCategory.value
        val allPreferenceItems = newPreferencesList ?: preferenceItems.value

        preferencesUiState.update { oldState ->
            val targetState = (newUiState ?: oldState)
            targetState.copy(
                preferenceItems = when {
                    targetState.searching -> searchItems(allPreferenceItems, targetState.searchTerm)
                    currentCategoryKey != null -> findCategoryByKeyUseCase(allPreferenceItems, currentCategoryKey)?.children
                        ?: emptyList()
                    else -> allPreferenceItems
                }
            )
        }
    }

    private fun searchItems(items: List<KutePreferenceListItem>, searchTerm: String): List<KutePreferenceListItem> {
        return when {
            searchTerm.isBlank() -> emptyList()
            else -> items.filterRecursive(searchTerm)
        }
    }
}

data class UiState(
    val searching: Boolean = false,
    val searchTerm: String = "",
    val preferenceItems: List<KutePreferenceListItem> = emptyList(),
)